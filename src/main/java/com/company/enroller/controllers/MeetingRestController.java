package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meeting")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    // 3.1. Pobieranie listy wszystkich spotkań
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    // 3.2. Pobieranie listy pojedyncznego spotkania
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    // 3.3. Dodawanie spotkań
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
        Meeting foundmeeting = meetingService.findById(meeting.getId());
        if (foundmeeting != null) {
            return new ResponseEntity("Already exist", HttpStatus.CONFLICT);
        }

        meetingService.add(meeting);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 3.4. Usuwanie spotkań
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if (foundMeeting == null) {
            return new ResponseEntity("Not exist", HttpStatus.NOT_FOUND);
        }

        meetingService.delete(foundMeeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 3.5. Aktualizację spotkań
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if (foundMeeting == null) {
            return new ResponseEntity("Not exist2", HttpStatus.NOT_FOUND);
        }

        meetingService.update(meeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
