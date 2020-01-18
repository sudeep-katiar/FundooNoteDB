package com.blblz.fundoonotes.serviceimplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blblz.fundoonotes.dto.LabelDto;
import com.blblz.fundoonotes.model.LabelModel;
import com.blblz.fundoonotes.model.NoteModel;
import com.blblz.fundoonotes.model.UserModel;
import com.blblz.fundoonotes.repository.LabelRepository;
import com.blblz.fundoonotes.repository.NoteRepository;
import com.blblz.fundoonotes.repository.UserRepository;
import com.blblz.fundoonotes.service.LabelService;
import com.blblz.fundoonotes.utility.Jwt;

@Service
public class LabelServiceImpl implements LabelService {
	
	@Autowired
	private Jwt tokenGenerator;
	
	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private NoteRepository noterepository;
	
	@Autowired
	private LabelRepository labelrepository;

	@Override
	public int createLabel(LabelDto labeldto, String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userrepository.findById(userId);
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
		UserModel user = userrepository.findById(userId);
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
		UserModel user = userrepository.findById(userId);
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
		UserModel user = userrepository.findById(userId);
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
		UserModel isUserAvailable = userrepository.findById(userId);
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

}
