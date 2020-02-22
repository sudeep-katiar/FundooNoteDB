package com.blblz.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	private ResponseEntity<Response> createNote(@RequestBody NoteDto notedto, @RequestHeader("token") String token)
			throws Exception {

		NoteModel note = noteservice.createNote(notedto, token);

		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Note is created successfully", note));

	}

	@PostMapping("/deleteORrestore/{id}")
	public ResponseEntity<Response> deleteNote(@RequestHeader("token") String token, @PathVariable("id") long id) {

		int result = noteservice.deleteNote(token, id);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("successfully restored", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("successfully deleted", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}

	@PostMapping("/deleteforever/{id}")
	public ResponseEntity<Response> deleteNoteForever(@RequestHeader("token") String token,
			@PathVariable("id") long id) {

		boolean result = noteservice.deleteForever(token, id);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully deleted", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response("Something went wrong can't delete", 400));
		}
	}
	
	@PostMapping("/emptybin")
	public ResponseEntity<Response> emptyBin(@RequestHeader("token") String token)
	{
		boolean result = noteservice.emptybin(token);
		if(result)
		{
			return ResponseEntity.status(HttpStatus.OK).body(new Response("successfully deleted", 200));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Response("Something went wrong can't delete", 400));
	}

	@PostMapping("/updatenote/{id}")
	public ResponseEntity<Response> updateNote(@RequestBody NoteDto notedto, @RequestHeader("token") String token,
			@PathVariable("id") long id) {
		boolean result = noteservice.updateNote(notedto, token, id);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is update successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}

	@PostMapping("/pinunpin/{id}")
	public ResponseEntity<Response> pin(@RequestHeader("token") String token, @PathVariable("id") long id) {
		int result = noteservice.pin(token, id);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully unPinned", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully Pinned", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}

	@PostMapping("/archive/{id}")
	private ResponseEntity<Response> archive(@PathVariable("id") long id, @RequestHeader("token") String token) {

		int result = noteservice.archive(token, id);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully removed from Archive", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully Archived", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}
	
	@PostMapping("/allnotes/")
	public ResponseEntity<Response> getAllNotes(@RequestHeader("token") String token)  {
		List<NoteModel> notesList = noteservice.getAllNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "all notes of user", notesList));
	}
	
	@PostMapping("/addcolor/{id}")
	public ResponseEntity<Response> addColor(@RequestHeader("token") String token, @PathVariable("id") long id,@RequestParam("color") String color)
	{
		boolean result = noteservice.addcolor(token, id, color);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("color is added", 200));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Something went wrong...", 400));
	}
	
	@PostMapping("/reminder/{id}")
	public ResponseEntity<Response> reminder(@RequestHeader("token") String token, @PathVariable("id") long id,@RequestParam("time")String time)
	{
		boolean result = noteservice.reminder(token, id, time);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200,"reminder added", result));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("something went wrong", 400));
	}
	
	public ResponseEntity<Response> collaborator()
	{
		
		return null;
	}
	
	public ResponseEntity<Response> sort()
	{
//		boolean result = noteservice.sort();
//		if(result)
			return null;
	}

}
