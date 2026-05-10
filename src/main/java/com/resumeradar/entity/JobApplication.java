package com.resumeradar.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
	    name = "job_applications",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"seeker_id", "job_id"})
	    }
	)
@Data
@NoArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String applicationId;	

    @ManyToOne
    @JoinColumn(name = "job_id" , nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "seeker_id" , nullable=false)
    private User seeker;

    @ManyToOne
    @JoinColumn(name = "resume_id" , nullable=false)
    private Resume resume;

    @Enumerated(EnumType.STRING)
    @Column(name="status" , nullable=false)
    private ApplicationStatus status;

    @CreationTimestamp
    private LocalDateTime appliedAt;

	public JobApplication(Job job, User seeker, Resume resume) {
		super();
		this.job = job;
		this.seeker = seeker;
		this.resume = resume;
		this.status = ApplicationStatus.PENDING;
	}
}
