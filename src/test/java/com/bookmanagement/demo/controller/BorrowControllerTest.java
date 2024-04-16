package com.bookmanagement.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.bookmanagement.demo.models.Book;
import com.bookmanagement.demo.models.Borrow;
import com.bookmanagement.demo.models.Patron;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@WithUserDetails("admin@library.org")
class BorrowControllerTest {


	@Autowired
	private  MockMvc mockMvc ;
	
	
	@Test
	void testBorrowBook() throws JsonProcessingException, Exception {
	
		Patron patron = new Patron(null, "amr", "mahmoud", "borrow.test@gmail.com", "01234567890") ; 
		Book book = new Book(null, "book-title", "book-author", "2020", "0-306-40615-5" , false) ; 
		
		book = createNewBook(book) ; 
		patron = createNewPatron(patron) ; 
	
		tryBorrowBook(patron, book) 
		.andExpect(status().isOk()) 
		.andExpect(jsonPath("$.id").exists()) 
		.andExpect(jsonPath("$.patronId").value(patron.getId())) 
		.andExpect(jsonPath("$.patronName").value(patron.getFullName())) 
		.andExpect(jsonPath("$.bookId").value(book.getId())) 
		.andExpect(jsonPath("$.bookTitle").value(book.getTitle())) ; 
			
	} 
	
	@Test
	void testBorrowViolateValidation() throws JsonProcessingException, Exception { 
	
		Patron patron1 = new Patron(null, "amr", "mahmoud", "borrow.test1@gmail.com", "01234567890") ; 
		Patron patron2 = new Patron(null, "ahmed", "mahmoud", "borrow.test2@gmail.com", "02234567890") ; 
		
		Book book = new Book(null, "book-title", "book-author", "2020", "0-306-40615-5" , false) ; 
		
		book = createNewBook(book) ; 
		patron1 = createNewPatron(patron1) ; 
	    patron2 = createNewPatron(patron2) ;
	    
	    
		
		// invalid book reference 

	    tryBorrowBook(patron1.getId() , 100)
	    .andExpect(status().isBadRequest())  ; 
	    

		// invalid patron reference 
		
	    tryBorrowBook(100 , book.getId())
	    .andExpect(status().isBadRequest())  ; 
	    
	    
		// book already borrowed 
		
	    // borrow book by patron 1
	    tryBorrowBook(patron1.getId()  ,book.getId())  
	    .andExpect(status().isOk()) ;
	    
		// try borrow the same book by the same patron  
	    
	    tryBorrowBook(patron1.getId()  ,book.getId())  
	    .andExpect(status().isUnprocessableEntity()) ;
	    
	    // try borrow the same book by other patron 
	    
	    tryBorrowBook(patron2.getId()  ,book.getId())  
	    .andExpect(status().isUnprocessableEntity()) ;
	    
	    
	} 
	
	@Test
	public void testRetrunBook() throws Exception { 

		Patron patron = new Patron(null, "amr", "mahmoud", "borrow.test@gmail.com", "01234567890") ; 
		Book book = new Book(null, "book-title", "book-author", "2020", "0-306-40615-5" , false) ; 
		
		book = createNewBook(book) ; 
		patron = createNewPatron(patron) ; 
	
		tryBorrowBook(patron, book) 
		.andExpect(status().isOk())  ;  
		
		tryReturnBook(patron, book) 
		.andExpect(status().isOk()) 
		.andExpect(jsonPath("$.id").exists())  
		.andExpect(jsonPath("$.request").value("RETURN_BOOK"))
		.andExpect(jsonPath("$.patronId").value(patron.getId())) 
		.andExpect(jsonPath("$.patronName").value(patron.getFullName())) 
		.andExpect(jsonPath("$.bookId").value(book.getId())) 
		.andExpect(jsonPath("$.bookTitle").value(book.getTitle())) ; 
	}
	
	
	@Test
	public void testRetrunBookViolateValidation() throws JsonProcessingException, Exception { 
		

		Patron patron1 = new Patron(null, "amr", "mahmoud", "borrow.test1@gmail.com", "01234567890") ; 
		Patron patron2 = new Patron(null, "ahmed", "mahmoud", "borrow.test2@gmail.com", "02234567890") ; 
		
		Book book = new Book(null, "book-title", "book-author", "2020", "0-306-40615-5" , false) ; 
		
		book = createNewBook(book) ; 
		patron1 = createNewPatron(patron1) ; 
	    patron2 = createNewPatron(patron2) ;
	

		// invalid book reference 

	    tryReturnBook(patron1.getId() , 100)
	    .andExpect(status().isBadRequest())  ; 
	    

		// invalid patron reference 
		
	    tryReturnBook(100 , book.getId())
	    .andExpect(status().isBadRequest())  ; 

	    
	    // try return book that not been borrowed before 
	    tryReturnBook(patron1.getId(), book.getId()) 
	    .andExpect(status().isUnprocessableEntity()) ; 
	    
	    // patron 1 will borrow the book 
	    tryBorrowBook(patron1.getId(), book.getId()) 
	    .andExpect(status().isOk()) ; 
	    
	    // then patron 2 will try return it 
	    
	    tryReturnBook(patron2.getId(), book.getId()) 
	    .andExpect(status().isUnprocessableEntity()) ; 
	    
	    // try return book by patron borrow it 
	    tryReturnBook(patron1.getId(), book.getId()) 
	    .andExpect(status().isOk()) ; 
	    
	    
	    
	
	}
	
 
	private ResultActions tryReturnBook(Patron patron , Book book) throws Exception { 
		return tryReturnBook(patron.getId(), book.getId()) ;
	}
	
	private ResultActions tryReturnBook(int patronId , int bookId) throws Exception{ 
		return mockMvc.perform(
				   put("/api/borrows/" + bookId + "/patron/" + patronId ) 
				  .contentType(MediaType.APPLICATION_JSON)) ;		
	}
	
	private ResultActions tryBorrowBook(Patron patron , Book book) throws Exception { 
		return tryBorrowBook(patron.getId(), book.getId()) ;
	}
	
	private ResultActions tryBorrowBook(int patronId , int bookId) throws Exception {
	return mockMvc.perform(
			   post("/api/borrows/" + bookId + "/patron/" + patronId ) 
			  .contentType(MediaType.APPLICATION_JSON)) ;
}
	

	private Book createNewBook(Book book) throws JsonProcessingException, Exception {  
		ObjectMapper mapper = new ObjectMapper() ; 
		
		  MvcResult result = 	
				  tryAddBook(book)
				  .andExpect(status().isCreated())
			     .andReturn();
		  
		  String bookLocation = result.getResponse().getHeader("location") ; 
		  System.out.println("Location  " + bookLocation) ; 
		  
		  MvcResult bookResult = 
				  tryGetBook(bookLocation)
				  .andExpect(status().isOk())
				  .andReturn();
		  
		  Book resultBook =   mapper.readValue(bookResult.getResponse().getContentAsString(), Book.class) ;  
		
		  return resultBook ; 
	}



	private ResultActions tryAddBook(Book book) throws JsonProcessingException, Exception {  
		ObjectMapper mapper = new ObjectMapper() ; 
		
		return mockMvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(book))) ;
	}
	
	
	private ResultActions tryGetBook(String bookLocation) throws Exception {  
		return mockMvc.perform(
				  get(bookLocation) 
				  .contentType(MediaType.APPLICATION_JSON)) ;
	}

	private Patron createNewPatron(Patron patron) throws JsonProcessingException, Exception {  
		ObjectMapper mapper = new ObjectMapper() ; 
		
		  MvcResult result = 	
				  tryAddPatron(patron)
				  .andExpect(status().isCreated())
			     .andReturn();
		  
		  String location = result.getResponse().getHeader("location") ; 
		  
		  MvcResult patronResult = 
				  tryGetPatron(location)
				  .andExpect(status().isOk())
				  .andReturn();
		  
		  Patron resultPatron =   mapper.readValue(patronResult.getResponse().getContentAsString(), Patron.class) ;  
		
		  return resultPatron ; 
	}

	private ResultActions tryAddPatron(Patron patron) throws JsonProcessingException, Exception {  
		ObjectMapper mapper = new ObjectMapper() ; 
		
		return mockMvc.perform(post("/api/patrons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(patron))) ;
	}
	
	
	private ResultActions tryGetPatron(String location) throws Exception {  
		return mockMvc.perform(
				  get(location) 
				  .contentType(MediaType.APPLICATION_JSON)) ;
	}
	
	
}
