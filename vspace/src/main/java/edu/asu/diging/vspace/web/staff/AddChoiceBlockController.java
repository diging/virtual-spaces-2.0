package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class AddChoiceBlockController {

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/choicecontent", method = RequestMethod.POST)
    public ResponseEntity<ISequence> addChoiceBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @RequestParam("content") String choiceContent,
            @RequestParam("contentOrder") Integer contentOrder) throws IOException {

        IChoiceBlock choiceBlock = contentBlockManager.createChoiceBlock(slideId, choiceContent, contentOrder);

        return new ResponseEntity<>(choiceBlock.getSequence(), HttpStatus.OK);
    }
}
