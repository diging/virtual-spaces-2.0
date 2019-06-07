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

import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class EditTextBlockController {

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/textcontent/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editTextBlock(@PathVariable("id") String slideId,
            @RequestParam("textBlockId") String blockId, @PathVariable("moduleId") String moduleId,
            @RequestParam("textBlockDesc") String textBlockDesc) throws IOException {

        ITextBlock textBlock = contentBlockManager.getTextBlock(blockId);
        textBlock.setText(textBlockDesc);
        contentBlockManager.updateTextBlock((TextBlock) textBlock);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}