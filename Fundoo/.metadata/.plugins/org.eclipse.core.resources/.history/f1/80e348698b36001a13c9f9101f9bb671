package com.blblz.fundoonotes.serviceimplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blblz.fundoonotes.dto.NoteDto;
import com.blblz.fundoonotes.model.NoteModel;
import com.blblz.fundoonotes.model.UserModel;
import com.blblz.fundoonotes.repository.NoteRepository;
import com.blblz.fundoonotes.repository.UserRepository;
import com.blblz.fundoonotes.service.NoteService;
import com.blblz.fundoonotes.utility.Jwt;

@Service
public class NoteServiceImpl implements NoteService {
	
	@Autowired
	private Jwt tokenGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;

	@Override
	public boolean save(NoteDto noteDto, String token) {
		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(id);
		if (user != null) {
			NoteModel note = new NoteModel(noteDto.getTitle(), noteDto.getContent());
			note.setCreatedBy(user);
			note.setCreatedAt();
			noteRepository.insertData(note.getContent(), note.getCreatedAt(), note.getTitle(), note.getUpdatedAt(),note.getCreatedBy().getId());
			return true;
		}
		return false;
	}

	@Override
	public boolean color(String color, String token, long noteId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
		if (user != null)
		{
			noteRepository.updateColor(color, userId, noteId);
			return true;
		}
		return false;
		
	}

}
