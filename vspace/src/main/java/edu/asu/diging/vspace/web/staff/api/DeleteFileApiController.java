package edu.asu.diging.vspace.web.staff.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.services.impl.FileApiManager;

@Controller
public class DeleteFileApiController {
    
    @Autowired
    private FileApiManager fileManager;
    
    @RequestMapping(value = "/staff/files/delete/{fileId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFile(Model model, @PathVariable String fileId) {
        if(!fileManager.deleteFile(fileId)) {
            return new ResponseEntity<String>("Could not delete file", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Deleted file", HttpStatus.OK);
    }

}
