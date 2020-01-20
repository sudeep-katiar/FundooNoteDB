package com.blblz.fundoonotes.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.blblz.fundoonotes.dto.LabelDto;
import com.blblz.fundoonotes.model.LabelModel;

@Component
public interface LabelService {

	int createLabel(LabelDto labeldto, String token);

	boolean updateLabel(LabelDto labeldto, String token, long labelId);

	boolean deleteLabel(String token, long labelId);

	List<LabelModel> getAllLabel(String token);

	LabelModel createOrMapWithNote(LabelDto labeldto, long noteid, String token);

	LabelModel addLabelsToNote(String token, long labelid, long noteid);

}
