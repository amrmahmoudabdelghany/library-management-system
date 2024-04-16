package com.bookmanagement.demo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookmanagement.demo.models.Patron;


@Repository
public interface IPatronRepository extends CrudRepository<Patron, Integer> {

	boolean existsByEmail(String email) ; 
	boolean existsByPhone(String phone) ; 
	
	Patron findPatronByEmail(String email) ; 
	Patron findPatronByPhone(String phone) ; 
	
}
