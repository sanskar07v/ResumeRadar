package com.resumeradar.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateRequest {
	
	@NotBlank(message = "Old Password cann't be blank")
	private String oldPassword;
	
	@NotBlank(message = "New Password Cann't be blank")
	@Pattern(
		    regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
		    message = "Password must be at least 8 characters and include a digit, lowercase, uppercase, and special character"
		)
	private String newPassword;
	
}
