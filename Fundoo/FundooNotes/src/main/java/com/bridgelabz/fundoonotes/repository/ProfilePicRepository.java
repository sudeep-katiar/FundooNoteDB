package com.bridgelabz.fundoonotes.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.ProfilePic;

@Repository
@Transactional
public interface ProfilePicRepository extends JpaRepository<ProfilePic,Long> {
	
	@Query(value = "select * from profile_pic where user_id=?",nativeQuery = true)
	ProfilePic findByUserId(long user_id);

}
