package com.bookmanagement.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookmanagement.demo.models.Patron;
import com.bookmanagement.demo.models.exceptions.AlreadyExistException;
import com.bookmanagement.demo.models.exceptions.NotFoundException;
import com.bookmanagement.demo.models.exceptions.ValidationException;
import com.bookmanagement.demo.repo.IPatronRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PatronService implements IPatronService {

	private IPatronRepository repo ;

	@Override
	public List<Patron> findAllPatrons() {
		List<Patron> patrons = new ArrayList<>() ; 
		this.repo.findAll().forEach(patrons::add);
	     return patrons ; 
	}

	@Override
	public Patron retrivePatronById(Integer id) {
		return this.repo.findById(id).orElseThrow(()->new NotFoundException("Patron with id " + id + " is not exists")) ;
	}

	@Override
	@Transactional
	public Patron addPatron(Patron patron) {
	
	
		patron.setId(null); 
		
		makeCommonValidation(patron); 
	
		if(this.repo.existsByEmail(patron.getEmail())) { 
			throw  new AlreadyExistException("Patron email " + patron.getEmail() + " is already exists") ; 
		}
		
		if(this.repo.existsByPhone(patron.getPhone())) { 
			throw new AlreadyExistException("Patron phone number " + patron.getPhone() + " is already exists") ; 
		}
		
		return this.repo.save(patron);
	}


	@Override
	@Transactional
	public Patron updatePatron(Patron patron) {
 
		if(patron.getId() == null) { 
			throw new ValidationException("Patron reference required to update its details") ;
		}
		
		makeCommonValidation(patron); 
		
		// try to check if passed email is owned by another patron 
		Patron patronByEmail = this.repo.findPatronByEmail(patron.getEmail()) ; 
		
		if(patronByEmail != null && patronByEmail.getId() != patron.getId()) { 
			throw new ValidationException("Patron email " + patron.getEmail() + " is already exists") ;
		}
		
		Patron patronByPhone  = this.repo.findPatronByPhone(patron.getPhone()) ; 
		
		if(patronByPhone != null && patronByPhone.getId() != patron.getId()) { 
			throw new ValidationException("Patron phone " + patron.getPhone() + " is already exists") ; 
		}
		
		return this.repo.save(patron);
	}

	@Override
	@Transactional
	public Patron removePatron(Integer patronId) {
		
		if(patronId == null) { 
			 throw new ValidationException("Patron reference required "); 
		}
		
		Patron target = this.repo
				.findById(patronId)
				.orElseThrow(()->new NotFoundException("Could not found patron with id " + patronId )) ; 

		this.repo.deleteById(target.getId());
 
	return target ; 
	} 
	
	
	private void makeCommonValidation(Patron patron) { 
		if(patron == null) { 
			throw new ValidationException("Patron should not be null") ; 
		}
		String firstName = patron.getFirstName() ; 
		
		if(firstName == null || firstName.isBlank() ) { 
			throw new ValidationException("Patron first name is required") ; 
		}
		if(!firstName.matches("[a-zA-Z]+")) { 
			throw new ValidationException("Patron first name should only contains alphabetic characters") ;
		}
		if(firstName.length() > 25) { 
			throw new ValidationException("Patron first name should not exceed 25 characters ") ; 
		}
		
		String lastName = patron.getLastName() ; 
		
		if(lastName== null || lastName.isBlank()) { 
			throw new ValidationException("Patron last name is required") ;
		}
		
		if(!lastName.matches("[a-zA-Z]+")) { 
			throw new ValidationException("Patron last name should only contains alphabetic characters") ;
		}
		
		if(lastName.length() > 25) { 
			throw new ValidationException("Patron first name should not exceed 25 characters ") ; 		
		}
		
		String email = patron.getEmail() ; 
		
		if(email == null || email.isBlank() ) { 
			throw new ValidationException("patron email is required") ; 
		}
		
		if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$" )) { 
			throw new ValidationException("patron email is invalid") ;
		}
		
	    String phone = patron.getPhone() ; 
	    
	    if(phone == null || phone.isBlank()) { 
	    	throw new ValidationException("patron phone is required") ; 
	    }
	    
	    // check egypt phone format
	    if(!(phone.startsWith("") && phone.matches("[0-9]+") && phone.length() == 11)) { 
	    	throw new ValidationException("patron phone format is invalid") ;
 	    }
	   	
	}	
}
