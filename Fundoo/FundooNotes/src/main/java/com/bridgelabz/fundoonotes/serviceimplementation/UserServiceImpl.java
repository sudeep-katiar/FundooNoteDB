package com.bridgelabz.fundoonotes.serviceimplementation;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.LoginDto;
import com.bridgelabz.fundoonotes.dto.ResetPasswordDto;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.UserService;
import com.bridgelabz.fundoonotes.utility.EmailVerify;
import com.bridgelabz.fundoonotes.utility.Jwt;

/**
 * This class has the implemented functionality of registering the user
 * and verifying with the identity, login, forget password and update password
 * functionality.
 * 
 * @author Sudeep Kumar Katiar
 * @created 2019-12-30
 * @version 1.0
 * @see {@link BCryptPasswordEncoder} for creating encrypted password
 * @see {@link UserRepository} for storing data with the database
 * @see {@link Jwt} fore creation of token
 * @see {@link EmailVerify} for mail facilities
 */

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

	/**
	 * This class takes the user inputed data and checks whether the user present in
	 * the database or not if the user is not registered with the database then it
	 * copies all the data from DTO to normal user class and encodes the user
	 * password and save the user with the database and then by using
	 * {@link Jwt} and {@link EmailVerify} it create a token and send
	 * the user's mail id for verification.
	 */
	@Override
	public UserModel register(UserDto userdto) {
		Date date = new Date();
		UserModel emailavailable = repository.findEmail(userdto.getEmail());
		System.out.println(emailavailable);
		if (emailavailable == null) {
			UserModel userDetails = new UserModel(userdto.getFirstname(), userdto.getLastname(), userdto.getEmail(),
					userdto.getMobile(), userdto.getPassword());
			userDetails.setCreatorStamp(date);
			userDetails.setVerified(false);
			userDetails.setPassword(bcryptpasswordEncoder.encode(userDetails.getPassword()));

			repository.insertdata(date, userdto.getEmail(), userdto.getFirstname(), false, userdto.getLastname(),
					userdto.getMobile(), bcryptpasswordEncoder.encode(userdto.getPassword()), userdto.getUsername());

			UserModel sendMail = repository.findEmail(userdto.getEmail());
			String response = "http://192.168.1.24:8080/users/verify/" + tokenGenerator.createToken(sendMail.getId());
			mail.sendVerifyMail(sendMail.getEmail(), response);

			return userDetails;
		} else {
			return null;
		}
	}

	/**
	 * this function takes login information from user on the basis of input user
	 * email id it fetch all information of the user from database and checks for
	 * Verification details of the user. if the user is verified then it return all
	 * information of user else it proceed with the verification.
	 */
	public UserModel login(LoginDto logindto) {
		UserModel usermodel = repository.findEmail(logindto.getEmail());
		if (usermodel.isVerified())
			if (bcryptpasswordEncoder.matches(logindto.getPassword(), usermodel.getPassword())) {
				return usermodel;
			}
		return null;
	}

	/**
	 * This function takes id as long input parameter and checks for user. if user is
	 * found it checks for verification status if user is verified then it sends the
	 * user with
	 * update password link else it send the user with verification link to verify
	 * his identity before reseting his password.
	 */
	@Override
	public UserModel verify(String token) {

		long id = tokenGenerator.parseJwtToken(token);
		UserModel userInfo = repository.findbyId(id);
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
	public UserModel forgetPassword(String email) {
		UserModel isIdAvailable = repository.findEmail(email);
		if (isIdAvailable != null && isIdAvailable.isVerified() == true) {
			String response = "http://localhost:4200/resetpassword/"
					+ tokenGenerator.createToken(isIdAvailable.getId());

			mail.sendForgetPasswordMail(isIdAvailable.getEmail(), response);
			return isIdAvailable;
		}
		return null;
	}

	/**
	 * This function takes reset password information along with valid token as
	 * user input parameter and encode the recent password given by the user and
	 * after successful update of password confirmation message is sent to the
	 * user's mail id.
	 */
	@Override
	public UserModel resetPassword(ResetPasswordDto resetpassword, String token) {
		if (resetpassword.getPassword().equals(resetpassword.getConfirmpassword())) {
			long id = tokenGenerator.parseJwtToken(token);
			UserModel isIdAvailable = repository.findbyId(id);
			if (isIdAvailable != null) {
				isIdAvailable.setPassword(bcryptpasswordEncoder.encode((resetpassword.getPassword())));
				repository.save(isIdAvailable);
				return isIdAvailable;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
