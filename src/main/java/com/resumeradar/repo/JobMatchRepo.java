package com.resumeradar.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumeradar.entity.Job;
import com.resumeradar.entity.JobMatch;
import com.resumeradar.entity.User;

@Repository
public interface JobMatchRepo extends JpaRepository<JobMatch, String>{

	List<JobMatch> findBySeeker(User user);

	JobMatch findBySeekerAndJob(User user, Job job);

}
