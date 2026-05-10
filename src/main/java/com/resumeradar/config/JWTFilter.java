package com.resumeradar.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.resumeradar.entity.User;
import com.resumeradar.service.UserService;
import com.resumeradar.utils.TokenBlacklistService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter 
{
	@Autowired
	private JWTUtils jwtutils;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenBlacklistService tokenBlacklistService;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException 
	{
		String url = request.getRequestURI();
		log.info("Requested URL : " + url);
		return url.contains("/auth") || 
				url.contains("/swagger-ui") ||
				url.contains("/v3/api-docs") || 
				url.contains("/apidocs.html") ||
				url.contains("/login/oauth2/code/google");
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

	    final String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.getWriter().write("Token not found or malformed.");
	        return;
	    }

	    final String jwt = authHeader.substring(7);

	    try {
	    	
	    	if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
	    		log.error("Token is blacklisted (user is logged out)");
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("Token is blacklisted (user is logged out).");
	            return;
	        }
	    	
	        if (jwtutils.isTokenExpired(jwt)) {
	        	log.error("Token has expired. Please login again.");
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("Token has expired. Please login again.");
	            return;
	        }

	        if (!jwtutils.validateToken(jwt)) {
	        	log.error("Token validation failed. Possibly tampered.");
	            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	            response.getWriter().write("Token validation failed. Possibly tampered.");
	            return;
	        }

	        String userId = jwtutils.extractUserID(jwt);
	        Optional<User> optionalUser = userService.getUserById(userId);
	        if (optionalUser.isPresent()) {
	            User user = optionalUser.get();
	            UsernamePasswordAuthenticationToken authToken =
	                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	            SecurityContextHolder.getContext().setAuthentication(authToken);
	            log.info("User is successfully logged in.");
	            filterChain.doFilter(request, response);
	        } else {
	            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	            response.getWriter().write("Invalid user associated with token.");
	        }
	    } catch (ExpiredJwtException e) {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.getWriter().write("Token has expired. Please login again.");
	    } catch (SecurityException | JwtException e) {
	        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	        response.getWriter().write("Invalid token: " + e.getMessage());
	    }
	}

}
