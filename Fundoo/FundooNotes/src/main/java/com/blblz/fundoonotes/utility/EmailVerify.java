package com.blblz.fundoonotes.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailVerify {

	@Autowired
	private JavaMailSender mailsender;

	public void sendVerifyMail(String email, String token) {
//		System.out.println("email" + email);
		log.info("verification mail");
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("sudeepkatiar@gmail.com");
		mail.setTo(email);
		mail.setSubject("verify user");
		mail.setText("click here..." + token);

		mailsender.send(mail);
	}
	
	public void sendForgetPasswordMail(String email, String token)
	{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("sudeepkatiar@gmail.com");
		mail.setTo(email);
		mail.setSubject("Forget password link");
		mail.setText("click here..." + token);

		mailsender.send(mail);
	}

}
