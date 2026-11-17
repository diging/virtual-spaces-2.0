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
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.ILanguageService;

@Controller
public class AddTextBlockController {

    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @Autowired
    private ILanguageService languageService;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/textcontent", method = RequestMethod.POST)
    public ResponseEntity<String> addTextBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, 
            @RequestParam("content") String content,
            @RequestParam(value = "languageCode", required = false) String languageCode) throws IOException {

        Integer contentOrder = contentBlockManager.findMaxContentOrder(slideId);
        contentOrder = contentOrder == null ? 0 : contentOrder + 1;
        
        if (languageCode == null || languageCode.isEmpty()) {
            languageCode = languageService.getDefaultLanguageCode();
        }
        
        ITextBlock textBlock = contentBlockManager.createTextBlockWithLanguage(slideId, content, contentOrder, languageCode);

        return new ResponseEntity<>(textBlock.getId(), HttpStatus.OK);
    }

}