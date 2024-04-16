package com.bookmanagement.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor 
@ToString
public class BorrowResponse {

	private Integer id ; 
	
	private Integer patronId;
	private Integer bookId;

	private String patronName;

	private String bookTitle;
}