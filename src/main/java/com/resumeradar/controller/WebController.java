package com.resumeradar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumeradar.config.JWTUtils;
import com.resumeradar.entity.User;
import com.resumeradar.model.ApiResponse;
import com.resumeradar.model.LoginModel;
import com.resumeradar.model.LoginResponse;
import com.resumeradar.model.RegisterModel;
import com.resumeradar.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class WebController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passEncoder;
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginModel model){
		
		User user =  userService.login(model);
		
		if(user==null) {
			ApiResponse res = new ApiResponse(false, null, "Email Id is Wrong!!");
			return ResponseEntity.ok(res);
		}
		if(user.getPassword()==null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, null, "Password is not set"));
		}
		boolean flag = passEncoder.matches(model.getPassword(), user.getPassword());
		if(flag) {
			String token = jwtUtils.generateToken(user.getUserId());
			LoginResponse lr = new LoginResponse(user.getName(), user.getRole(), token);
			return ResponseEntity.ok(new ApiResponse(true, lr, "User Login Successfully"));
		}
		
		return ResponseEntity.ok(new ApiResponse(false, null, "Email and Password doesn't match"));
	}
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterModel model) throws MessagingException{
		model.setPassword(passEncoder.encode(model.getPassword()));
		User user = userService.saveUser(model);
		if(user!=null) {
			return ResponseEntity.ok(new ApiResponse(true, user, "User Added Successfully"));
		}
		else {
			return ResponseEntity.ok(new ApiResponse(false , null , "User is not able to add"));
		}
	}

}
