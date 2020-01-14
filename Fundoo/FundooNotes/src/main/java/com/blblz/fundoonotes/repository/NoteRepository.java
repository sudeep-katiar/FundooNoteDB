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
	
	@Query(value = "select * from note where id = :id", nativeQuery = true)
	NoteModel findById(long id);

	@Modifying
	@Query(value = "insert into note (content, created_at,  title, updated_at, user_id) values ( :content, :createdAt, :title, :updatedAt, :id)" , nativeQuery = true)
	public void insertData(String content, Date createdAt, String title, Date updatedAt, long id);

	@Modifying
	@Query(value = "update note set is_deleted = :b where user_id = :userid AND id = :id" ,  nativeQuery = true)
	public int delete(boolean b,long userid,long id);

	
	
}
