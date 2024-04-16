package com.bookmanagement.demo.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

	private final String REQUEST = "LOGGIN_REQUEST" ; 
	private String firstName ; 
	private String lastName ; 
	private String email ;  
	private String token ; 
}
