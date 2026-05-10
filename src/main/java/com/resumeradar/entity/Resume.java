package com.resumeradar.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String resumeId;

    @Column(name="resume_path"  , nullable=false)
    private String resumePath;
    
    @Lob
    @Column(name="skills" , nullable=false)
    private String skills;

    @UpdateTimestamp
    private LocalDateTime uploadedAt;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id" , nullable=false)
    private User user;

	public Resume(String resumePath , String skills, User user) {
		super();
		this.resumePath = resumePath;
		this.skills = skills;
		this.user = user;
	}
    
    
}

