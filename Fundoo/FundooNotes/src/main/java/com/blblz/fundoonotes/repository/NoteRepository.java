package com.blblz.fundoonotes.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blblz.fundoonotes.model.NoteModel;

@Repository
@Transactional
public interface NoteRepository extends JpaRepository<NoteModel, Long> {

	@Modifying
	@Query(value = "insert into note (content, created_at,  title, updated_at, user_id) values ( :content, :createdAt, :title, :updatedAt, :id)" , nativeQuery = true)
	public void insertData(String content, Date createdAt, String title, Date updatedAt, long id);

	@Modifying
	@Query(value = "update note set note_color = :color where user_id = :userId AND id = :noteId" , nativeQuery = true)
	public void updateColor(String color, long userId, long noteId);
	
}
