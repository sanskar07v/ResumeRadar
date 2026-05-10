package com.resumeradar.model;

import com.resumeradar.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse 
{
	private String name;
	private Role role;
	private String token;
}
