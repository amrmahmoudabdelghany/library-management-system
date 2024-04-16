package com.bookmanagement.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Patron {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "patron_id")
	private Integer id ;  
	
	@Column(name = "patron_first_name" , nullable = false , length = 25)
	private String firstName ; 
	
	@Column(name = "patron_last_name" , nullable = false , length = 25) 
	private String lastName ; 
	
	@Column(name = "patron_email"  , nullable =  false , unique = true)
	private String email ; 
	
	@Column(name = "patron_phone" , nullable =  false , unique =  true)
	private String phone ;  
	
	
	
	@JsonIgnore
	public  String getFullName() { 
		return this.firstName + " " + this.lastName ; 
	}
	
}
