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

import com.bookmanagement.demo.BookManagementApiApplication;
import com.bookmanagement.demo.dto.auth.LoginRequest;
import com.bookmanagement.demo.dto.auth.LoginResponse;
import com.bookmanagement.demo.dto.auth.RegRequest;
import com.bookmanagement.demo.dto.auth.RegResponse;
import com.bookmanagement.demo.models.AppUserRole;
import com.bookmanagement.demo.models.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest(  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext( classMode = ClassMode.AFTER_EACH_TEST_METHOD )
class AuthControllerTest {


	@Autowired
	private  MockMvc mockMvc ;
	
	@Test
	void testRegisterNewUser() throws JsonProcessingException, Exception {
 
		
		// admin accounts only allowed to register new accounts 
		
		//login with a per-created admin account and get token 
		
		ObjectMapper mapper = new ObjectMapper() ; 
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) ; 
		
		LoginRequest loginRequest = LoginRequest.builder()
				.email("admin@library.org") 
				.password("123456") 
				.build() ; 
		
	 MvcResult loginResult  = 	tryLogin(loginRequest) 
		.andExpect(status().isOk()) 
		.andReturn();  
	  
	LoginResponse loginResponse =  mapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class) ;
	 	
	 String token = loginResponse.getToken() ; 
	 
	 
	// try use token to register a new account 
	 
		// add new account 
		RegRequest regRequest = RegRequest.builder()
				.email("amr.mahmoud@email.com")  
				.firstName("Amr")
				.lastName("Mahmoud") 
				.password("123456") 
				.role(AppUserRole.ROLE_LIBRARIAN.name()) 
				.build(); 
		
		tryRegUser(regRequest , token)  
		.andExpect(status().isOk()) 
		.andExpect(jsonPath("$.token").exists())
		.andExpect(jsonPath("$.id").exists()) ; 
		
		
		 
		// login with newly created account 
		
		 loginRequest = LoginRequest.builder()
				.email(regRequest.getEmail()) 
				.password(regRequest.getPassword()) 
				.build() ; 
		
	loginResult = 	tryLogin(loginRequest) 
		 .andExpect(status().isOk()) 
		 .andExpect(jsonPath("$.request").value("LOGGIN_REQUEST")) 
		 .andExpect(jsonPath("$.firstName").value(regRequest.getFirstName()))
		 .andExpect(jsonPath("$.lastName").value(regRequest.getLastName())) 
		 .andExpect(jsonPath("$.email").value(regRequest.getEmail())) 
		 .andExpect(jsonPath("$.token").isNotEmpty()) 
		 .andReturn() ;  
		
		
		// try register new account by not admin account 
		
	 String nonAdminAccountToken = mapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class).getToken()  ; 
	
	 regRequest = RegRequest.builder()
			 .email("newEmail@email.com") 
			 .firstName("firstName") 
			 .lastName("lastName") 
			 .password("123456") 
			 .role(AppUserRole.ROLE_LIBRARIAN.name()) 
			 .build() ; 
	   tryRegUser(regRequest, nonAdminAccountToken) 
	   .andExpect(status().isForbidden()) ; 
	   
	
	}
	
	@Test
	void testRegisterNewUserVaiolateValidation () throws JsonProcessingException, Exception {
	
		String adminToken = getAdminToken() ; 
		
		RegRequest regRequest = RegRequest.builder()
				.email("amr.mahmoud2@email.com")  
				.firstName("Amr")
				.lastName("Mahmoud") 
				.password("123456") 
				.role(AppUserRole.ROLE_LIBRARIAN.name()) 
				.build(); 
		
		tryRegUser(regRequest , adminToken)  
		.andExpect(status().isOk()) ; 
		
		// try register a new user with the same email 
		 regRequest = RegRequest.builder()
				.email("amr.mahmoud2@email.com")  
				.firstName("mohamed")
				.lastName("mahmoud") 
				.password("123456") 
				.role(AppUserRole.ROLE_LIBRARIAN.name()) 
				.build(); 
		
		tryRegUser(regRequest , adminToken) 
		.andExpect(status().isUnprocessableEntity()) ;  
		
		
		
		
		
		
	}
	
	private String getNonAdminToken() throws Exception { 
		return login("lib@library.org" , "123456").getToken() ;
	} 
	
	private String getAdminToken() throws JsonProcessingException, Exception { 
		
	 return login("admin@library.org" , "123456").getToken() ; 
	 
	}
	
	private LoginResponse login(String email , String password) throws Exception { 
		ObjectMapper mapper = new ObjectMapper() ; 
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) ; 
		
		LoginRequest loginRequest = LoginRequest.builder()
				.email(email) 
				.password(password) 
				.build() ; 
		
	 MvcResult loginResult  = 	tryLogin(loginRequest) 
		.andExpect(status().isOk()) 
		.andReturn();  
	  
	return  mapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class) ;
	 	
	}
	
	private ResultActions tryRegUser(RegRequest request , String token) throws JsonProcessingException, Exception { 
		ObjectMapper mapper = new ObjectMapper() ; 
		return mockMvc.perform(post("/api/auth")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request))) ;
		
	}


	
	private ResultActions tryLogin(LoginRequest request) throws JsonProcessingException, Exception { 
		ObjectMapper mapper = new ObjectMapper() ; 
		return mockMvc.perform(get("/api/auth")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request))) ;
		
	}

}
