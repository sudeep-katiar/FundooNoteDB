package com.blblz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blblz.fundoonotes.dto.LabelDto;
import com.blblz.fundoonotes.model.LabelModel;
import com.blblz.fundoonotes.responses.Response;
import com.blblz.fundoonotes.service.LabelService;

@RestController
@RequestMapping("/label")
public class LabelController {

	@Autowired
	private LabelService labelService;

	@PostMapping("/addlabel")
	public ResponseEntity<Response> createLabel(@RequestBody LabelDto labeldto, @RequestParam("token") String token) {
		int result = labelService.createLabel(labeldto, token);
		if (result != 0)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is Created", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Already exist in label ", 400));
	}
	
	@PostMapping("/updatelabel")
	public ResponseEntity<Response> updateLabel(@RequestBody LabelDto labeldto, @RequestParam("token") String token, @RequestParam("labelId") long labelId)
	{
		LabelModel result = labelService.updateLabel(labeldto, token, labelId);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is Created", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Already exist in label ", 400));
	}
	
//	public ResponseEntity<Response> mapToNote(@RequestBody LabelDto labeldto, @RequestParam("noteid") Long noteid, @RequestParam("token") String token)
//	{
//		int result = labelservice.createOrMapWithNote(labeldto, noteid, token);
//		if(result != 0)
//			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is mapped to note", 200));
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The label you are trying to create is already exist!!!", 400));
//	}

}