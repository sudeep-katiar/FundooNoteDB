package com.bridgelabz.fundoonotes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.dto.CollaboratorDto;
import com.bridgelabz.fundoonotes.model.CollaboratorModel;

/**
 * This interface has the unimplemented functionality of adding collaborator,
 * updating status of collaborator, adding collaborator to note functionality of the user's
 * note after verifying with the identity.
 * 
 * @author Sudeep Kumar Katiar
 * @created 2020-01-24
 * @version 1.0
 */
@Component
public interface CollaboratorService {

	CollaboratorModel addCollaborator(CollaboratorDto collaboratorDto, String token, long noteId);

	Optional<CollaboratorModel> deleteCollaborator(Long collaboratorId, String token, Long noteId);

	List<CollaboratorModel> getNoteCollaborators(String token, Long noteId);

}
