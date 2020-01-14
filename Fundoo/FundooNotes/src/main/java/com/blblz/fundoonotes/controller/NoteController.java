package com.blblz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blblz.fundoonotes.dto.NoteDto;
import com.blblz.fundoonotes.model.NoteModel;
import com.blblz.fundoonotes.responses.Response;
import com.blblz.fundoonotes.service.NoteService;

@RestController
@RequestMapping("/notes")
public class NoteController {
	
	@Autowired
	private NoteService noteservice;
	
	@PostMapping("/create")
	private ResponseEntity<Response> createNote(@RequestBody NoteDto notedto,@RequestParam String token) throws Exception {
		
		NoteModel note = noteservice.createNote(notedto, token);
		
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Note is created successfully", note));
		
	}
	
	@PostMapping("/deleteORrestore")
	public ResponseEntity<Response> deleteNote(@RequestBody @RequestParam String token, @RequestParam long id) {
		
		int result = noteservice.deleteNote(token,id);
		if (result == 1)
		{
			return ResponseEntity.status(HttpStatus.OK).body(new Response("successfully restored", 200));
		}
		else if (result == 0)
		{
			return ResponseEntity.status(HttpStatus.OK).body(new Response("successfully deleted", 200));
		} 
		else 
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}
	
	public ResponseEntity<Response> deleteNoteForever(@RequestBody @RequestParam("id") long id, @RequestParam("token") String token)
	{
		
		return new ResponseEntity<Response>(noteservice.deleteForever(token, id), HttpStatus.OK);
	}
	
}
