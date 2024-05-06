package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.services.impl.FileManager;

@Controller
public class DeleteFileController {
    
    @Autowired
    private FileManager fileManager;
    
    @RequestMapping(value = "/staff/files/{fileId}/delete", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFile(Model model, @PathVariable String fileId) {
        if(!fileManager.deleteFile(fileId)) {
            return new ResponseEntity<String>("Could not delete file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("File deleted successfully", HttpStatus.OK);
    }

}
