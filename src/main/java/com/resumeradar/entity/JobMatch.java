package com.resumeradar.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	    name = "job_matches",
	    uniqueConstraints = {
	    		@UniqueConstraint(columnNames = {"seeker_id", "job_id"})
	    }
	)
@Data
@NoArgsConstructor
public class JobMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String matchId;

    @ManyToOne
    @JoinColumn(name = "seeker_id" , nullable=false)
    private User seeker;

    @ManyToOne
    @JoinColumn(name = "job_id" , nullable=false)
    private Job job;

    @Column(name="match_score" , nullable=false)
    private Double matchScore;

    @CreationTimestamp
    private LocalDateTime matchedOn;

	public JobMatch(User seeker, Job job, Double matchScore) {
		super();
		this.seeker = seeker;
		this.job = job;
		this.matchScore = matchScore;
	}
}

