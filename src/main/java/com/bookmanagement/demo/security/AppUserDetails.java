package com.bookmanagement.demo.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bookmanagement.demo.models.AppUser;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppUserDetails implements UserDetails {

	private final AppUser user ; 
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.user.getRole().name()));
	}

	@Override
	public String getPassword() {
		return this.user.getPassword() ;
	}

	@Override
	public String getUsername() {
		return this.user.getEmail() ; 
	}

	@Override
	public boolean isAccountNonExpired() {
		return true ; 
	}

	@Override
	public boolean isAccountNonLocked() {
		return true ; 
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true ; 
	}

	@Override
	public boolean isEnabled() {
	   return true ; 
	}

	
	
}
