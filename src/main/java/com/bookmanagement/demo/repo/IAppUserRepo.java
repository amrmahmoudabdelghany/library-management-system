package com.bookmanagement.demo.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookmanagement.demo.models.AppUser;

@Repository
public interface IAppUserRepo extends CrudRepository<AppUser, Integer> {

	Optional<AppUser> findByEmail(String email) ;  
	Boolean existsByEmail(String email) ; 
	
}
