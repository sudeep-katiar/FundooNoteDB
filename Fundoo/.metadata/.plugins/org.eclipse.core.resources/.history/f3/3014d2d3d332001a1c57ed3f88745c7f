package com.blblz.fundoonotes.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blblz.fundoonotes.model.UserModel;
import com.blblz.fundoonotes.responses.Response;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserModel, Long> {
	
	@Query(value="Select * from user_model where email = :email",nativeQuery = true)
	UserModel findEmail(String email);

	@Modifying
	@Query(value="Insert into user_model (creator_stamp,email,firstname,is_verified,lastname,mobile,password,username) values (:creator_stamp,:email,:firstname,:is_verified,:lastname,:mobile,:password,:username)",nativeQuery = true)
	Response insertdata(Date creator_stamp,String email,String firstname,boolean is_verified,String lastname,String mobile,String password,String username);
	
}
