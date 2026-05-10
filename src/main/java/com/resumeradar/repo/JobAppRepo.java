package com.resumeradar.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumeradar.entity.Job;
import com.resumeradar.entity.JobApplication;

@Repository
public interface JobAppRepo extends JpaRepository<JobApplication, String>{

	List<JobApplication> findByJob(Job job);

	Optional<JobApplication> findByApplicationId(String applicantId);

}
