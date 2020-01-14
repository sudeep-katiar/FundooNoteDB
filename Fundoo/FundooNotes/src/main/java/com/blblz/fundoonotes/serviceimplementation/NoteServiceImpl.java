package com.blblz.fundoonotes.serviceimplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blblz.fundoonotes.dto.NoteDto;
import com.blblz.fundoonotes.model.NoteModel;
import com.blblz.fundoonotes.model.UserModel;
import com.blblz.fundoonotes.repository.NoteRepository;
import com.blblz.fundoonotes.repository.UserRepository;
import com.blblz.fundoonotes.responses.Response;
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
	public NoteModel createNote(NoteDto noteDto, String token) {
		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(id);
		if (user != null) {
			NoteModel note = new NoteModel(noteDto.getTitle(), noteDto.getContent());
			note.setCreatedBy(user);
			note.setCreatedAt();
			noteRepository.insertData(note.getContent(), note.getCreatedAt(), note.getTitle(), note.getUpdatedAt(),note.getCreatedBy().getId());
			return note;
		}
		return null;
	}

	@Override
	public int deleteNote(String token, long id) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
		if (user != null)
		{
			NoteModel note = noteRepository.findById(id);
			if(note.isDeleted())
			{
				System.out.println("isdeleted false "+user+" "+ id);
				
				return noteRepository.delete(false, userId, id);
			}
			else
			{
				System.out.println("isdeleted true "+userId+" "+id);
				noteRepository.delete(true, userId, id);
				return 0;
			}
		}
		return -1;
	}

	@Override
	public Response deleteForever(String token, long id) {
		
		return null;
	}

}
