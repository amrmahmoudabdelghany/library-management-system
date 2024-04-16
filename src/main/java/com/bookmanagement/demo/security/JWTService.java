package com.bookmanagement.demo.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService implements IJWTService {

	
	// 256-bit hex key  to sign and verify tokens 
	 private static final String SECRET_KEY = 
			 "6C53456D52745145684361564770696630754B4779672B4D44627A30364B376E66782B7A354573483476633D" ;  
	 
	    @Override
	    public String extractUsername(String jwt) {
	          
	        return extractClaims(jwt,Claims::getSubject);
	    } 
	 
	    public <T> T extractClaims(String token , Function<Claims , T> claimsResolver) { 
	        final Claims claims = extractAllClaims(token) ;  
	 

	        return claimsResolver.apply(claims) ; 
	    } 

	    @Override
	    public boolean isValidToken(String token, UserDetails user) {
	         return  extractUsername(token).equals(user.getUsername()) && !isTokenExpaired(token) ; 
	    }
	  
	    private boolean isTokenExpaired(String token) {
	        return extractExpDate(token).before(new Date()) ; 
	    } 


	    private Date extractExpDate(String token) {
	        return extractClaims(token, Claims::getExpiration); 
	    }

	    @Override
	    public String generateToken(String user) {
	        return generateToken(new HashMap<>() , user) ; 
	    }

	    @Override
	    public String generateToken(Map<String, Object> extraClimes, String user) {
	 
	     return Jwts.builder()
	     .subject(user) 
	     .issuedAt(new Date(System.currentTimeMillis())) 
	     .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 1 day 
	     .claims(extraClimes)  
	     .signWith(getSigninkey())
	     .compact() ; 
	    } 

	    private Claims extractAllClaims(String jwt) { 
	        return Jwts
	        .parser() 
	        .verifyWith(getSigninkey())
	        .build() 
	        .parseSignedClaims(jwt) 
	        .getPayload() ; 

	    } 
	 
	    private SecretKey getSigninkey() { 
	        final  byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY) ; 
	         return Keys.hmacShaKeyFor(keyBytes) ; 
	    }

}
