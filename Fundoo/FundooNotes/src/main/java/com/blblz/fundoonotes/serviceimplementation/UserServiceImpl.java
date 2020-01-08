package com.blblz.fundoonotes.serviceimplementation;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.blblz.fundoonotes.dto.UserDto;
import com.blblz.fundoonotes.model.UserModel;
import com.blblz.fundoonotes.repository.UserRepository;
import com.blblz.fundoonotes.responses.Response;
import com.blblz.fundoonotes.service.UserService;

public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private UserDto user;
	
	@Override
	public Response addUser(UserDto userdto) {
		if(repository.findEmail(userdto.getEmail())==null)
		{
			Date date = new Date();
			
			repository.insertdata(date, userdto.getEmail(), userdto.getFirstName(), false, userdto.getLastName(), userdto.getMobile(), userdto.getPassword(), userdto.getUserName());
		}
		
		return null;
	}

}
