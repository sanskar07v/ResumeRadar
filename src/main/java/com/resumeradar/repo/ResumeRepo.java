package com.resumeradar.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumeradar.entity.Resume;

@Repository
public interface ResumeRepo extends JpaRepository<Resume, String>{

}
