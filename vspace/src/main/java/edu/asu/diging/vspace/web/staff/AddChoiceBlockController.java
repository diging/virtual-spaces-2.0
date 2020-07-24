package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/choice/content", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> addChoiceBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId,
            @RequestParam("contentOrder") Integer contentOrder, @RequestParam("selectedChoices") List<String> selectedChoices,
            @RequestParam("showsAll") boolean showsAll) throws IOException {

        IChoiceBlock choiceBlock = contentBlockManager.createChoiceBlock(slideId, selectedChoices, contentOrder, showsAll);
        /*After annotating sequence attribute with JsonIgnore in Choice model to fix stack overflow issue 
         * the sequences are not returned as part of choiceBlock object, hence sequences are explicitly passed 
         * as part of Response in a list form*/
        Map<String,Object> responseData = new HashMap<String,Object>();
        responseData.put("choiceBlock",choiceBlock);
        if(!choiceBlock.isShowsAll()) {
            List<ISequence> selectedSequences = choiceBlock.getChoices().stream().map(choice->choice.getSequence()).collect(Collectors.toList());
            responseData.put("selectedSequences",selectedSequences);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
