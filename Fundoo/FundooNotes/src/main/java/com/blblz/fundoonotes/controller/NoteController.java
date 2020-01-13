package com.blblz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blblz.fundoonotes.dto.NoteDto;
import com.blblz.fundoonotes.responses.Response;
import com.blblz.fundoonotes.service.NoteService;

@RestController
@RequestMapping("/notes")
public class NoteController {
	
	@Autowired
	private NoteService noteService;
	
	@PostMapping("/create")
	private ResponseEntity<Response> createNote(@RequestBody NoteDto noteDto,@RequestHeader String token)
			throws Exception {
		
		boolean result = noteService.save(noteDto,token);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is created successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}
	
	@GetMapping("/color/{id}")
	private ResponseEntity<Response> color(@PathVariable("id") long noteId, @RequestParam String color,
			@RequestHeader("token") String token) {

		boolean result = noteService.color(color, token, noteId);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Color changed succussfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}

	}
	
}
