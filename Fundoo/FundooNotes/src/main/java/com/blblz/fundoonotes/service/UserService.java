package com.blblz.fundoonotes.service;

import org.springframework.stereotype.Component;

import com.blblz.fundoonotes.dto.LoginDto;
import com.blblz.fundoonotes.dto.ResetPasswordDto;
import com.blblz.fundoonotes.dto.UserDto;
import com.blblz.fundoonotes.model.UserModel;

@Component
public interface UserService {
	
	UserModel register(UserDto userdto);

	UserModel login(LoginDto logindto);

	UserModel verify(String token);

	UserModel forgetPassword(String email);
	
	UserModel resetPassword(ResetPasswordDto resetpassword, String token);

}
