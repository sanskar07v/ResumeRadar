package com.resumeradar.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumeradar.entity.Job;

@Repository
public interface JobRepo extends JpaRepository<Job, String>{

	List<Job> findByIsActive(boolean b);

	Optional<Job> findByJobIdAndIsActive(String jobId, boolean b);

}
