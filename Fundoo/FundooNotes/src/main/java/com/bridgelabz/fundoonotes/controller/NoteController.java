package com.bridgelabz.fundoonotes.controller;

import java.util.List;

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

import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.model.LabelModel;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.NoteService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/notes")
public class NoteController {

	@Autowired
	private NoteService noteservice;

	/*
	 * API to create notes
	 */
	@PostMapping("/create")
	@ApiOperation(value = "Api to create note for Fundoonotes", response = Response.class)
	private ResponseEntity<Response> createNote(@RequestBody NoteDto notedto, @RequestHeader("token") String token)
			throws Exception {

		NoteModel note = noteservice.createNote(notedto, token);

		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Note is created successfully", note));

	}

	/*
	 * API to delete or restore notes
	 */
	@PostMapping("/deleteORrestore/{id}")
	@ApiOperation(value = "Api to delete or restore note for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> deleteNote(@RequestHeader("token") String token, @PathVariable("id") long id) {

		int result = noteservice.trashNote(token, id);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("successfully restored", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("successfully deleted", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}
	
	/*
	 * API to get all trashed notes
	 */
	@GetMapping("/alltrashednotes")
	@ApiOperation(value = "Api to get all trashed notes for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> getAllTrashedNotes(@RequestHeader("token") String token) {
		List<NoteModel> notesList = noteservice.allTrashedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "all trashed notes of user", notesList));
	}

	/*
	 * API to delete notes forever
	 */
	@PostMapping("/deleteforever/{id}")
	@ApiOperation(value = "Api to delete note forever for Fundoonotes", response = Response.class)
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
	
	/*
	 * API to empty bin
	 */
	@PostMapping("/emptybin")
	@ApiOperation(value = "Api to empty bin for Fundoonotes", response = Response.class)
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

	/*
	 * API to update notes
	 */
	@PostMapping("/updatenote/{id}")
	@ApiOperation(value = "Api to update note for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> updateNote(@RequestBody NoteDto notedto, @RequestHeader("token") String token,
			@PathVariable("id") long id) {
		boolean result = noteservice.updateNote(notedto, token, id);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is update successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}

	/*
	 * API to pin and unpin notes
	 */
	@PostMapping("/pinunpin")
	@ApiOperation(value = "Api to pin or unpin note for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> pin(@RequestHeader("token") String token, @RequestParam("id") long id) {
		int result = noteservice.pin(token, id);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully unPinned", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully Pinned", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}
	
	/*
	 * API to get all pinned notes
	 */
	@GetMapping("/allpinnednotes")
	@ApiOperation(value = "Api to get all pinned note for Fundoonotes", response = Response.class)
	private ResponseEntity<Response> getPinnedNotes(@RequestHeader("token") String token) {
		
		List<NoteModel> notesList = noteservice.allPinnedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "all pinned notes of user", notesList));
	}
	
	/*
	 * API to get all unpinned notes
	 */
	@GetMapping("/allunpinnednotes")
	@ApiOperation(value = "Api to get all unpinned note for Fundoonotes", response = Response.class)
	private ResponseEntity<Response> getAllUnpinnedNotes(@RequestHeader("token") String token) {
		
		List<NoteModel> notesList = noteservice.allUnpinnedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "all unpinned notes of user", notesList));
	}

	/*
	 * API to archive notes
	 */
	@PostMapping("/archive/{id}")
	@ApiOperation(value = "Api to archive note for Fundoonotes", response = Response.class)
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
	
	/*
	 * API to get all archived notes
	 */
	@GetMapping("/getallarchived")
	@ApiOperation(value = "Api to get all archived note for Fundoonotes", response = Response.class)
	private ResponseEntity<Response> getallarchived(@RequestHeader("token") String token) {
		List<NoteModel> notesList = noteservice.allArchived(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "all archived notes of user", notesList));
	}
	
	/*
	 * API to get all unarchived notes
	 */
	@GetMapping("/getallunarchived")
	@ApiOperation(value = "Api to get all unarchived note for Fundoonotes", response = Response.class)
	private ResponseEntity<Response> getallunarchived(@RequestHeader("token") String token) {
		List<NoteModel> notesList = noteservice.allUnarchived(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "all unarchived notes of user", notesList));
	}
	
	/*
	 * API to get all notes
	 */
	@PostMapping("/allnotes")
	@ApiOperation(value = "Api to get all note for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> getAllNotes(@RequestHeader("token") String token)  {
		List<NoteModel> notesList = noteservice.getAllNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "all notes of user", notesList));
	}
	
	/*
	 * API to add color to notes
	 */
	@PostMapping("/addcolor/{id}")
	@ApiOperation(value = "Api to add color to note for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> addColor(@RequestHeader("token") String token, @PathVariable("id") long id,@RequestParam("color") String color)
	{
		boolean result = noteservice.addcolor(token, id, color);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("color is added", 200));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Something went wrong...", 400));
	}
	
	/*
	 * API to add reminder to notes
	 */
	@PostMapping("/reminder/{id}")
	@ApiOperation(value = "Api to add reminder to note for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> reminder(@RequestHeader("token") String token, @PathVariable("id") long id,@RequestParam("time")String time)
	{
		boolean result = noteservice.reminder(token, id, time);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200,"reminder added", result));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("something went wrong", 400));
	}
	
	@GetMapping("/getallreminder")
	@ApiOperation(value = "Api to get all reminder notes for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> getAllReminder(@RequestHeader("token") String token) {
		List<NoteModel> notesList = noteservice.allReminderNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "all notes of user", notesList));
		
	}
	
	/*
	 * API to get all label of a note
	 */
	@GetMapping("/getnotelabels")
	@ApiOperation(value = "Api to get add label of one note for Fundoonotes", response = Response.class)
	public ResponseEntity<Response> getAllNoteLabels(@RequestHeader("token") String token,
			@RequestParam("noteId") long noteId) {
		List<LabelModel> noteList = noteservice.allLabelofOneNote(token, noteId);
		return ResponseEntity.status(HttpStatus.OK).body(new Response( 200,"Labels related to current Note are", noteList));
	}
	
	public ResponseEntity<Response> sort()
	{
//		boolean result = noteservice.sort();
//		if(result)
			return null;
	}

}
