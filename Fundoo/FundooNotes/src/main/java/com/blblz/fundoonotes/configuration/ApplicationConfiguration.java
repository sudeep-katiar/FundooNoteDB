package com.blblz.fundoonotes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.blblz.fundoonotes.serviceimplementation.NoteServiceImpl;
import com.blblz.fundoonotes.serviceimplementation.UserServiceImpl;
import com.blblz.fundoonotes.utility.EmailVerify;

@Configuration
public class ApplicationConfiguration {
	
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
}
