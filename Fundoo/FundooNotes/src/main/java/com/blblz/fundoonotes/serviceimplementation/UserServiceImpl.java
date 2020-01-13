package com.blblz.fundoonotes.serviceimplementation;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blblz.fundoonotes.dto.LoginDto;
import com.blblz.fundoonotes.dto.ResetPasswordDto;
import com.blblz.fundoonotes.dto.UserDto;
import com.blblz.fundoonotes.model.UserModel;
import com.blblz.fundoonotes.repository.UserRepository;
import com.blblz.fundoonotes.service.UserService;
import com.blblz.fundoonotes.utility.EmailVerify;
import com.blblz.fundoonotes.utility.Jwt;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private BCryptPasswordEncoder bcryptpasswordEncoder;

	@Autowired
	private UserRepository repository;

	@Autowired
	private EmailVerify mail;

	@Autowired
	private Jwt tokenGenerator;

	@Override
	public UserModel register(UserDto userdto) {

//		System.out.println("1");
		Date date = new Date();
//		System.out.println(userdto.getEmail());
//		System.out.println(repository.findEmail(userdto.getEmail()));
		
		UserModel emailavailable = repository.findEmail(userdto.getEmail());
		if (emailavailable == null) {
			UserModel userDetails = new UserModel(userdto.getFirstName(), userdto.getLastName(), userdto.getEmail(),
					userdto.getMobile(), userdto.getPassword());
			userDetails.setCreatorStamp(date);
			userDetails.setVerified(false);
			userDetails.setPassword(bcryptpasswordEncoder.encode(userDetails.getPassword()));

//			System.out.println(userDetails.isVerified());
//			System.out.println("entering inside");

			repository.insertdata(date, userdto.getEmail(), userdto.getFirstName(), false, userdto.getLastName(),
					userdto.getMobile(), bcryptpasswordEncoder.encode(userdto.getPassword()), userdto.getUserName());

			UserModel sendMail = repository.findEmail(userdto.getEmail());
//			UserModel sendMsg = repository.findmobile(userdto.getMobile());
//			System.out.println(sendMail);
			String response = "http://localhost:8080/users/verify/" + tokenGenerator.createToken(sendMail.getId());
			mail.sendVerifyMail(sendMail.getEmail(), response);
//			mail.sendmsg(sendMail.getMobile(), response);

			return userDetails;
		} else {
			return null;
		}
	}

	public UserModel login(LoginDto logindto) {
		UserModel usermodel;
		usermodel = repository.findEmail(logindto.getEmail());
		if (bcryptpasswordEncoder.matches(logindto.getPassword(),usermodel.getPassword())) {
			return usermodel;
		}
		return null;
	}

	@Override
	public UserModel verify(String token) {
		
		long id = tokenGenerator.parseJwtToken(token);
		System.out.println(id);
		UserModel userInfo = repository.findById(id);
		if (userInfo != null) {
			if (!userInfo.isVerified()) {
				userInfo.setVerified(true);
				repository.verify(userInfo.getId());
				return userInfo;
			} else {
				return userInfo;
			}
		}
		return null;

	}

	@Override
	public UserModel forgetPassword(String email)
	{
		UserModel isIdAvailable = repository.findEmail(email);
		if (isIdAvailable != null && isIdAvailable.isVerified() == true) {
			String response = "http://localhost:8080/users/resetpassword/"
					+ tokenGenerator.createToken(isIdAvailable.getId());

			mail.sendForgetPasswordMail(isIdAvailable.getEmail(), response);
			return isIdAvailable;
		}
		return null;
	}
	
	@Override
	public UserModel resetPassword(ResetPasswordDto resetpassword, String token)
	{
		if (resetpassword.getPassword().equals(resetpassword.getConfirmpassword()))
		{
			long id = tokenGenerator.parseJwtToken(token);
			UserModel isIdAvailable = repository.findById(id);
			if (isIdAvailable != null)
			{
				isIdAvailable.setPassword(bcryptpasswordEncoder.encode((resetpassword.getPassword())));
				repository.save(isIdAvailable);
				return isIdAvailable;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

}
