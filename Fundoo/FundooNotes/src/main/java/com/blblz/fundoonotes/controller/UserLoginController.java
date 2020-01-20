package com.blblz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blblz.fundoonotes.dto.LoginDto;
import com.blblz.fundoonotes.dto.ResetPasswordDto;
import com.blblz.fundoonotes.dto.UserDto;
import com.blblz.fundoonotes.model.UserModel;
import com.blblz.fundoonotes.responses.Response;
import com.blblz.fundoonotes.service.UserService;
import com.blblz.fundoonotes.utility.Jwt;

@RestController
@RequestMapping("/users")
public class UserLoginController {

	@Autowired
	private UserService userservice;

	@Autowired
	private Jwt tokenGenerator;

	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody UserDto userdto) {
		
		UserModel user = userservice.register(userdto);
		if (user != null) {
			
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "registration successfull", user));
		} else {
			return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
					.body(new Response(400, "user already exist", userdto));
		}

	}

	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginDto logindto) {
		UserModel userInformation = userservice.login(logindto);

		if (userInformation != null) {
			// return new
			// ResponseEntity<Response>(serviceimpl.login(logindto),HttpStatus.OK);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new Response(200, tokenGenerator.createToken(userInformation.getId()), userInformation));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "Login failed", logindto));
		}

	}
	
	

	@GetMapping("/verify/{token}")
	public ResponseEntity<Response> userVerification(@PathVariable("token") String token) {
			UserModel user = userservice.verify(token);
			if(user != null)
			{
				return ResponseEntity.status(HttpStatus.OK).body(new Response("verified",200));
			}
				return ResponseEntity.status(HttpStatus.OK).body(new Response("not verified", 400));

	}
	
	@PostMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestParam("email") String email) {

		UserModel user = userservice.forgetPassword(email);
		if (user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("User Exist", 200));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("User Doesn't Exist", 400));
		}
	}

	@PostMapping("/resetpassword/{token}")
	public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordDto resetPassword, @PathVariable("token") String token) {
		
		UserModel user = userservice.resetPassword(resetPassword, token);

		if(user != null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Password is Update Successfully", 200));
		}
		else
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Password and Confirm Password doesn't matched", 400));
		}
						
	}

}
