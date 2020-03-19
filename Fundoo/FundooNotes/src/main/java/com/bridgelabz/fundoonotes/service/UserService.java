package com.bridgelabz.fundoonotes.service;

import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.dto.LoginDto;
import com.bridgelabz.fundoonotes.dto.ResetPasswordDto;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.model.UserModel;

/**
 * This interface has the unimplemented functionality of registering the user
 * and verifying with the identity, login, forget password and update password
 * functionality.
 * 
 * @author Sudeep Kumar Katiar
 * @created 2019-12-30
 * @version 1.0
 */

@Component
public interface UserService {
	
	UserModel register(UserDto userdto);

	UserModel login(LoginDto logindto);

	UserModel verify(String token);

	UserModel forgetPassword(String email);
	
	UserModel resetPassword(ResetPasswordDto resetpassword, String token);

}
