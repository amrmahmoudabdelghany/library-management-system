package com.bookmanagement.demo.service;

import java.util.List;
import java.util.Optional;

import com.bookmanagement.demo.models.Book;
import com.bookmanagement.demo.models.exceptions.AlreadyExistException;
import com.bookmanagement.demo.models.exceptions.NotFoundException;
import com.bookmanagement.demo.models.exceptions.ValidationException;
/**
 * 
 * The intent of this type is to encapsulate business rules and policies
 */
public interface IBookService {

	
	
	/**
	 * Retrieve a list of all books.
	 * 
	 * @return List of all books
	 */
	public List<Book> findAllBooks() ;  

	/**
	 * Retrieve a specific book by ID.
	 * @param  id of the book to be returned
	 * @return Optional of Book or null if not exists
	 */
	public Book  retriveBookById(Integer id) ;   
	
	/**
	 * Add a new book to the library.
	 * 
	 * @param book to add
	 * @return the added book 
	 * @throws AlreadyExistException if there is book with the same ISBN
	 * @throws ValidationException if the passed book is null or any of its required fields
	 */
	public Book addBook(Book book) ; 
	

	/**
	 * Update an existing book
	 * 
	 * @param book to update
	 * @return the updated book
	 * @throws NotFoundException if there is not book with passed id 
	 * @throws ValidationException if the passed book is null or any of its required fields
	 */
	public Book updateBook(Book book) ; 
	

	/**
	 * Remove an existing book by its id  
	 * if there is no book with this id 
	 * 
	 * @param book to delete
	 * @return the removed book or null if is not exists
	 * @throws ValidationException if the passed book id is null 
	 */
	public Book removeBook(Integer bookId) ; 
	
	
}
