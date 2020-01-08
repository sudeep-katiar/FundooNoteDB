package com.blblz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blblz.fundoonotes.dto.LoginDto;
import com.blblz.fundoonotes.dto.UserDto;
import com.blblz.fundoonotes.responses.Response;
import com.blblz.fundoonotes.service.UserService;

@RestController
@RequestMapping("/users")
public class UserLoginController {
	
	@Autowired
	private UserService serviceimpl;
	
	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody UserDto userdto)
	{
//		System.out.println("12");
//		System.out.println(userdto.getFirstName());
		return new ResponseEntity<Response>(serviceimpl.register(userdto),HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginDto logindto)
	{
		return new ResponseEntity<Response>(serviceimpl.login(logindto),HttpStatus.OK);
		
	}
	

}
