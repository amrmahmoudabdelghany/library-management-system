package com.bookmanagement.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class ReturnResponse {

	private Integer id ; 
	
	private final String REQUEST = "RETURN_BOOK" ; 
	
	private Integer patronId ; 
	private Integer bookId ;  
	
	private String patronName ; 
	private String bookTitle; 
	
	
}
