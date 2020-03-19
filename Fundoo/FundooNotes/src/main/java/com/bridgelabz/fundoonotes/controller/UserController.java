package com.bridgelabz.fundoonotes.controller;

/**
 * user controller 
 * @author user
 * @date : 07/01/2020
 * @version :1.0
 */

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.LoginDto;
import com.bridgelabz.fundoonotes.dto.ResetPasswordDto;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.UserService;
import com.bridgelabz.fundoonotes.utility.Jwt;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@CrossOrigin(allowedHeaders = "*", origins = "*", exposedHeaders = { "jwtToken" })
public class UserController {

	@Autowired
	private UserService userservice;

	@Autowired
	private Jwt tokenGenerator;

	/**
	 * api for user registration
	 * 
	 * @param userDto
	 * @return response
	 */
	@PostMapping("/register")
	@ApiOperation(value = "Api to Register User for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> register(@Valid @RequestBody UserDto userdto) {

		UserModel user = userservice.register(userdto);
		if (user != null) {

			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "registration successfull", user));
		} else {
			return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
					.body(new Response(208, "user already exist", userdto));
		}

	}

	/**
	 * api for user login
	 * 
	 * @param loginDetails 
	 * @return response
	 */
	@PostMapping("/login")
	@ApiOperation(value = "Api to Login User for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> login(@Valid @RequestBody LoginDto logindto) {
		UserModel userInformation = userservice.login(logindto);
		if (userInformation != null) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, tokenGenerator.createToken(userInformation.getId()), userInformation));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Login failed", 400));
	}

	/**
	 * api for user verification
	 * 
	 * @param token
	 * @return response Entity
	 */
	@GetMapping("/verify/{token}")
	@ApiOperation(value = "Api to Verify User for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> userVerification(@Valid @PathVariable("token") String token) {
		UserModel user = userservice.verify(token);
		if (user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("verified", 200));
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new Response("not verified", 304));

	}

	/**
	 * api for forget password
	 * 
	 * @param email
	 * @return response
	 */
	@PostMapping("/forgotpassword")
	@ApiOperation(value = "Api to FoegetPassword User for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> forgotPassword(@RequestBody UserDto email) {

		log.info("email "+email.getEmail());
		UserModel user = userservice.forgetPassword(email.getEmail());
		if (user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("User Exist", 200));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("User Doesn't Exist", 400));
		}
	}


	/**
	 * api for reset password
	 * 
	 * @param token
	 * @param pswd
	 * @return response
	 * @throws Exception
	 */
	@PostMapping("/resetpassword/{token}")
	@ApiOperation(value = "Api to ResetPassword User for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordDto resetPassword,
			@RequestHeader("token") String token) {

		UserModel user = userservice.resetPassword(resetPassword, token);

		if (user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Password is Updated Successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response("Password and Confirm Password doesn't matched", 400));
		}

	}

}
