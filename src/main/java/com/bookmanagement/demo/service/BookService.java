package com.bookmanagement.demo.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookmanagement.demo.models.Book;
import com.bookmanagement.demo.models.exceptions.AlreadyExistException;
import com.bookmanagement.demo.models.exceptions.NotFoundException;
import com.bookmanagement.demo.models.exceptions.ValidationException;
import com.bookmanagement.demo.repo.IBookRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookService implements IBookService {

	private final IBookRepository repo ; 
	
	
	@Override
	public List<Book> findAllBooks() { 
	   List<Book> res = new ArrayList<>() ; 
	   
		 this.repo.findAll().forEach(res::add); 
	  return res ; 
	}

	@Override
	public Book retriveBookById(Integer id) {
	
		return this.repo.findById(id).orElseThrow(()->new NotFoundException("Book with id " + id + " not found")) ; 
	}


	@Override
	@Transactional
	public Book addBook(Book book) {
		
		makeCommonValidation(book); 
		
		// set the book id by null to ensure that this method only deal with adding new book 
		// handle update operation by other function make code more clean (SRP) 
		book.setId(null);  
	   
		String isbn = book.getISBN() ; 
		
		if(repo.existsByISBN(isbn)) { 
			throw new AlreadyExistException("Book with ISBN " + isbn + " is aready exists") ;
		}
	    
	   return   this.repo.save(book) ; 
	    
	    
	}

	private void makeCommonValidation(Book book) { 
		if(book == null) { 
			throw new ValidationException("Book should not be null") ; 
		}
	
		if(book.getAuthor() == null) { 
			throw new ValidationException("Book author should not be null") ;
		}
	
		if(book.getTitle() == null) { 
			throw new ValidationException("Book title should not be null") ; 
		}

		String isbn = book.getISBN() ; 
		
		if(isbn == null) { 
			throw new ValidationException("Book ISBN should not be null") ; 
		}
		
		if(!(isValidIsbn10(isbn) || isValidIsbn13(isbn))) { 
			throw new ValidationException("Book ISBN format is not valid , expection ISBN-13  or ISBN-10 format") ; 
		}

		 String pubYear = book.getPublicationYear() ; 
		    if(pubYear != null) { 
		    	if(!Pattern.matches("^\\d{4}$", pubYear)) { 
		    		throw new ValidationException("Book publication year format is not valid ") ;
		    	}
		    	
		    	int bookPubYear = Integer.parseInt(pubYear) ; 
		    	
		    	if(bookPubYear > Year.now().getValue()) {
		    		throw new ValidationException("Book publication year is not valid ") ; 
		    	}
		    }
		
	}
	
	
	@Override
	@Transactional
	public Book updateBook(Book book) {
		
		if(book.getId() == null) { 
		  throw new ValidationException("Book reference is required to update its details");	
		}
		
		if(!repo.existsById(book.getId())) { 
			throw new NotFoundException("Book with id " + book.getId() + " is not exists") ;
		}
		
		makeCommonValidation(book); 
		
		Book bookByISBN = this.repo.findBookByISBN(book.getISBN()) ; 
		
		if(bookByISBN != null && bookByISBN.getId() != book.getId()) { 
			throw new AlreadyExistException("Book with ISBN is alread exists") ; 
		}
		
		return this.repo.save(book);
	}

	@Override
	@Transactional
	public Book removeBook(Integer bookId) {
		
		if(bookId == null) { 
			  throw new ValidationException("Book reference is required to delete book");	
		}
			
		Book book = this.repo.findById(bookId)
				.orElseThrow(()->new NotFoundException("Book with id " + bookId + " is not exists ")) ; 
		
		repo.deleteById(book.getId());
		
		return book ; 
	}



	    private  boolean isValidIsbn10(String isbn) {
	        String regex = "^(?:\\d{9}[\\dXx]|[0-9]{1}-[\\dXx]{3}-[\\dXx]{5}-[\\dXx])$";
	        return isbn.matches(regex);
	    }

	    private boolean isValidIsbn13(String isbn) {
	        String regex = "^(978|979)-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d$";
	        return isbn.matches(regex);
	    }
	
}
