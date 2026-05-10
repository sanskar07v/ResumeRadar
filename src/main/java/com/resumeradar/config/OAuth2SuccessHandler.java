package com.resumeradar.config;

import com.resumeradar.entity.Role;
import com.resumeradar.entity.User;
import com.resumeradar.repo.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepo userRepository;
    private final JWTUtils jwtUtils;

    public OAuth2SuccessHandler(UserRepo userRepository, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String email = token.getPrincipal().getAttribute("email");
        String name = token.getPrincipal().getAttribute("name");

        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isEmpty()) {
            // Auto-register user
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setRole(Role.ROLE_JOB_SEEKER); // default
            user.setIsActive(true);
            user = userRepository.save(user);
        } else {
            user = existingUser.get();
        }

        // Generate JWT
        String jwtToken = jwtUtils.generateToken(user.getUserId());

        // Optionally save token in DB or Redis
        // tokenRepository.save(new Token(user, jwtToken));

        // Return token to client
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + jwtToken + "\"}");
    }
}
