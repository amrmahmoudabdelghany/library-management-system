package com.bookmanagement.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.bookmanagement.demo.models.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@AutoConfigureMockMvc 
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
@TestPropertySource(locations = "classpath:application.properties")
@WithUserDetails("admin@library.org")
class BookControllerTest {

	@Autowired
	private  MockMvc mockMvc ;
	

	
	@Test
	@Order(1)
	void testAddAndRetriveBook() throws JsonProcessingException, Exception { 
	   	
		
		Book book = new Book(null, "test-title", "test-author", "2001", "0-306-40615-5" , false) ; 
	    Book resultBook =   createNewBook(book);  
	    
	   assertTrue(resultBook.getId() > 0) ; 
	   assertEquals(resultBook.getTitle(), book.getTitle()) ; 
	   assertEquals(resultBook.getAuthor(), book.getAuthor()) ; 
	   assertEquals(resultBook.getISBN(), book.getISBN()) ; 
	   assertEquals(resultBook.getPublicationYear(), book.getPublicationYear()) ; 
	   
 	  //System.out.println("Result Book is : " + resultBook) ; 
	  //assertTrue(resultBook.getId() > 0) ;
	  
	}

	@Test
	@Order(2)
	void testAddBookViolatCommonValidations() throws JsonProcessingException, Exception { 

		Book book = new Book(null, "test-title", "test-author", "2020","0-306-40615-6" , false) ; 
		
		tryAddBook(book).andExpect(status().isCreated()) ; 
		// set required fields to null in order to test response   
		// publication year is not required
		book.setTitle(null); 
		book.setAuthor(null); 
		book.setISBN(null); 
		
		tryAddBook(book).andExpect(status().isUnprocessableEntity()) ;	
		book.setTitle("test-title") ; 
		tryAddBook(book).andExpect(status().isUnprocessableEntity()) ;
		book.setAuthor("test-author") ; 
		tryAddBook(book).andExpect(status().isUnprocessableEntity()) ;
		book.setISBN("0-306-40615-abc");   
		tryAddBook(book).andExpect(status().isUnprocessableEntity()) ; // try add book with invalid isbn format 
		book.setISBN("0-306-40615-6");
		tryAddBook(book).andExpect(status().isUnprocessableEntity()) ; // try add book with existence isbn 
		book.setPublicationYear("200"); 
		tryAddBook(book).andExpect(status().isUnprocessableEntity()) ; //try add book with invalid publication year format

	}

	@Test
	@Order(3)
	void testRetriveBookViolatValidation() throws Exception { 
		// try get book with wrong reference
		tryGetBook("/api/books/3000")  
	   .andExpect(status().isBadRequest()) ; 	 
	}

	
	
	@Test
	@Order(4)
	void testRetriveAllBooks() throws Exception {
		
		Book book = new Book(null, "test-title", "test-author", "2001", "0-306-40614-7" , false) ;  
		
		// add book 
				  tryAddBook(book)
				  .andExpect(status().isCreated()) ; 
		  
		  
		
		ResultActions result = mockMvc.perform(get("/api/books")
				.accept(MediaType.APPLICATION_JSON)
				) ; 
		
		result.andExpect(status().isOk())
		.andExpect(jsonPath("$.books").exists()) 
		.andExpect(jsonPath("$.books").isArray())
		.andExpect(jsonPath("$.books[*].id").isNotEmpty());
		     	
	}
	
	
 
	@Test
	@Order(5)
	void testUpdateBook() throws JsonProcessingException, Exception { 
		
		Book book = createNewBook(new Book(null, "new-book-title", "new-book-author", "2020", "0-305-40615-8" , false)) ; 
		 
		String newTitle = "updated-book-ttitle" ; 
		String newAuthor = "updated-book-author" ;  
		String newPubYear = "2021" ; 
		String newISBN = "0-306-40615-7" ; 
		
		book.setTitle(newTitle); 
		book.setAuthor(newAuthor); 
		book.setPublicationYear(newPubYear); 
		book.setISBN(newISBN); 
		
		
		tryUpdateBook(book) 
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(book.getId())) 
		.andExpect(jsonPath("$.title").value(newTitle)) 
		.andExpect(jsonPath("$.author").value(newAuthor)) 
		.andExpect(jsonPath("$.publicationYear").value(newPubYear)) 
		.andExpect(jsonPath("$.isbn").value(newISBN)) ; 
		
	}
	
	

	@Test
	@Order(6)
	void testUpdateBookViolateCommonValidation() throws JsonProcessingException, Exception { 
		
	

		// to create new unique isbn 
		 createNewBook(new Book(null, "new-book-title", "new-book-author", "2020", "0-315-40615-1" , false))  ; 
		 
		 
		Book book = createNewBook(new Book(null, "new-book-title", "new-book-author", "2020", "0-315-40625-8" , false)) ; 
		 
		// set required fields to null in order to test response   
		// publication year is not required
		book.setTitle(null); 
		book.setAuthor(null); 
		book.setISBN(null); 
		
		tryUpdateBook(book).andExpect(status().isUnprocessableEntity()) ;	
		book.setTitle("test-title") ; 
		tryUpdateBook(book).andExpect(status().isUnprocessableEntity()) ;
		book.setAuthor("test-author") ; 
		tryUpdateBook(book).andExpect(status().isUnprocessableEntity()) ;
		book.setISBN("0-306-40615-abc");   
		tryUpdateBook(book).andExpect(status().isUnprocessableEntity()) ; // try add book with invalid isbn format 
		book.setISBN("0-315-40615-1") ;
		tryUpdateBook(book).andExpect(status().isUnprocessableEntity()) ; // try add book with existence isbn 
		book.setPublicationYear("200"); 
		tryUpdateBook(book).andExpect(status().isUnprocessableEntity()) ; //try add book with invalid publication year format

	} 
	
	@Test
	@Order(7)
	void testDeleteBook() throws JsonProcessingException, Exception { 
		 
			Book book = createNewBook(new Book(null, "new-book-title", "new-book-author", "2020", "0-315-40615-8" , false)) ; 
		
			tryDeleteBook(book.getId())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(book.getId()));  
			
			tryGetBook("/api/books/" + String.valueOf(book.getId()))  
			   .andExpect(status().isBadRequest()) ; 
			
	}
	
	
	@Test
	@Order(8)
	void testDeleteBookViolateValidation() throws Exception { 
		tryDeleteBook(3000) 
		.andExpect(status().isBadRequest())  ; 
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

	
	private ResultActions tryDeleteBook(int book) throws Exception { 
		return mockMvc
				.perform(delete("/api/books/" + String.valueOf(book))
							.contentType(MediaType.APPLICATION_JSON)
						) ;
				
				
	}
	private ResultActions tryUpdateBook(Book book) throws JsonProcessingException, Exception { 

		ObjectMapper mapper = new ObjectMapper() ; 
		return mockMvc.perform(put("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(book))) ;
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
	
	

	
	
	

}
