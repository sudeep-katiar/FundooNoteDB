package com.bridgelabz.fundoonotes.serviceimplementation;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.model.LabelModel;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.repository.LabelRepository;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.LabelService;
import com.bridgelabz.fundoonotes.utility.Jwt;

import lombok.extern.slf4j.Slf4j;

/**
 * This class has the implemented functionality of creating label,
 * updating status of label, adding label to note functionality of the user's
 * note after verifying with the identity.
 * 
 * @author Sudeep Kumar Katiar
 * @created 2020-01-16
 * @version 1.0
 */
@Service
@Slf4j
public class LabelServiceImpl implements LabelService {
	
	@Autowired
	private Jwt tokenGenerator;
	
	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private NoteRepository noterepository;
	
	@Autowired
	private LabelRepository labelrepository;
	
	@Autowired
	private RedisTemplate<String, Object> redis;

	@Override
	public int createLabel(LabelDto labeldto, String token) {
		long userId = getRedisCecheId(token);
//		long userId = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> isUserAvailable = userrepository.findById(userId);
		if(isUserAvailable != null)
		{
			String labelname = labeldto.getLabelTitle();
			LabelModel label = labelrepository.findByName(labelname);
			if(label == null)
			{
				return labelrepository.insertLabelData(labeldto.getLabelTitle(), userId);
				
			}
		}
		return 0;
	}

	@Override
	public boolean updateLabel(LabelDto labeldto, String token, long labelId) {
		long userId = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> user = userrepository.findById(userId);
		if(user != null)
		{
			LabelModel label = labelrepository.findById(labelId, userId);
			if(label != null)
			{
				label.setLabelTitle(labeldto.getLabelTitle());
				labelrepository.update(label.getLabelTitle(), labelId);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deleteLabel(String token, long labelId) {
		long userId = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> user = userrepository.findById(userId);
		if(user != null)
		{
			LabelModel label = labelrepository.findById(labelId, userId);
			if(label != null)
			{
				labelrepository.delete(userId, labelId);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<LabelModel> getAllLabel(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> user = userrepository.findById(userId);
		if(user != null)
		{
			List<LabelModel> labeldata = labelrepository.getall(userId);
			return labeldata;
		}
		return null;
	}

	@Override
	public LabelModel createOrMapWithNote(LabelDto labeldto, long noteid, String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> isUserAvailable = userrepository.findById(userId);
		if(isUserAvailable != null)
		{
			NoteModel noteInfo = noterepository.findById(noteid);
			if(noteInfo != null)
			{
				String labelTitle = labeldto.getLabelTitle();
				LabelModel label = labelrepository.findByTitle(labelTitle);
				if(label == null)
				{
					LabelModel newLabel = new LabelModel();
					newLabel.setLabelTitle(labeldto.getLabelTitle());
					labelrepository.insertLabelData(newLabel.getLabelTitle(), userId);
					LabelModel labelCreate = labelrepository.findByName(newLabel.getLabelTitle());
					labelrepository.insertdatatomap(noteid, labelCreate.getLabelId());
					return labelCreate;
				}
				else
				{
					Object map = labelrepository.findoneByLabelIdAndNoteId(label.getLabelId(), noteid);
					if (map == null) {
						labelrepository.insertdatatomap(noteid, label.getLabelId());
					}
					return label;
				}
			}
		}
		return null;
	}

	@Override
	public LabelModel addLabelsToNote(String token, long labelid, long noteid) {
		long userId = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> user = userrepository.findById(userId);
		if(user != null)
		{
			NoteModel note = noterepository.findById(noteid);
			if(note != null)
			{
				LabelModel isLabelAvailable = labelrepository.findById(labelid, userId);
				if(isLabelAvailable != null)
				{
					Object label = labelrepository.findoneByLabelIdAndNoteId(isLabelAvailable.getLabelId(), noteid);
					if(label == null)
					{
						labelrepository.insertdatatomap(noteid, isLabelAvailable.getLabelId());
					}
					return isLabelAvailable;
				}
			}
		}
		return null;
	}
	
	private Long getRedisCecheId(String token) {
		String[] splitedToken = token.split("\\.");
		String redisTokenKey = splitedToken[1] + splitedToken[2];
		if (redis.opsForValue().get(redisTokenKey) == null) {
			Long idForRedis = tokenGenerator.parseJwtToken(token);
			log.info("idForRedis is :" + idForRedis);
			redis.opsForValue().set(redisTokenKey, idForRedis, 3 * 60, TimeUnit.SECONDS);
		}
		Long userId = (Long) redis.opsForValue().get(redisTokenKey);
		return userId;
	}

}
