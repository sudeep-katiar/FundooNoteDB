package com.blblz.fundoonotes.serviceimplementation;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.blblz.fundoonotes.dto.LoginDto;
import com.blblz.fundoonotes.dto.ResetPasswordDto;
import com.blblz.fundoonotes.dto.UserDto;
import com.blblz.fundoonotes.model.UserModel;
import com.blblz.fundoonotes.repository.UserRepository;
import com.blblz.fundoonotes.responses.Response;
import com.blblz.fundoonotes.service.UserService;
import com.blblz.fundoonotes.utility.EmailVerify;
import com.blblz.fundoonotes.utility.Jwt;

public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private EmailVerify mail;
	
	@Autowired
	private Jwt tokenGenerator;

//	@Autowired
//	private UserDto user;

	@Override
	public UserModel register(UserDto userdto) {
		
		Date date = new Date();
		UserModel emailavailable = UserRepository.findEmail(userdto.getEmail());
		if (emailavailable == null)
		{
			UserModel userDetails = new UserModel(userdto.getFirstName(), userdto.getLastName(), userdto.getEmail(),
					userdto.getMobile(), userdto.getPassword());
			userDetails.setCreatorStamp(date);
			userDetails.setVerified(false);
			userDetails.setPassword(userDetails.getPassword());
			
			System.out.println("entering inside");
			
			repository.insertdata(date, userdto.getEmail(), userdto.getFirstName(), false, userdto.getLastName(),
					userdto.getMobile(), userdto.getPassword(), userdto.getUserName());
			
			UserModel sendMail = UserRepository.findEmail(userdto.getEmail());
			String response = "http://localhost:8080/users/verify/"+ tokenGenerator.createToken(sendMail.getId());
			mail.sendVerifyMail(sendMail.getEmail(), response);
			sendMail.setPassword("******");
			return sendMail;
		}else {
		return null;}
	}
	
	public UserModel login(LoginDto logindto)
	{
		UserModel usermodel;
		usermodel = UserRepository.findEmail(logindto.getEmail());
		if(logindto.getPassword().equals(UserRepository.findEmail(logindto.getEmail()).getPassword()))
		{
			return usermodel;
		}
		return null;
	}

	@Override
	public UserModel resetPassword(ResetPasswordDto resetpassword, String token) {
		if (resetpassword.getPassword().equals(resetpassword.getConfirmpassword()))
		{
			String id = tokenGenerator.parseJwtToken(token);
			UserModel isIdAvailable = repository.findById(id);
		}
		return null;
	}

}
