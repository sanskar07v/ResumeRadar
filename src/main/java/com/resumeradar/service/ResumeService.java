package com.resumeradar.service;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resumeradar.entity.Resume;
import com.resumeradar.entity.User;
import com.resumeradar.exception.UnauthorizedUserException;
import com.resumeradar.repo.ResumeRepo;
import com.resumeradar.repo.UserRepo;
import com.resumeradar.utils.SkillSet;

import jakarta.transaction.Transactional;

@Service
public class ResumeService {

	@Autowired
	private ResumeRepo resRepo;

	@Autowired
	private UserRepo userRepo;

	private static final LevenshteinDistance distance = LevenshteinDistance.getDefaultInstance();

	public static Set<String> matchSkills(String text) {
		Set<String> knownSkills = SkillSet.getSkillSet(); // Your skills
		Set<String> matchedSkills = new HashSet<>();
		String normalizedText = text.toLowerCase();

		for (String skill : knownSkills) {
			String normalizedSkill = skill.toLowerCase().trim();

			if (normalizedSkill.isEmpty())
				continue;

			if (normalizedSkill.length() <= 2) {
				// Match only whole word for short skills (like "c", "r")
				Pattern pattern = Pattern.compile("\\b" + Pattern.quote(normalizedSkill) + "\\b");
				Matcher matcher = pattern.matcher(normalizedText);
				if (matcher.find()) {
					matchedSkills.add(skill);
				}
			} else {
				// Longer skill: check exact phrase match
				if (normalizedText.contains(normalizedSkill)) {
					matchedSkills.add(skill);
					continue;
				}
				// Fuzzy match for longer skills
				String[] words = normalizedText.split("\\W+");
				for (String word : words) {
					String singular = toSingular(word);
					int fuzz = distance.apply(singular, normalizedSkill);
					if (fuzz == 1 || (fuzz == 2 && normalizedSkill.length() > 5)) {
						matchedSkills.add(skill);
						break;
					}
				}
			}
		}
		return matchedSkills;
	}

	private static String toSingular(String word) {
		if (word.endsWith("s") && word.length() > 3) {
			return word.substring(0, word.length() - 1);
		}
		return word;
	}

	@Transactional
	@PreAuthorize("hasRole('JOB_SEEKER')")
	public Resume uploadResume(String resumePath, MultipartFile file) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User user) {
			String skills = String.join(", ", matchSkills(extractText(file)));
			Resume res;
			if (user.getResumes() == null) {
				res = new Resume(resumePath, skills, user);
				user.setResumes(res);
			} else {
				res = user.getResumes();
				res.setResumePath(resumePath);
				res.setSkills(skills);
			}
			resRepo.save(res);
			userRepo.save(user);
			return res;
		} else {
			throw new UnauthorizedUserException("User is not authenticated or invalid");
		}
	}

	public String extractText(MultipartFile file) throws Exception {
		InputStream inputStream = file.getInputStream();
		PDDocument document = PDDocument.load(inputStream);
		PDFTextStripper stripper = new PDFTextStripper();
		return stripper.getText(document);
	}

}
