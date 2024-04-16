package com.bookmanagement.demo.models;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_user") 
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id ; 
	
	@Column(nullable = false)
	private String firstName ; 

	@Column(nullable = false)
	private String lastName ;  
	
	@Column(unique =  true , nullable = false)
	private String email ; 

	@Column(nullable = false)
	private String password ; 
	
	@Enumerated(EnumType.STRING)
	private AppUserRole role ; 
	
	
	
}
