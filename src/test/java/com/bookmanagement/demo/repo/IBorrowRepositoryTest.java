package com.bookmanagement.demo.repo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bookmanagement.demo.models.Book;
import com.bookmanagement.demo.models.Borrow;
import com.bookmanagement.demo.models.Patron;


@DataJpaTest
class IBorrowRepositoryTest {

	
	@Autowired
	private IBorrowRepository borrowRepo ;  
	
	@Autowired
	private IBookRepository bookRepo ; 
	
	@Autowired
	private IPatronRepository patronRepo ; 
	
	
	
	@Test
	void test() { 
		Book book = new Book() ; 
		book.setAuthor("book-author"); 
		book.setTitle("book-title"); 
		book.setISBN("1122-22221-222"); 
		book.setPublicationYear("2020");
		book.setIsBorrowed(false);
		
		book = this.bookRepo.save(book) ; 
		
		assertTrue(book.getId() > 0); 
		
		Patron patron = new Patron() ; 
		patron.setEmail("amr.mahmoud.jpa.testcsae@gmail.com");
		patron.setFirstName("amr"); 
		patron.setLastName("mahmoud"); 
		patron.setPhone("01234567891"); 
		
		patron = this.patronRepo.save(patron) ; 
		
		assertTrue(patron.getId() > 0); 
		
		
		Borrow borrow = new Borrow() ; 
		
		borrow.setBook(book)  ;
		borrow.setPatron(patron) ; 
		borrow.setBorrowDate(LocalDate.now()) ;
		borrow.setReturnDate(null) ;
		
		
 		borrow =  this.borrowRepo.save(borrow) ;  
 		
 		assertTrue(borrow.getId() > 0 ) ; 
 		
 		Borrow association =  this.borrowRepo.findByPatronIdAndBookId(patron.getId(), book.getId()).get() ; 
 		
 		assertTrue(borrow.getId() == association.getId());
 		
	}

}
