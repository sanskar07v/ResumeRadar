package com.resumeradar.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.resumeradar.entity.Job;
import com.resumeradar.entity.JobApplication;
import com.resumeradar.entity.Resume;
import com.resumeradar.entity.User;
import com.resumeradar.model.ApiResponse;
import com.resumeradar.service.JobService;
import com.resumeradar.service.ResumeService;

import jakarta.mail.MessagingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/candidate")
public class CandidateController {

	@Autowired
	private ResumeService resService;

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Autowired
	private JobService jobService;

	@PostMapping("/uploadresume")
	public ResponseEntity<ApiResponse> uploadResume(@RequestParam("file") MultipartFile file) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !(auth.getPrincipal() instanceof User user)) {
			log.warn("User is unauthorized");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ApiResponse(false, null, "Unauthorized access"));
		}

		String filename = StringUtils.cleanPath(
			user.getUserId() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'))
		);

		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		Path filePath = uploadPath.resolve(filename);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		Resume res = resService.uploadResume(filePath.toString(), file);
		if (res != null) {
			jobService.match(user);
			log.info("Resume uploaded successfully");
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse(true, res, "Resume uploaded successfully!!"));
		}
		log.warn("User is not uploaded");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ApiResponse(false, null, "Resume not uploaded"));
	}


	@PostMapping("/apply/job/{jobId}")
	public ResponseEntity<ApiResponse> applyJob(@PathVariable String jobId) throws MessagingException {
		JobApplication jobApp = null;

		jobApp = jobService.applyJob(jobId);
		if (jobApp != null) {
			log.info("Applied for job is successfully saved.");
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, jobApp, "Your Application is successfully added"));
		}
		log.warn("Your Application is not saved");
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ApiResponse(false, null, "Your Application is not saved"));
	}

	@GetMapping("/get/job")
	public ResponseEntity<ApiResponse> getJobByMatch() {

		List<Job> jobs = jobService.getJobByMatch();
		if (jobs != null && !jobs.isEmpty()) {
			log.info("Job is matched with user is returned");
			return ResponseEntity.ok(new ApiResponse(true, jobs, "Matched Job"));
		}
		log.warn("No job is matched");
		return ResponseEntity.ok(new ApiResponse(true, jobService.getAllJob(), "All Jobs"));
	}
}
