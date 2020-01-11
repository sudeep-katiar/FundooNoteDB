package com.blblz.fundoonotes.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blblz.fundoonotes.model.UserModel;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserModel, Long> {
	
	@Query(value="Select * from user_model where email = :email",nativeQuery = true)
	UserModel findEmail(String email);
	
	@Query(value = "select * from user_model where id = :id", nativeQuery = true)
	UserModel findById(long id);

	@Modifying
	@Query(value="Insert into user_model (creator_stamp,email,firstname,is_verified,lastname,mobile,password,username) values (:creator_stamp,:email,:firstname,:is_verified,:lastname,:mobile,:password,:username)",nativeQuery = true)
	void insertdata(Date creator_stamp,String email,String firstname,boolean is_verified,String lastname,String mobile,String password,String username);

	@Modifying
	@Query(value="update user_model set is_verified = true where id = :id", nativeQuery = true)
	void verify(long id);
	
}
