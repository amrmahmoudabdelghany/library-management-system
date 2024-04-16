package com.bookmanagement.demo.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bookmanagement.demo.repo.IAppUserRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthFilter  extends OncePerRequestFilter{

	
    private final IJWTService jwtService ; 
    private final UserDetailsService userService ; 

    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
    		HttpServletResponse response,
    		FilterChain filterChain)
    		throws ServletException, IOException {
    	
    	  
        final String jwt   = request.getHeader("Authorization") ; 
        
        if(jwt == null || !jwt.startsWith("Bearer ")){
            filterChain.doFilter(request, response); 
            return ; 
        }
        final String token = jwt.substring(7) ;
         
        final String email = jwtService.extractUsername(token) ; 
        
        // if email is valid and user is not authenticated
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) { 

            final UserDetails userDetails = userService.loadUserByUsername(email) ;
            
            UsernamePasswordAuthenticationToken authtoken = 
             new UsernamePasswordAuthenticationToken(userDetails , null , 
             userDetails.getAuthorities()) ; 
            
             authtoken.setDetails(new WebAuthenticationDetailsSource()
            		 .buildDetails(request));  

             SecurityContextHolder.getContext().setAuthentication(authtoken); 


        } 

        filterChain.doFilter(request, response);



    }
    
    
}
