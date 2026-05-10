package com.resumeradar.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpModel {
	@NotNull(message = "OTP cann't be null")
	private Integer otp;
}
