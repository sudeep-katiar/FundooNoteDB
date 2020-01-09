package com.blblz.fundoonotes.service;

import com.blblz.fundoonotes.dto.LoginDto;
import com.blblz.fundoonotes.dto.ResetPasswordDto;
import com.blblz.fundoonotes.dto.UserDto;
import com.blblz.fundoonotes.model.UserModel;
import com.blblz.fundoonotes.responses.Response;

public interface UserService {
	
	UserModel register(UserDto userdto);

	UserModel login(LoginDto logindto);

	UserModel resetPassword(ResetPasswordDto resetpassword, String token);

}
