package com.resumeradar.model;

import com.resumeradar.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterModel {
	
	@NotBlank(message = "Email id can not be blank")
	@Email(message ="Email is not in proper format")
	private String emailId;
	
	@NotBlank(message = "Password Can not be blank")
	@Pattern(
		    regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
		    message = "Password must be at least 8 characters and include a digit, lowercase, uppercase, and special character"
		)
	private String password;
	
	@NotNull(message = "Role can not be null")
	private Role role;
	
	@NotBlank(message = "Name can not be blank")
	private String name;
	
	private String education;
	
	@NotBlank(message = "Phone Number can not be blanked")
	private String phone;
		
}
