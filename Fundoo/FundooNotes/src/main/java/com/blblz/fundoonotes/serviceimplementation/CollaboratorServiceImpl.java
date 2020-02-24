package com.blblz.fundoonotes.serviceimplementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blblz.fundoonotes.dto.CollaboratorDto;
import com.blblz.fundoonotes.model.CollaboratorModel;
import com.blblz.fundoonotes.model.NoteModel;
import com.blblz.fundoonotes.model.UserModel;
import com.blblz.fundoonotes.repository.CollaboratorRepository;
import com.blblz.fundoonotes.repository.NoteRepository;
import com.blblz.fundoonotes.repository.UserRepository;
import com.blblz.fundoonotes.service.CollaboratorService;
import com.blblz.fundoonotes.utility.Jwt;

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
		CollaboratorModel collaborator = new CollaboratorModel();
		collaborator.setEmail(collaboratorDto.getEmail());
		NoteModel note = noteRepository.findById(noteId);
		CollaboratorModel collaboratorDB = collaboratorRepository.findOneByEmail(collaboratorDto.getEmail(), noteId);
		if (note != null && collaboratorDB == null) {
			BeanUtils.copyProperties(collaboratorDto, collaborator);
			collaborator.setNote(note);
			collaboratorRepository.addCollaborator(collaborator.getId(), collaborator.getEmail(), noteId);
			return collaborator;
		}
		return null;
	}


	@Override
	public Optional<CollaboratorModel> deleteCollaborator(Long collaboratorId, String token, Long noteId) {
		long userId = jwtGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
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
