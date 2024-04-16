package com.bookmanagement.demo.service;

import com.bookmanagement.demo.dto.auth.LoginRequest;
import com.bookmanagement.demo.dto.auth.LoginResponse;
import com.bookmanagement.demo.dto.auth.RegRequest;
import com.bookmanagement.demo.dto.auth.RegResponse;

public interface IAuthService {
 
	RegResponse register(RegRequest request) ; 
	LoginResponse login(LoginRequest request) ; 
}
