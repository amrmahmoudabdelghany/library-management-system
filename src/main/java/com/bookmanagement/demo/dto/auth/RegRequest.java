package com.bookmanagement.demo.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder 
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegRequest {

	private String firstName ; 
	private String lastName ; 
	private String email ; 
	private String password ; 
	private String role ; 
	
	
}
