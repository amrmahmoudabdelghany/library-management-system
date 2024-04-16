package com.bookmanagement.demo.service;

import java.util.List;
import java.util.Optional;

import com.bookmanagement.demo.models.Patron;


public interface IPatronService {
 

	/**
	 * Retrieve a list of all patrons.
	 * 
	 * @return List of all patrons
	 */
	public List<Patron> findAllPatrons() ;  

	/**
	 * Retrieve a specific patron by ID.
	 * @param  id of the patron to be returned
	 * @return patron 
	 * @throws NotFoundException if there is not patron with passed id 
	 */
	public Patron  retrivePatronById(Integer id) ;   
	
	/**
	 * Add a new patron 
	 * 
	 * @param patron to add
	 * @return the added patron 
	 * @throws AlreadyExistException if there is patron with the same email or phone number
	 * @throws ValidationException if the passed patron is null or any of its required fields
	 */
	public Patron addPatron(Patron patron) ; 
	

	/**
	 * Update an existing patron
	 * 
	 * @param patron to update
	 * @return the updated patron
	 * @throws NotFoundException if there is not patron with passed id 
	 * @throws ValidationException if the passed patron is null or any of its required fields
	 */
	public Patron updatePatron(Patron patron) ; 
	

	/**
	 * Remove an existing patron by its id  
	 * if there is no patron with this id 
	 * 
	 * @param patron to delete
	 * @return the removed patron or null if is not exists
	 * @throws ValidationException if the passed patron id is null 
	 */
	public Patron removePatron(Integer patronId) ; 

	
}
