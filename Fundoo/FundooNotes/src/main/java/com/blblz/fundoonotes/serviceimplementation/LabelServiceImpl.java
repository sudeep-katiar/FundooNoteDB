package com.blblz.fundoonotes.serviceimplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blblz.fundoonotes.dto.LabelDto;
import com.blblz.fundoonotes.model.LabelModel;
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
	public LabelModel updateLabel(LabelDto labeldto, String token, long labelId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userrepository.findById(userId);
		if(user != null)
		{
			LabelModel isLabelAvailable = labelrepository.findById(labelId, userId);
			if(isLabelAvailable != null)
			{
				labelrepository.update(isLabelAvailable.getLabelTitle(), labelId);
				return isLabelAvailable;
			}
		}
		return null;
	}

//	@Override
//	public int createOrMapWithNote(LabelDto labeldto, long noteid, String token) {
//		long userId = tokenGenerator.parseJwtToken(token);
//		UserModel isUserAvailable = userrepository.findById(userId);
//		if(isUserAvailable != null)
//		{
//			NoteModel noteInfo = noterepository.findById(noteid);
//			if(noteInfo != null)
//			{
//				String labelTitle = labeldto.getLabelTitle();
//				LabelModel label = labelrepository.findByTitle(labelTitle);
//				if(label == null)
//				{
//					
//				}
//			}
//		}
//		return 0;
//	}

}
