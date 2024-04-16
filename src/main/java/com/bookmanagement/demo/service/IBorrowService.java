package com.bookmanagement.demo.service;

import com.bookmanagement.demo.dto.BorrowRequest;
import com.bookmanagement.demo.dto.BorrowResponse;
import com.bookmanagement.demo.dto.ReturnRequest;
import com.bookmanagement.demo.dto.ReturnResponse;

public interface IBorrowService {

	
	BorrowResponse borrow(BorrowRequest request) ; 
	ReturnResponse returnBook(ReturnRequest request) ; 
	
}
