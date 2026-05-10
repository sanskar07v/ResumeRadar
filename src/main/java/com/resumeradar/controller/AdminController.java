package com.resumeradar.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumeradar.entity.User;
import com.resumeradar.model.ApiResponse;
import com.resumeradar.model.RegisterModel;
import com.resumeradar.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passEncoder;
	
	@GetMapping("/get/role/{role}")
	@Cacheable(value = "usersByRole", key = "#role")
	public ResponseEntity<ApiResponse> getUserByRole(@PathVariable String role){
		log.info("Get user by role : {}" , role);
		List<User> users = userService.getUserByRole(role);
		if (users==null || users.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, null, "Role is not found or has no User"));
		}
		log.info("List of user received");
		return ResponseEntity.ok(new ApiResponse(true, users, "List of User send"));
	}
	
	@GetMapping("/activate/{id}")
	public ResponseEntity<ApiResponse> activateUser(@PathVariable String id) {
		Optional<User> op = userService.getUserById(id);
		if (op.isPresent()) {
			User updatedUser = userService.activate(op.get());
			log.info("User {} is Activated."  , id);
			return ResponseEntity.ok(new ApiResponse(true , updatedUser , "User activated"));
		}
		else {
			log.warn("User {} is not found" , id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, null, "User not found"));
		}
	}
	
	@PostMapping("/add/admin")
	public ResponseEntity<ApiResponse> addAdmin(@Valid @RequestBody RegisterModel model) throws MessagingException{
		model.setPassword(passEncoder.encode(model.getPassword()));
		User user = userService.saveUser(model);
		if(user!=null) {
			log.info("New admin is created");
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, user, "User Added Successfully"));
		}
		else {
			log.warn("There is error is creating the new admin");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false , null , "User is not able to add"));
		}
	}
	
}
