package com.blblz.fundoonotes.serviceimplementation;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.blblz.fundoonotes.dto.LoginDto;
import com.blblz.fundoonotes.dto.UserDto;
import com.blblz.fundoonotes.model.UserModel;
import com.blblz.fundoonotes.repository.UserRepository;
import com.blblz.fundoonotes.responses.Response;
import com.blblz.fundoonotes.service.UserService;

public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

//	@Autowired
//	private UserDto user;

	@Override
	public Response register(UserDto userdto) {

		if (repository.findEmail(userdto.getEmail()) == null) {
			Date date = new Date();

			return repository.insertdata(date, userdto.getEmail(), userdto.getFirstName(), false, userdto.getLastName(),
					userdto.getMobile(), userdto.getPassword(), userdto.getUserName());

//			UserModel userModel = repository.findEmail(user.getEmail());
		}else {
		return null;}
	}
	
	public Response login(LoginDto logindto)
	{
		UserDto userdto = new UserDto();
		if(repository.findEmail(logindto.getEmail()) == repository.findEmail(userdto.getEmail()))
		{
			boolean isValid = logindto.getPassword().equals(userdto.getPassword());
			System.out.println(isValid);
		}
		return null;
	}

}
