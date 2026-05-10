package com.resumeradar.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumeradar.entity.User;
import com.resumeradar.model.ApiResponse;
import com.resumeradar.model.OtpModel;
import com.resumeradar.model.PasswordUpdateRequest;
import com.resumeradar.model.UpdateUserModel;
import com.resumeradar.service.UserService;
import com.resumeradar.utils.TokenBlacklistService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class CommonController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passEncoder;

	@Autowired
    private TokenBlacklistService tokenBlacklistService;

	@GetMapping("/get/{userId}")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable String userId){
		Optional<User> op = userService.getUserById(userId);	
		if(op.isPresent()) {
			log.info("User is get with id {}" , userId);
			return ResponseEntity.ok(new ApiResponse(true, op.get(), "User Found"));
		}
		log.warn("No user is found with id {}" , userId);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, op.get(), "User not found"));
	}
	
	@PostMapping("/updatepassword")
	public ResponseEntity<ApiResponse> updatePassword(@Valid @RequestBody PasswordUpdateRequest model) throws MessagingException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !(auth.getPrincipal() instanceof User user)) {
			log.warn("Unauthorized Access");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ApiResponse(false, null, "Unauthorized access"));
		}

		if (!passEncoder.matches(model.getOldPassword(), user.getPassword())) {
			log.warn("Old password doesn't match");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiResponse(false, null, "Old password doesn't match"));
		}

		String encodedPass = passEncoder.encode(model.getNewPassword());
		User userUpdated = userService.updatePassword(user, encodedPass);

		if (userUpdated != null) {
			log.info("Password updated");
			return ResponseEntity.ok(new ApiResponse(true, userUpdated, "Password updated successfully"));
		}
		log.warn("Password could not be updated");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ApiResponse(false, null, "Password could not be updated"));
	}

	
	@GetMapping("/deactivate")
	public ResponseEntity<ApiResponse>  deactivateYourself(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User user) {
			User updatedUser = userService.deactivate(user);
			log.info("User is deactivated");
			return ResponseEntity.ok(new ApiResponse(true , updatedUser , "User deactivated"));
		}
		else {
			log.warn("User is not able to deactivate");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, null, "Login Again"));
		}
	}
	
	@PostMapping("/updateUser")
	public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserModel model) {
		User user = userService.updateUser(model);
		if(user!=null) {
			log.info("User is pdated successfully");
			return ResponseEntity.ok(new ApiResponse(true, user, "User is updated successfully!!"));
		}
		log.warn("User is not updated");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, null, "User is updated!!"));
		
	}
	
	@PostMapping("/forgetpassword")
	public ResponseEntity<ApiResponse> forgetPassword() {
			try {
				userService.forgetPassword();
				log.info("Otp is send");
				return ResponseEntity.ok(new ApiResponse(true, null, "otp is sent."));
			} catch (MessagingException e) {
		
				log.warn(e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(false, null, "Password reset but failed to send email"));
			}
	}

	@PostMapping("/check/otp")
	public ResponseEntity<ApiResponse> checkOtp(@RequestBody @Valid OtpModel otp){
		Boolean isValid = userService.verifyOtp(otp);
		if(isValid) {
			return ResponseEntity.ok(new ApiResponse(true, null, "Otp is Successfully verified"));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false , null , "Otp is not correct"));
	}
	
	
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
            log.info("Token is added to Black List");
            return ResponseEntity.ok(new ApiResponse(true, null, "Logged out successfully"));
        }
        log.warn("Token not found");
        return ResponseEntity.badRequest().body(new ApiResponse(false, null, "Token not found"));
    }
}
