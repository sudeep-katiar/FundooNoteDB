package com.bridgelabz.fundoonotes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.dto.CollaboratorDto;
import com.bridgelabz.fundoonotes.model.CollaboratorModel;

@Component
public interface CollaboratorService {

	CollaboratorModel addCollaborator(CollaboratorDto collaboratorDto, String token, long noteId);

	Optional<CollaboratorModel> deleteCollaborator(Long collaboratorId, String token, Long noteId);

	List<CollaboratorModel> getNoteCollaborators(String token, Long noteId);

}
