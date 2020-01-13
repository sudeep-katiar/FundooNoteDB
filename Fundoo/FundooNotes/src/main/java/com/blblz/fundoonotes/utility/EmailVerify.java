package com.blblz.fundoonotes.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailVerify {
	
//	private Logger log = LoggerFactory.getLogger(EmailVerify.class);

	@Autowired
	private JavaMailSender mailsender;
	
//	public static final String ACCOUNT_SID =
//            "ACxxxxxxxxxxxxxxxxxxxxxx";
//    public static final String AUTH_TOKEN =
//            "xxxxxxxxxxxxxxxxxxxxxx";

	public void sendVerifyMail(String email, String token) throws MailException {
//		System.out.println("email" + email);
		log.info("verification mail");
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("sudeepkatiar@gmail.com");
		mail.setTo(email);
		mail.setSubject("verify user");
		mail.setText("click here..." + token);

		mailsender.send(mail);
	}
	
	public void sendForgetPasswordMail(String email, String token) throws MailException
	{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("sudeepkatiar@gmail.com");
		mail.setTo(email);
		mail.setSubject("Forget password link");
		mail.setText("click here..." + token);

		mailsender.send(mail);
	}
	
//	public void sendmsg(String mobile, String token) {
//		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
//	    Message message = Message.creator(new PhoneNumber(mobile), // to
//	                    new PhoneNumber("9040594939"), // from
//	                    "Where's rahul?").create();
//	    message.getSid();
//		}


}
