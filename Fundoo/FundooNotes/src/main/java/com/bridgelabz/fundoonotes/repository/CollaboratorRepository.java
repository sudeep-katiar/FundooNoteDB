package com.bridgelabz.fundoonotes.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.CollaboratorModel;

@Repository
public interface CollaboratorRepository extends JpaRepository<CollaboratorModel, Object> {
	
	@Transactional
	@Query(value = "select * from collaborator where email=? and noteid=?",nativeQuery=true)
	CollaboratorModel findOneByEmail(String email, long noteId);

	@Transactional
	@Modifying
	@Query(value = "insert into collaborator(id,email,noteid)values(?,?,?)",nativeQuery=true)
	void addCollaborator(long id,String email,Long noteid);
	
	@Query(value = "select * from collaborator where id=?",nativeQuery=true)
	CollaboratorModel findById(long id);

	@Transactional
	@Modifying
	@Query(value = "delete from collaborator where id=? and noteid=?",nativeQuery=true)
	void deleteCollaborator(long collaboratorId,long noteId);

	@Query(value = "select * from collaborator where noteid=?", nativeQuery = true)
	List<CollaboratorModel> getAllNoteCollaborators(long noteId);

}
