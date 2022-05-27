package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

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
public class DownloadFileController {
    
    @Autowired
    private FileManager fileManager;
    
    @RequestMapping(value = "/staff/files/download/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(Model model, @PathVariable String fileId) {
        byte[] fileContent = null;
        try {
            fileContent = fileManager.downloadFile(fileId);
        } catch (IOException e) {
            return new ResponseEntity<byte[]>(fileContent, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<byte[]>(fileContent, HttpStatus.OK);
    }
}
