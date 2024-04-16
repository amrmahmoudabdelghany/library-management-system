package com.bookmanagement.demo.models;

import java.time.LocalDate;
import java.util.Date;

import org.hibernate.annotations.Cascade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
public class Borrow {
 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "borrow_id")
	private Integer id ; 

	@ManyToOne( optional = false)
	@JoinColumn(name = "patorn_id" , nullable =  false , unique = false) 
	private Patron patron ; 
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id" , nullable =  false , unique = false)
	private Book book  ; 
	
	@Column(name = "borrow_date" , nullable = false)
	private LocalDate borrowDate ; 
	
	@Column(name = "return_date" , nullable = true)
	private LocalDate returnDate ; 
	
	
	
	
	
}
