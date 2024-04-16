package com.bookmanagement.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmanagement.demo.dto.BorrowRequest;
import com.bookmanagement.demo.dto.BorrowResponse;
import com.bookmanagement.demo.dto.ReturnRequest;
import com.bookmanagement.demo.dto.ReturnResponse;
import com.bookmanagement.demo.service.IBorrowService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/borrows")
@PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN')")
public class BorrowController {

	private final IBorrowService borrowService ; 
	
	
	

	@PostMapping("/{bookId}/patron/{patronId}")
	public ResponseEntity<BorrowResponse> borrowBook(@PathVariable Integer bookId ,
			@PathVariable Integer patronId) { 
		return ResponseEntity.ok(borrowService.borrow(new BorrowRequest(bookId, patronId))) ; 
	}
	
	@PutMapping("/{bookId}/patron/{patronId}")
	public ResponseEntity<ReturnResponse> returnBook(@PathVariable Integer bookId  , 
			@PathVariable Integer  patronId  ) { 
		return ResponseEntity.ok(borrowService.returnBook(new ReturnRequest(bookId, patronId))) ; 
	}
	
	
	
	
}
