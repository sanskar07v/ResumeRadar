package com.resumeradar.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OtpToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String otpId;
	
	@OneToOne
	@JoinColumn(name = "user" , unique=true , nullable=false)
	private User user;
	
	private Integer otp;
	
	private LocalDateTime expiry;
	
	private Boolean isUsed;
	
	private Integer attempt;
	
	public OtpToken(User user) {
		this.user =user;
		this.generateOtp();
	}
	
	public void generateOtp() {
		int min = 100000;
		int max = 999999;
		int otp = (int)(Math.random() * (max - min + 1)) + min;
		this.otp = otp;
		this.expiry = LocalDateTime.now().plusSeconds(120);
		this.isUsed = false;
		this.attempt = 0;
	}
}
