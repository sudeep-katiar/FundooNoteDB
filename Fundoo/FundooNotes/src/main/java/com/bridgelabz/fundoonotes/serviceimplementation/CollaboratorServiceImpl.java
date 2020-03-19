package com.bridgelabz.fundoonotes.serviceimplementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.CollaboratorDto;
import com.bridgelabz.fundoonotes.model.CollaboratorModel;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.repository.CollaboratorRepository;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.CollaboratorService;
import com.bridgelabz.fundoonotes.utility.Jwt;

/**
 * This class has the implemented functionality of adding collaborator,
 * updating status of collaborator, adding collaborator to note functionality of the user's
 * note after verifying with the identity.
 * 
 * @author Sudeep Kumar Katiar
 * @created 2020-01-24
 * @version 1.0
 */
@Service
public class CollaboratorServiceImpl implements CollaboratorService {

	@Autowired
	private Jwt jwtGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private CollaboratorRepository collaboratorRepository;

	@Override
	public CollaboratorModel addCollaborator(CollaboratorDto collaboratorDto, String token, long noteId) {
		long userId = jwtGenerator.parseJwtToken(token);
		Optional<UserModel> user = userRepository.findById(userId);
		if (user != null) {
			CollaboratorModel collaborator = new CollaboratorModel();
			collaborator.setEmail(collaboratorDto.getEmail());
			NoteModel note = noteRepository.findById(noteId);
			CollaboratorModel collaboratorDB = collaboratorRepository.findOneByEmail(collaboratorDto.getEmail(),
					noteId);
			if (note != null && collaboratorDB == null) {
				BeanUtils.copyProperties(collaboratorDto, collaborator);
				collaborator.setNote(note);
				collaboratorRepository.addCollaborator(collaborator.getId(), collaborator.getEmail(), noteId);
				return collaborator;
			}
		}
		return null;
	}

	@Override
	public Optional<CollaboratorModel> deleteCollaborator(Long collaboratorId, String token, Long noteId) {
		long userId = jwtGenerator.parseJwtToken(token);
		Optional<UserModel> user = userRepository.findById(userId);
		if (user != null) {
			Optional<NoteModel> note = noteRepository.findById(noteId);
			if (note != null) {
				Optional<CollaboratorModel> collaborator = collaboratorRepository.findById(collaboratorId);
				if (collaborator != null) {
					collaboratorRepository.deleteCollaborator(collaboratorId, noteId);
					return collaborator;
				}
			}
		}
		return null;
	}

	@Override
	public List<CollaboratorModel> getNoteCollaborators(String token, Long noteId) {
		long userId = jwtGenerator.parseJwtToken(token);
		if (userId != 0) {
			List<NoteModel> note = noteRepository.searchAllNotesByNoteId(userId, noteId);
			if (note != null) {
				return collaboratorRepository.getAllNoteCollaborators(noteId);
			}
		}
		return null;
	}

}
