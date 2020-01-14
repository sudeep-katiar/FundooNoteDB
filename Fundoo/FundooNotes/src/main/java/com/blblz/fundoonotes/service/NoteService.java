package com.blblz.fundoonotes.service;

import com.blblz.fundoonotes.dto.NoteDto;
import com.blblz.fundoonotes.model.NoteModel;
import com.blblz.fundoonotes.responses.Response;

public interface NoteService {

	public NoteModel createNote(NoteDto createdto, String token);

	public int deleteNote(String token, long id);

	public Response deleteForever(String token, long id);

}
