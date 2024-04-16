package com.bookmanagement.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor 
@ToString
public class ReturnRequest {

	private Integer bookId ; 
	private Integer patronId ; 
	
	
}
