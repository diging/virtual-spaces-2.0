package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.services.impl.FileManager;

@Controller
public class DownloadFileController {
    
    @Autowired
    private FileManager fileManager;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    
    @RequestMapping(value = "/staff/files/{fileId}/download", method = RequestMethod.GET )
    public ResponseEntity<Resource> downloadFile(Model model, @PathVariable String fileId) throws IOException {
        Resource resource = null;  
        IVSFile file = null;
        try {        
            file = fileManager.getFileById(fileId);
            if(file!=null) {
                resource = fileManager.downloadFile(file.getFilename(), fileId);   
            } else {
                logger.error("Could not find file " + fileId);
                return new ResponseEntity<Resource>(resource, HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            logger.error("Could not download file", e);
            return new ResponseEntity<Resource>(resource, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getOriginalFileName())
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_TYPE, file.getFileType())
                .body(resource);
    }
}
