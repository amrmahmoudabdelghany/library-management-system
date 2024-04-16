package com.bookmanagement.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookmanagement.demo.dto.auth.LoginRequest;
import com.bookmanagement.demo.dto.auth.LoginResponse;
import com.bookmanagement.demo.dto.auth.RegRequest;
import com.bookmanagement.demo.dto.auth.RegResponse;
import com.bookmanagement.demo.models.AppUser;
import com.bookmanagement.demo.models.AppUserRole;
import com.bookmanagement.demo.models.exceptions.AlreadyExistException;
import com.bookmanagement.demo.models.exceptions.ValidationException;
import com.bookmanagement.demo.repo.IAppUserRepo;
import com.bookmanagement.demo.security.IJWTService;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService implements IAuthService{

	private final IAppUserRepo repo ; 
	private final PasswordEncoder passwordEncoder ; 
	
	private final IJWTService jwtService ;  
	
	private final AuthenticationManager autManager ; 
	
	
	
	
	@Override
	public LoginResponse login(@Nonnull  LoginRequest request) {
	   
		 
		
		String email = request.getEmail() ; 
		
		checkEmail(email); 
		
		AppUser appUser = this.repo. 
							findByEmail(email) 
							.orElseThrow(()->new ValidationException("No account registerd with email  " + email)) ;  
		
		  this.autManager.authenticate(
				  new UsernamePasswordAuthenticationToken(request.getEmail(), 
						  request.getPassword())) ;
		
		 String token = this.jwtService.generateToken(email) ; 
		 
		return LoginResponse.builder()
				.firstName(appUser.getFirstName()) 
				.lastName(appUser.getLastName()) 
				.email(email) 
				.token(token)
				.build();
	}
	@Override
	public RegResponse register(@Nonnull RegRequest request) {
	 
		String firstName = request.getFirstName() ; 
		if(firstName == null || firstName.isBlank() ) { 
			throw new ValidationException("Account first name is required") ; 
		}
		if(!firstName.matches("[a-zA-Z]+")) { 
			throw new ValidationException("Account first name should only contains alphabetic characters") ;
		}
		if(firstName.length() > 25) { 
			throw new ValidationException("Account first name should not exceed 25 characters ") ; 
		}
		
		String lastName = request.getLastName() ; 
		
		if(lastName== null || lastName.isBlank()) { 
			throw new ValidationException("Account last name is required") ;
		}
		
		if(!lastName.matches("[a-zA-Z]+")) { 
			throw new ValidationException("Account last name should only contains alphabetic characters") ;
		}
		
		if(lastName.length() > 25) { 
			throw new ValidationException("Account first name should not exceed 25 characters ") ; 		
		}
		
		String email = request.getEmail() ; 
		
		checkEmail(email) ;
		
		if(this.repo.existsByEmail(email)) { 
			throw new AlreadyExistException("Email " + email + " already exists") ;
		}
		
		String password  = request.getPassword() ; 
		
		if(password == null || password.isBlank() ) { 
			throw new ValidationException("Account password is required") ; 
		}
		
		if(password.length() < 6 || password.length() > 25) { 
			throw new ValidationException("Account password should not less than 6 characters and not exceed 25 characters") ; 
		}
		
		String strRole = request.getRole() ; 
		AppUserRole role = AppUserRole.ROLE_LIBRARIAN ; // default role 
		
		if(strRole != null && !strRole.isBlank()) { 
		  
		  if(strRole.toLowerCase().equals(AppUserRole.ROLE_ADMIN.name().toLowerCase())) { 
			  role = AppUserRole.ROLE_ADMIN ;
		  }
		  
		}
		
		
		AppUser appUser = new AppUser() ;
		appUser.setFirstName(firstName); 
		appUser.setLastName(lastName); 
		appUser.setEmail(email);  
		appUser.setRole(role); 
		appUser.setPassword(this.passwordEncoder.encode(password));
		
		appUser = this.repo.save(appUser) ;
		
	  String token = 	this.jwtService.generateToken(appUser.getEmail()) ;
		
	return  RegResponse.builder()
	  .id(appUser.getId()) 
	  .token(token)
	  .build();
	
	}
	
	
	private void checkEmail(String email) { 
		if(email == null || email.isBlank() ) { 
			throw new ValidationException("Account email is required") ; 
		}
		
		if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$" )) { 
			throw new ValidationException("Account email is invalid") ;
		}
		
	}
}
