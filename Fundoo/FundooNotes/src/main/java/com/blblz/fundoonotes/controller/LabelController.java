package com.blblz.fundoonotes.controller;

import java.util.List;

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
		boolean result = labelService.updateLabel(labeldto, token, labelId);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is updated", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
	}
	
	@PostMapping("/deletelabel")
	public ResponseEntity<Response> deleteLabel(@RequestParam("token") String token, @RequestParam("labelId") long labelId)
	{
		boolean result = labelService.deleteLabel(token, labelId);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is deleted", 200));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Something went wrong", 400));
	}
	
	@PostMapping("/alllabel")
	public ResponseEntity<Response> getAllLabel(@RequestParam("token") String token)
	{
		List<LabelModel> labelList = labelService.getAllLabel(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "all labels of user", labelList));
	}
	
	@PostMapping("/maptonote")
	public ResponseEntity<Response> mapToNote(@RequestBody LabelDto labeldto,@RequestParam("token") String token, @RequestParam("noteid") Long noteid)
	{
		LabelModel result = labelService.createOrMapWithNote(labeldto, noteid, token);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is mapped to note", 200));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The label you are trying to create is already exist!!!", 400));
	}
	
	@PostMapping("/addLabelsToNote")
	public ResponseEntity<Response> addLabels(@RequestParam("token") String token, @RequestParam("labelid") long labelid, @RequestParam("noteid") long noteid)
	{
		LabelModel result = labelService.addLabelsToNote(token, labelid, noteid);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("label added", 200));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Something went wrong", 400));
	}

}
