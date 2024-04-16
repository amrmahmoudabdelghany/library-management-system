package com.bookmanagement.demo.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bookmanagement.demo.dto.BorrowRequest;
import com.bookmanagement.demo.dto.BorrowResponse;
import com.bookmanagement.demo.dto.ReturnRequest;
import com.bookmanagement.demo.dto.ReturnResponse;
import com.bookmanagement.demo.models.Book;
import com.bookmanagement.demo.models.Borrow;
import com.bookmanagement.demo.models.Patron;
import com.bookmanagement.demo.models.exceptions.BookNotAvailableException;
import com.bookmanagement.demo.models.exceptions.ValidationException;
import com.bookmanagement.demo.repo.IBorrowRepository;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;

@Service
@Transactional(propagation = Propagation.REQUIRED) // both use-cases must be transactional
@AllArgsConstructor
public class BorrowService implements IBorrowService {

	private final IBorrowRepository repo ; 
	private final IBookService bookService ; 
	private final IPatronService patronService ; 
	
	
   @Override
   public BorrowResponse borrow(@Nonnull BorrowRequest request) { 
		
		if(request.getBookId() == null) { 
			throw new ValidationException("Book id is required ") ;
		}
		
		if(request.getPatronId() == null) { 
			throw new ValidationException("Patron id is required") ; 
		}
		
		Book targetBook = this.bookService.retriveBookById(request.getBookId()) ; 
		

		
		if(targetBook.getIsBorrowed()) { 
			throw new BookNotAvailableException("Sorry , book is already borrowed") ;
		}
		
		Patron targetPatron = this.patronService.retrivePatronById(request.getPatronId()) ; 
				
		
		Borrow borrow = new Borrow() ;
				borrow.setBook(targetBook)  ;
				borrow.setPatron(targetPatron) ; 
				borrow.setBorrowDate(LocalDate.now()) ;
				borrow.setReturnDate(null)  ;  
				
		borrow = this.repo.save(borrow) ;
		
		targetBook.setIsBorrowed(true); 
		
		targetBook = this.bookService.updateBook(targetBook) ; 
		
		
		return new BorrowResponse(borrow.getId() ,targetPatron.getId(), targetBook.getId(), targetPatron.getFullName(), targetBook.getTitle());
	}
	
	@Override
	public ReturnResponse returnBook(@Nonnull ReturnRequest request) {
	  
		if(request.getBookId() == null) { 
			throw new ValidationException("Book id is required") ;
		}
		
		if(request.getPatronId() == null) { 
			throw new ValidationException("Patron id is required") ;
		}
		
	  final Patron targetPatron = this.patronService.retrivePatronById(request.getPatronId()) ; 
	
	  final	Book targetBook = this.bookService.retriveBookById(request.getBookId()) ;
		 
	  if(!targetBook.getIsBorrowed()) { 
		  throw new ValidationException("The book with id " + targetBook.getId() +  " has not been borrowed before") ;
	  }
		Borrow targetBorrow = this.repo.findByPatronIdAndBookId(targetPatron.getId(),targetBook.getId()) 
				.orElseThrow(()->
				new ValidationException("Patron " + 
						targetPatron.getFullName() + 
						" had never borrowed the " + targetBook.getTitle() + " book before" )); 
		
		targetBorrow.setReturnDate(LocalDate.now()); 
		
	    targetBorrow =	this.repo.save(targetBorrow) ;
		
	    targetBook.setIsBorrowed(false); 
		
	    this.bookService.updateBook(targetBook) ; 
		
		return ReturnResponse.builder()
				.id(targetBorrow.getId())
				.bookId(targetBook.getId()) 
				.bookTitle(targetBook.getTitle())
				.patronId(targetPatron.getId())
				.patronName(targetPatron.getFullName()) 
				.build();
	}
}
