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

import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class AddImageBlockController {

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/slide/{id}/imagecontent", method = RequestMethod.POST)
    public ResponseEntity<String> addImageBlock(@PathVariable("id") String slideId,
            @RequestParam("file") MultipartFile file, Principal principal, RedirectAttributes attributes)
            throws IOException {

        System.out.println("inside image block---------------");
        byte[] bgImage = null;
        String filename = null;
        if (file.isEmpty() || file.equals(null)) {
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("showAlert", "true");
            attributes.addAttribute("message", "Please select a background image.");

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", "Image Content block cannot be stored.");
            return new ResponseEntity<>(mapper.writeValueAsString(node), HttpStatus.INTERNAL_SERVER_ERROR);

        } else if (file != null) {
            bgImage = file.getBytes();
            filename = file.getOriginalFilename();
        }

        contentBlockManager.createImageBlock(slideId, bgImage, filename);

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
