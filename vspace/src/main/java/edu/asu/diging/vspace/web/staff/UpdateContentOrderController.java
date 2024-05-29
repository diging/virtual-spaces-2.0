package edu.asu.diging.vspace.web.staff;

import java.lang.reflect.InaccessibleObjectException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Controller
public class UpdateContentOrderController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @Autowired
    private ISlideManager slideManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/blocks/order/update", method = RequestMethod.POST)
    public ResponseEntity<List<ContentBlock>> adjustContentOrder(@RequestBody String contentBlockString, @PathVariable("id") String slideId) throws BlockDoesNotExistException, JsonMappingException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ISlide slide = slideManager.getSlide(slideId);
        List<ContentBlock> contentBlockList;
        try {
            contentBlockList = objectMapper.readValue(contentBlockString, new TypeReference<List<ContentBlock>>(){});
            contentBlockManager.updateContentOrder(contentBlockList,slide);
        } catch (InaccessibleObjectException e) {
            logger.warn("Error while parsing content blocks.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<ContentBlock>>(contentBlockList, HttpStatus.OK);
    }
}
