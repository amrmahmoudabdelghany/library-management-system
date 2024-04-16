package com.bookmanagement.demo.security;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface IJWTService {
 

    <T> T extractClaims(String token , Function<Claims , T> claimsResolver);
    String extractUsername(String jwt) ;  
    String generateToken(Map<String,Object> extraClimes , String subject) ;  
    String generateToken(String user) ;  
    boolean isValidToken(String token , UserDetails user) ; 

    
}
