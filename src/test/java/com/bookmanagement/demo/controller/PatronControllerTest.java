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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.bookmanagement.demo.models.Book;
import com.bookmanagement.demo.models.Patron;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@WithUserDetails("admin@library.org")
class PatronControllerTest {

	@Autowired
	private  MockMvc mockMvc ;
	

	
	@Test
	@Order(1)
	void testAddAndRetrivePatron() throws JsonProcessingException, Exception { 
	   	
		
		Patron patron = new  Patron(null, "amr", "mahmoud", "amr.mahmoud10@gmail.com", "01283612465") ; 
		
	    Patron result =   createNewPatron(patron);  
	    
	   assertTrue(result.getId() > 0) ; 
	   assertEquals(result.getFirstName(), patron.getFirstName()) ; 
	   assertEquals(result.getLastName(), patron.getLastName()) ; 
	   assertEquals(result.getEmail(), patron.getEmail()) ; 
	   assertEquals(result.getPhone(), patron.getPhone()) ; 
	   
	}

	@Test
	@Order(2)
	void testAddPatronViolatCommonValidations() throws JsonProcessingException, Exception { 

		Patron patron1 = new Patron(null, "amr", "mahmoud", "amr.mahmoud@gmail.com","01283612405") ; 
		
		Patron patron = new Patron(null, "amr", "mahmoud", "amr.mahmoud@gmail.com","01283612405") ; 
		
		tryAddPatron(patron).andExpect(status().isCreated()) ; 
		// set required fields to null in order to test response   
		
		patron.setLastName("mahmoud"); 
		patron.setEmail("amr.mahoud2@gmail.com"); 
		patron.setPhone("01583612465");
		
		////////////// check first name validation ///////////
		patron.setFirstName(null); 
		tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		patron.setFirstName("amr mahmoud") ;   // invalid ! contains space character
		tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setFirstName("Amr12");  // invalid ! contains non alphabetic characters
		tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setFirstName(" "); // invalid ! black value 
		tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setFirstName("aaaaaaaaaaaaaaaaaaaaaaaaaa"); // 26 character which exceed maximum length 
		tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		

		patron.setFirstName("Amr"); // set first name to a valid value 
		
		
		///////////////// check last name validation //////////////////
		patron.setLastName(null);
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		patron.setLastName("amr mahmoud") ;   // invalid ! contains space character
		tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setLastName("Amr12");  // invalid ! contains non alphabetic characters
		tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setLastName(" "); // invalid ! black value 
		tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setLastName("aaaaaaaaaaaaaaaaaaaaaaaaaa"); // 26 character which exceed maximum length 
		tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		

		patron.setLastName("Mahmoud"); // set last name to a valid value

	  
		///////////////// check email validation //////////////////
        patron.setEmail(null);
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		

        patron.setEmail("    ");  // invalid email ! blank value
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
        patron.setEmail("Amr  Mahmoud@gmail.com");  // invalid email format
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
	 
        patron.setEmail("amr.mahmoud.com"); // invalid email format
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
        
        patron.setEmail(patron1.getEmail()); // invalid because it already exists ; 
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	

        patron.setEmail("amr.mahmoud3@gmail.com"); // set a valid email 
        
        
        ///////////////// check last name validation //////////////////
        patron.setPhone(null);
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		

        patron.setPhone("    ");  // invalid email ! blank value
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
        patron.setPhone("01283a12465");  // invalid phone format , contains alpha character 
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
	 	
        patron.setPhone("128312465");  // invalid phone format , do`t start with 0  
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
	 	
        patron.setPhone("0128361246"); // invalid phone format , 10 digits 
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
	 	
        patron.setPhone(patron1.getPhone()); // invalid phone number , already exists
        tryAddPatron(patron).andExpect(status().isUnprocessableEntity()) ;	
        
        patron.setPhone("01183612465"); // set a valid phone 
        tryAddPatron(patron).andExpect(status().isCreated()) ; 
        
	 	
        
	
	}

	@Test
	@Order(3)
	void testRetrivePatronViolatValidation() throws Exception { 
		// try get book with wrong reference
		tryGetPatron("/api/patrons/3000")  
	   .andExpect(status().isBadRequest()) ; 	 
	}

	
	
	@Test
	@Order(4)
	void testRetriveAllPatrons() throws Exception {
		
		Patron patron = new Patron(null, "firstName", "lastName", "test.email@email.com", "01583612465") ;  
		
		// add patron 
				  tryAddPatron(patron)
				  .andExpect(status().isCreated()) ; 
		 

		 patron = new Patron(null, "firstName", "lastName", "test.email1@email.com", "01683612465") ;  
					
					// add patron 
			   tryAddPatron(patron)
				.andExpect(status().isCreated()) ; 
					
		
		ResultActions result = mockMvc.perform(get("/api/patrons")
				.accept(MediaType.APPLICATION_JSON)
				) ; 
		
		result.andExpect(status().isOk())
		.andExpect(jsonPath("$.patrons").exists()) 
		.andExpect(jsonPath("$.patrons").isArray())
		.andExpect(jsonPath("$.patrons[*].id").isNotEmpty());
		     	
	}
	
	
 
	@Test
	@Order(5)
	void testUpdatePatron() throws JsonProcessingException, Exception { 
		
		Patron patron = createNewPatron(new Patron(null, "firstName", "lastName", "fristName.lastName@gmail.com", "01983612465")) ; 
		 
		String newFirstName = "newFirstName" ; 
		String newLastName = "newLastName" ;  
		String newEmail = "newEmail@email.com" ; 
		String newPhone = "02183612465" ; 
		
		patron.setFirstName(newFirstName); 
		patron.setLastName(newLastName); 
		patron.setEmail(newEmail); 
		patron.setPhone(newPhone); 
		
		
		tryUpdatePatron(patron) 
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(patron.getId())) 
		.andExpect(jsonPath("$.firstName").value(newFirstName)) 
		.andExpect(jsonPath("$.lastName").value(newLastName)) 
		.andExpect(jsonPath("$.email").value(newEmail)) 
		.andExpect(jsonPath("$.phone").value(newPhone)) ; 
		
	}
	
	

	@Test
	@Order(6)
	void testUpdateBookViolateCommonValidation() throws JsonProcessingException, Exception { 
		
		
		// to create new unique email  and phone 
		Patron patron1 =  createNewPatron(new Patron(null, "firstName", "lastName", "amrmahmoud3@gmail.com" ,"01283613465"))  ; 
		 
		 
		Patron patron = createNewPatron(new Patron(null, "firstName", "lastName", "amrmahmoud4@gmail.com", "01283615465")) ; 
		
		////////////// check first name validation ///////////
		patron.setFirstName(null); 
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		patron.setFirstName("amr mahmoud") ;   // invalid ! contains space character
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setFirstName("Amr12");  // invalid ! contains non alphabetic characters
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setFirstName(" "); // invalid ! black value 
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setFirstName("aaaaaaaaaaaaaaaaaaaaaaaaaa"); // 26 character which exceed maximum length 
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		

		patron.setFirstName("Amr"); // set first name to a valid value 
		
		
		///////////////// check last name validation //////////////////
		patron.setLastName(null);
        tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		patron.setLastName("amr mahmoud") ;   // invalid ! contains space character
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setLastName("Amr12");  // invalid ! contains non alphabetic characters
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setLastName(" "); // invalid ! black value 
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		
		patron.setLastName("aaaaaaaaaaaaaaaaaaaaaaaaaa"); // 26 character which exceed maximum length 
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ; 
		

		patron.setLastName("Mahmoud"); // set last name to a valid value

		
		///////////////// check email validation //////////////////
		patron.setEmail(null);
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		
		patron.setEmail("    ");  // invalid email ! blank value
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		patron.setEmail("Amr  Mahmoud@gmail.com");  // invalid email format
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		patron.setEmail("amr.mahmoud.com"); // invalid email format
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		patron.setEmail(patron1.getEmail()); // invalid because it already exists ; 
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		patron.setEmail("amr.mahmoud30@gmail.com"); // set a valid email 
		
		
		///////////////// check phone  validation //////////////////
		patron.setPhone(null);
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		
		patron.setPhone("    ");  // invalid email ! blank value
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		patron.setPhone("01283a12465");  // invalid phone format , contains alpha character 
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
			
		patron.setPhone("128312465");  // invalid phone format , do`t start with 0  
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
			
		patron.setPhone("0128361246"); // invalid phone format , 10 digits 
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
			
		patron.setPhone(patron1.getPhone()); // invalid phone number , already exists
		tryUpdatePatron(patron).andExpect(status().isUnprocessableEntity()) ;	
		
		patron.setPhone("02083612465"); // set a valid phone 
		tryUpdatePatron(patron).andExpect(status().isOk()) ; 
		

 
	}
	
	
	
	@Test
	@Order(7)
	void testDeletePatron() throws JsonProcessingException, Exception { 
		 
			Patron patron = createNewPatron(new Patron(null, "firstName", "lastName","amr.mahmoud20@gmail.com" ,  "01026729362")) ; 
		
			tryDeletePatron(patron.getId())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(patron.getId()));  
			
			tryGetPatron("/api/patrons/" + String.valueOf(patron.getId()))  
			   .andExpect(status().isBadRequest()) ; 
	
			// try to delete patron that is not exists
			tryDeletePatron(patron.getId()) 
			.andExpect(status().isBadRequest())  ;
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

	
	private ResultActions tryDeletePatron(int patron) throws Exception { 
		return mockMvc
				.perform(delete("/api/patrons/" + String.valueOf(patron))
							.contentType(MediaType.APPLICATION_JSON)
						) ;
				
				
	}
	private ResultActions tryUpdatePatron(Patron patron) throws JsonProcessingException, Exception { 

		ObjectMapper mapper = new ObjectMapper() ; 
		return mockMvc.perform(put("/api/patrons")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(patron))) ;
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
