package com.bookmanagement.demo;

import java.util.List;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.bookmanagement.demo.dto.auth.RegRequest;
import com.bookmanagement.demo.dto.auth.RegResponse;
import com.bookmanagement.demo.models.AppUserRole;
import com.bookmanagement.demo.models.Book;
import com.bookmanagement.demo.repo.IBookRepository;
import com.bookmanagement.demo.service.AuthService;
import com.bookmanagement.demo.service.IAuthService;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication 
@EnableWebSecurity
public class BookManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookManagementApiApplication.class, args);
	}
	
	@Bean
	CommandLineRunner initalDB(IAuthService authService) { 
		return (args)->{ 
	   RegResponse admin = 		authService.register(RegRequest.builder()
					.email("admin@library.org")  
					.firstName("Amr") 
					.lastName("Mahmoud") 
					.role(AppUserRole.ROLE_ADMIN.name())  
					.password("123456")
					.build()
					);  
	   RegResponse LIBRLIBRARIAN = 		authService.register(RegRequest.builder()
				.email("lib@library.org")  
				.firstName("Amr") 
				.lastName("Mahmoud") 
				.role(AppUserRole.ROLE_LIBRARIAN.name())  
				.password("123456")
				.build()
				); 
	   System.out.println("Admin Auth Token : " + admin.getToken()); 
	   System.out.println("Librlibrarian Auth Token : " + LIBRLIBRARIAN.getToken()) ; 
		};
	}

}
