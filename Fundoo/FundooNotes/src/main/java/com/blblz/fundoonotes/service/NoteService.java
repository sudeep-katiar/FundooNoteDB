package com.blblz.fundoonotes.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.blblz.fundoonotes.dto.NoteDto;
import com.blblz.fundoonotes.model.LabelModel;
import com.blblz.fundoonotes.model.NoteModel;

@Component
public interface NoteService {

	public NoteModel createNote(NoteDto createdto, String token);

	public int deleteNote(String token, long id);

	public boolean deleteForever(String token, long id);

	public boolean updateNote(NoteDto notedto, String token, long id);

	public int pin(String token, long id);

	public int archive(String token, long id);

	public List<NoteModel> getAllNotes(String token);

	public boolean emptybin(String token);

	public boolean addcolor(String token, long id, String color);

	boolean reminder(String token, Long id, String time);

	public List<NoteModel> allPinnedNotes(String token);

	public List<LabelModel> allLabelofOneNote(String token, long id);

}
