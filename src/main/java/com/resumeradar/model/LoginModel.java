package com.resumeradar.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel {

	@NotBlank(message = "Email Id cann't be blank")
	@Email(message = "Email Id is not in correct format")
	private String emailId;
	
	@NotBlank
	private String password;
}
