package com.blblz.fundoonotes.service;

import com.blblz.fundoonotes.dto.UserDto;
import com.blblz.fundoonotes.responses.Response;

public interface UserService {
	
	Response addUser(UserDto userdto);

}