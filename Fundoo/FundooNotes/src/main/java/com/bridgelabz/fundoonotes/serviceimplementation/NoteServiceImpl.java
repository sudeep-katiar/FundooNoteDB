package com.bridgelabz.fundoonotes.serviceimplementation;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.model.LabelModel;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.NoteService;
import com.bridgelabz.fundoonotes.utility.Jwt;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements {@link NoteService} interface which has the
 * unimplemented functionality of creating a note, updating, deleting and all.
 * All operations will be carried out if the user is a valid user.
 * 
 * @author Durgasankar Mishra
 * @created 2020-01-10
 * @version 1.0
 * @modified -> optimized code for authentication of user and validation of note
 */
@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

	@Autowired
	private Jwt tokenGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;
	
	@Autowired
	private RedisTemplate<String, Object> redis;

	/**
	 * This function takes {@link NoteDto} as input parameter and token as path
	 * variable. Using token it authorize the user if the user is verified then all
	 * data of noteDto is copied to the note class and creation dateTime and color
	 * information is saved then the user note information is saved in the database.
	 * After successful saving of note it return boolean value.
	 */
	@Override
	public NoteModel createNote(NoteDto noteDto, String token) {
		long id = getRedisCecheId(token);
//		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findbyId(id);
		if (user != null) {
			NoteModel note = new NoteModel(noteDto.getTitle(), noteDto.getContent(),noteDto.isPinned(),
					noteDto.isArchived(), noteDto.isDeleted(),noteDto.getColor(),noteDto.getReminder(),noteDto.isReminderStatus());
			note.setCreatedBy(user);
			note.setCreatedAt();
			note.setUpdatedAt();
			noteRepository.insertData(note.getContent(), note.getCreatedAt(), note.getTitle(), note.getUpdatedAt(),
					note.getCreatedBy().getId(), note.getNoteColor(), note.isArchived(), note.isDeleted(), note.isPinned(), note.getReminder(), note.getReminderStatus());
			return note;
		}
		return null;
	}

	/**
	 * This function takes note id and authorized token from the user checks for
	 * user authorization if valid customer then find for the available of note on
	 * the database. if found valid note then it change the status of trashed on
	 * database. if the note is trashed already then it return false. on Successful
	 * change of trashed status of note it return boolean value.
	 */
	@Override
	public int trashNote(String token, long id) {
		long userId = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> user = userRepository.findById(userId);
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
		Optional<UserModel> user = userRepository.findById(userId);
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
		Optional<UserModel> user = userRepository.findById(userId);
		if (user != null) {
			noteRepository.empty(userId);
			return true;
		}

		return false;
	}

	@Override
	public boolean updateNote(NoteDto notedto, String token, long id) {
		long userid = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> user = userRepository.findById(userid);
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

	/**
	 * This function takes note id and authorized token from the user checks for
	 * user authorization if valid customer then find for the available of note on
	 * the database. if found valid note then it change the status of pinned on
	 * database. on Successful change of pinned status of note it return boolean
	 * value. if already pinned it will unPin it on second request.
	 */
	@Override
	public int pin(String token, long id) {
		long userid = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> user = userRepository.findById(userid);
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

	/**
	 * This function takes note id and authorized token from the user checks for
	 * user authorization if valid customer then find for the available of note on
	 * the database. if found valid note then it change the status of archived on
	 * database. if the note is archived already then make it as unArchived note and
	 * after Successful change of archived status of note it return boolean value.
	 */
	@Override
	public int archive(String token, long id) {
		long userid = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> user = userRepository.findById(userid);
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

	/**
	 * This function takes note id and authorized token from the user checks for
	 * user authorization if valid customer then find all the available notes which
	 * are not trashed from database and return it.
	 */
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

	/**
	 * This function takes note id, {@link NoteDto} and authorized token from
	 * the user checks for user authentication if valid customer found then it
	 * checks whether remainder is set before or not if not set it set the remainder
	 * for note and add the update time then add to database.
	 */
	public boolean reminder(String token, Long id, String time) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findbyId(userId);
		if (user != null) {
			Optional<NoteModel> note = noteRepository.findById(id);
			if (note.isPresent()) {
				if (note.get().getCreatedBy()==user) {
					(note.get()).setReminder(time);
					(note.get()).setReminderStatus(true);
					noteRepository.save(note.get());
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This function takes note id and authorized token and note color from the user
	 * checks for user authentication if valid customer found then it set the color
	 * of the valid fetched note and also record the update time then save to
	 * database.
	 */
	@Override
	public boolean addcolor(String token, long id, String color) {
		try {
			long userid = tokenGenerator.parseJwtToken(token);
			Optional<UserModel> isUserAvailable = userRepository.findById(userid);
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

	/**
	 * This function takes note id and authorized token from the user checks for
	 * user authorization if valid customer then find all the available pinned notes
	 * from database and return it.
	 */
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
		Optional<UserModel> user = userRepository.findById(userId);
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

	/**
	 * This function takes note id and authorized token from the user checks for
	 * user authorization if valid customer then find all the available unpinned notes
	 * from database and return it.
	 */
	@Override
	public List<NoteModel> allUnpinnedNotes(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable != null)
		{
			List<NoteModel> notes = noteRepository.getallunpinned(userId);
			return notes;
		}
		return null;
	}

	/**
	 * This function takes note id and authorized token from the user checks for
	 * user authorization if valid customer then find all the available archived
	 * notes from database and return it.
	 */
	@Override
	public List<NoteModel> allArchived(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable != null)
		{
			List<NoteModel> notes = noteRepository.getallarchived(userId);
			return notes;
		}
		return null;
	}

	@Override
	public List<NoteModel> allUnarchived(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable != null)
		{
			List<NoteModel> notes = noteRepository.getallunarchived(userId);
			return notes;
		}
		return null;
	}

	/**
	 * This function takes note id and authorized token from the user checks for
	 * user authorization if valid customer then find all the available trashed
	 * notes from database and return it.
	 */
	@Override
	public List<NoteModel> allTrashedNotes(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable != null)
		{
			List<NoteModel> notes = noteRepository.getalltrashed(userId);
			return notes;
		}
		return null;
	}

	@Override
	public List<NoteModel> allReminderNotes(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable != null) {
			List<NoteModel> notes = noteRepository.getallreminder(userId);
			return notes;
		}
		return null;
	}
	
	private Long getRedisCecheId(String token) {
		String[] splitedToken = token.split("\\.");
		String redisTokenKey = splitedToken[1] + splitedToken[2];
		if (redis.opsForValue().get(redisTokenKey) == null) {
			Long idForRedis =tokenGenerator.parseJwtToken(token);
			log.info("idForRedis is :" + idForRedis);
			redis.opsForValue().set(redisTokenKey, idForRedis, 3 * 60, TimeUnit.SECONDS);
		}
		Long userId =  (Long) redis.opsForValue().get(redisTokenKey);
		return userId;
	}

}
