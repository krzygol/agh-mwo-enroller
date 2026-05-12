package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@Autowired
	PasswordEncoder passwordEncoder;

	// Get all participants
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants() {
		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	// 1.1. Find participant by login
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	// 1.2. Add new participant
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		// 1.3. Check if participant already exist
		if (foundParticipant != null) {
			return new ResponseEntity("Unable to create. A participant with login "
					+ participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
		}

		String hashedPassword = passwordEncoder.encode(participant.getPassword());
		participant.setPassword(hashedPassword);

		participantService.add(participant);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// 2.1.1. Remove participant
	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@RequestBody Participant participant) {
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (foundParticipant == null) {
			return new ResponseEntity("Not exist", HttpStatus.NOT_FOUND);
		}

		participantService.delete(foundParticipant);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 2.1.2. Remove participant
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@RequestBody Participant participant) {
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (foundParticipant == null) {
			return new ResponseEntity("Not exist2", HttpStatus.NOT_FOUND);
		}

		participantService.update(participant);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 2.2. Sort participants
	@RequestMapping(value = "", method = RequestMethod.GET, params = {"sortBy", "sortOrder"})
	public ResponseEntity<?> getParticipantsSorted(
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortOrder) {

		Collection<Participant> participants =
				participantService.getAll(sortBy, sortOrder);

		return ResponseEntity.ok(participants);
	}

	// 2.3. Filter parameter
	@RequestMapping(value = "", method = RequestMethod.GET, params = {"key"})
	public ResponseEntity<?> getParticipantsFilter(
			@RequestParam(required = false) String key) {

		Collection<Participant> participants =
				participantService.getAll(key);

		return ResponseEntity.ok(participants);
	}
}
