package com.bookmanagement.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmanagement.demo.dto.auth.LoginRequest;
import com.bookmanagement.demo.dto.auth.LoginResponse;
import com.bookmanagement.demo.dto.auth.RegRequest;
import com.bookmanagement.demo.dto.auth.RegResponse;
import com.bookmanagement.demo.service.IAuthService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

	private IAuthService authService ;
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<RegResponse> register(@RequestBody RegRequest request) { 
		return ResponseEntity.ok(this.authService.register(request));
	} 
	
	@GetMapping
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) { 
		return ResponseEntity.ok(this.authService.login(request)) ; 
	}
}
