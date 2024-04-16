package com.bookmanagement.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@AllArgsConstructor
@NoArgsConstructor 
@ToString
public class BorrowRequest {

	private Integer bookId ; 
	private Integer patronId ; 
	
}
