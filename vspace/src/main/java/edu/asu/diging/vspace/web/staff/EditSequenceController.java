package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.services.ISequenceManager;

@Controller
public class EditSequenceController {

    @Autowired
    private ISequenceManager sequenceManager;

    @RequestMapping(value = "/staff/module/{moduleId}/sequence/{sequenceId}/edit/description", method = RequestMethod.POST)
    public ResponseEntity<String> saveDescription(@RequestParam("description") String description,
            @PathVariable("moduleId") String moduleId, @PathVariable("sequenceId") String sequenceId) {
        ISequence sequence = sequenceManager.getSequence(sequenceId);
        sequence.setDescription(description);
        sequenceManager.updateSequence((Sequence) sequence);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/module/{moduleId}/sequence/{sequenceId}/edit/title", method = RequestMethod.POST)
    public ResponseEntity<String> saveTitle(@RequestParam("title") String title,
            @PathVariable("moduleId") String moduleId, @PathVariable("sequenceId") String sequenceId) {
        ISequence sequence = sequenceManager.getSequence(sequenceId);
        sequence.setName(title);
        sequenceManager.updateSequence((Sequence) sequence);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
