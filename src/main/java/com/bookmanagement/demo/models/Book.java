package com.bookmanagement.demo.models;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "book_id")
	private Integer id ; 
	
	@Column(name = "title" , nullable = false)
	private String title ;
	
	@Column(name = "author" , nullable = false)
	private String author ; 
	
	@Column(name = "publication_year" )
	private String publicationYear ;  
	
	@Column(name = "isbn" , nullable = false , unique = true)
	private String ISBN ;  
	
	@Column(name = "is_borrowed" , nullable =  false  , columnDefinition = "boolean default false")
	private Boolean isBorrowed ; 
	
	
	
	
}
