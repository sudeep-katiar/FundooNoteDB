package com.blblz.fundoonotes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.blblz.fundoonotes.serviceimplementation.UserServiceImpl;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public UserServiceImpl getUserService()
	{
		return new UserServiceImpl();
	}
	
}
