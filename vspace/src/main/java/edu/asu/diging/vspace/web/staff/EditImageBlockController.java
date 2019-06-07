package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class EditImageBlockController {
    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/image/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editImageBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @RequestParam("file") MultipartFile file,
            @RequestParam("contentOrder") Integer contentOrder, @RequestParam("imageBlockId") String blockId,
            Principal principal, RedirectAttributes attributes) throws IOException {
        System.out.println("****************************@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        byte[] image = null;
        String filename = null;
        if (file != null) {
            image = file.getBytes();
            filename = file.getOriginalFilename();
        }
        try {
            IImageBlock imageBlock = contentBlockManager.getImageBlock(blockId);
            contentBlockManager.createImageBlock(slideId, image, filename, contentOrder);
        } catch (ImageCouldNotBeStoredException e) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", "Image Content block cannot be stored.");
            return new ResponseEntity<>(mapper.writeValueAsString(node), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("ok", HttpStatus.OK);
    }

}