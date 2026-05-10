package com.resumeradar.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String jobId;

    @Column(name="title" , nullable = false)
    private String title;

    @Lob 
    @Column(name="description" , nullable = false)
    private String description;

    @Lob
    @Column(name="required_skills" , nullable = false)
    private String requiredSkills;

    @Column(name="location" , nullable = false)
    private String location;

    @Column(name="salary_range" , nullable = false)
    private String salaryRange;

    @CreationTimestamp
    private LocalDateTime postedAt;

    @Column(name="is_active" , nullable = false)
    private Boolean isActive = true;

    @Column(name="company" , nullable = false)
    private String company;
    
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    @JsonIgnore
    private User recruiter;

	public Job(String title, String description, String requiredSkills, String location, String salaryRange,
			User recruiter , String company) {
		super();
		this.title = title;
		this.description = description;
		this.requiredSkills = requiredSkills;
		this.location = location;
		this.salaryRange = salaryRange;
		this.recruiter = recruiter;
		this.company = company;
	}
}

