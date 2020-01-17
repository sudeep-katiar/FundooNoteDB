package com.blblz.fundoonotes.service;

import org.springframework.stereotype.Component;

import com.blblz.fundoonotes.dto.LabelDto;
import com.blblz.fundoonotes.model.LabelModel;

@Component
public interface LabelService {

	int createLabel(LabelDto labeldto, String token);

	LabelModel updateLabel(LabelDto labeldto, String token, long labelId);

//	int createOrMapWithNote(LabelDto labeldto, long noteid, String token);

}
