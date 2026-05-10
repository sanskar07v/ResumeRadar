package com.resumeradar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumeradar.entity.ApplicationStatus;
import com.resumeradar.entity.Job;
import com.resumeradar.entity.JobApplication;
import com.resumeradar.entity.User;
import com.resumeradar.model.ApiResponse;
import com.resumeradar.model.JobRegModel;
import com.resumeradar.service.JobService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/recruiter")
public class RecruiterController {
	
	@Autowired
	private JobService jobService;
	
	@PostMapping("/upload/job")
	public ResponseEntity<ApiResponse> uploadJob(@Valid @RequestBody JobRegModel model){
		Job job = jobService.addJob(model);
		if(job!=null) {
			log.info("Job is created");
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, job, "Job is added successfully!!"));
		}
		log.warn("Job can not able to create");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, null, "Job is not  added"));
	}
	
	
	@PostMapping("/process/job/{applicantId}")
	public ResponseEntity<ApiResponse> processApplication(@PathVariable String applicantId,@RequestBody ApplicationStatus appStatus) throws MessagingException {

	    JobApplication jobApp = jobService.getJobAppById(applicantId);

	    if (jobApp == null) {
	    	log.warn("No Job Application Found");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(new ApiResponse(false, null, "No Job Application Found!!"));
	    }

	    if (appStatus == null) {
	    	log.warn("Application status is missing");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(new ApiResponse(false, null, "Application status is missing"));
	    }

	    jobService.updateJobApp(jobApp, appStatus);
	    log.info("Job Application's Status updated");
	    return ResponseEntity.ok(new ApiResponse(true, jobApp, "Status Changed"));
	}

	
	@GetMapping("/get/user/application/{jobId}")
	public ResponseEntity<ApiResponse> getJobAppByJobId(@PathVariable String jobId){
		Job job = jobService.getJobById(jobId);
		List<User> users = jobService.getCandidateByJobApp(job);
		if(users!=null) {
			return ResponseEntity.ok(new ApiResponse(true, users, "User List"));
		}
		return ResponseEntity.ok(new ApiResponse(false, null, "No Data is found"));
		
	}
	
}
