package com.bookmanagement.demo.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class LoginRequest {

	private String email ; 
	private String password ; 
	
}
