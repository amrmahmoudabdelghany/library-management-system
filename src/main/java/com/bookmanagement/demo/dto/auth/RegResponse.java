package com.bookmanagement.demo.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class RegResponse {
 
	private final String REQUEST = "REGISTRATION_REQUEST" ;  
	private Integer id ; 
	private String token ;
	
	
}
