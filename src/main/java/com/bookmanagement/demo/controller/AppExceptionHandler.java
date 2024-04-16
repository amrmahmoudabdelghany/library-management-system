package com.bookmanagement.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.bookmanagement.demo.models.exceptions.AlreadyExistException;
import com.bookmanagement.demo.models.exceptions.AppException;
import com.bookmanagement.demo.models.exceptions.BookNotAvailableException;
import com.bookmanagement.demo.models.exceptions.NotFoundException;
import com.bookmanagement.demo.models.exceptions.ValidationException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestControllerAdvice
public class AppExceptionHandler {

	
	
	@ExceptionHandler({NoResourceFoundException.class})
	public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoResourceFoundException ex) { 
		HttpStatus statusCode = HttpStatus.NOT_FOUND ; 
		String message  = "Oops! The resource you're looking for cannot be found. Please check the URL and try again." ; 
		return ResponseEntity.status(statusCode)
				.body(new ErrorResponse(statusCode.name(), message)) ;
	}

	@ExceptionHandler({AppException.class}  )
	public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {  
		HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR ; 
		String message = "Sorry, an internal server error occurred. Please try again later." ; 
		
		if(ex instanceof NotFoundException) { 
			 statusCode = HttpStatus.BAD_REQUEST ; 
			 message = ex.getMessage() ;
		}else if (ex instanceof AlreadyExistException ||
				ex instanceof ValidationException || ex instanceof BookNotAvailableException) { 
			statusCode = HttpStatus.UNPROCESSABLE_ENTITY ; 
			message = ex.getMessage() ; 
		}
		
		ErrorResponse res = new ErrorResponse(statusCode.name(), message); 
		return ResponseEntity.status(statusCode).body(res) ;
	}
	
	
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public class ErrorResponse { 
		private String error ; 
		private String message ; 
	    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE) ;   
	}
	
	
}
