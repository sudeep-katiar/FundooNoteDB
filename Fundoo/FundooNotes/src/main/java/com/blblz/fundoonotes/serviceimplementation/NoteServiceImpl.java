package com.blblz.fundoonotes.serviceimplementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blblz.fundoonotes.dto.NoteDto;
import com.blblz.fundoonotes.model.LabelModel;
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
	public NoteModel createNote(NoteDto noteDto, String token) {
		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(id);
		if (user != null) {
			NoteModel note = new NoteModel(noteDto.getTitle(), noteDto.getContent());
			note.setCreatedBy(user);
			note.setCreatedAt();
			noteRepository.insertData(note.getContent(), note.getCreatedAt(), note.getTitle(), note.getUpdatedAt(),
					note.getCreatedBy().getId());
			return note;
		}
		return null;
	}

	@Override
	public int deleteNote(String token, long id) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
		if (user != null) {
			NoteModel note = noteRepository.findById(id);
			if (note.isDeleted()) {
//				System.out.println("isdeleted false " + user + " " + id);

				return noteRepository.delete(false, userId, id);
			} else {
//				System.out.println("isdeleted true " + userId + " " + id);
				noteRepository.delete(true, userId, id);
				return 0;
			}
		}
		return -1;
	}

	@Override
	public boolean deleteForever(String token, long id) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
		if (user != null) {
			NoteModel note = noteRepository.findById(id);

			if (note.isDeleted()) {
				noteRepository.deleteForever(userId, id);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean emptybin(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
		if (user != null) {
			noteRepository.empty(userId);
			return true;
		}

		return false;
	}

	@Override
	public boolean updateNote(NoteDto notedto, String token, long id) {
		long userid = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userid);
		if (user != null) {
			NoteModel note = noteRepository.findById(id);
			note.setContent(notedto.getContent());
			note.setTitle(notedto.getTitle());
			note.setUpdatedAt();
			noteRepository.updateData(note.getContent(), note.getTitle(), note.getUpdatedAt(), id, id);
			return true;
		}
		return false;
	}

	@Override
	public int pin(String token, long id) {
		long userid = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userid);
		if (user != null) {
			NoteModel note = noteRepository.findById(id);
			if (note.isPinned()) {
				noteRepository.setPinned(false, userid, id);
				return 1;
			} else if (!note.isPinned()) {
				noteRepository.setArchive(false, userid, id);
				noteRepository.setPinned(true, userid, id);
				return 0;
			} else {
				return -1;
			}
		}
		return 0;
	}

	@Override
	public int archive(String token, long id) {
		long userid = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userid);
		if (user != null) {
			NoteModel note = noteRepository.findById(id);
			if (note.isArchived()) {
				noteRepository.setArchive(false, userid, id);
				return 1;
			} else if (!note.isArchived()) {
				noteRepository.setPinned(false, userid, id);
				noteRepository.setArchive(true, userid, id);
				return 0;
			} else {
				return -1;
			}
		}
		return -1;
	}

	@Override
	public List<NoteModel> getAllNotes(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable != null) {
			List<NoteModel> notes = noteRepository.getAll(userId);
			return notes;
		}
		return null;
	}

	public boolean reminder(String token, Long id, String time) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
		if (user != null) {
			Optional<NoteModel> note = noteRepository.findById(id);
			if (note.isPresent()) {
				if (note.get().getCreatedBy()==user) {
					(note.get()).setReminder(time);
					noteRepository.save(note.get());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean addcolor(String token, long id, String color) {
		try {
			long userid = tokenGenerator.parseJwtToken(token);
			UserModel isUserAvailable = userRepository.findById(userid);
			if (isUserAvailable != null) {
				NoteModel note = noteRepository.findById(id);
				if (note != null) {
					noteRepository.updateColor(userid, id, color);
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<NoteModel> allPinnedNotes(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable != null)
		{
			List<NoteModel> notes = noteRepository.getallpinned(userId);
			return notes;
		}
		return null;
	}

	@Override
	public List<LabelModel> allLabelofOneNote(String token, long id) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
		if(user != null)
		{
			NoteModel note = noteRepository.findById(id);
			if(note != null)
			{
				List<LabelModel> label = noteRepository.findById(id).getLabels();
				return label;
			}
		}
		return null;
	}

}
