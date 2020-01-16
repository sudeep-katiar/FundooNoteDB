package com.blblz.fundoonotes.service;

import java.util.List;

import com.blblz.fundoonotes.dto.NoteDto;
import com.blblz.fundoonotes.model.NoteModel;

public interface NoteService {

	public NoteModel createNote(NoteDto createdto, String token);

	public int deleteNote(String token, long id);

	public boolean deleteForever(String token, long id);

	public boolean updateNote(NoteDto notedto, String token, long id);

	public int pin(String token, long id);

	public int archive(String token, long id);

	public List<NoteModel> getAllNotes(String token);

	public boolean reminder(String token, long id);

}
