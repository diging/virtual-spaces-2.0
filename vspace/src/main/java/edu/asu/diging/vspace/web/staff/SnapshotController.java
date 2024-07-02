package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SnapshotCouldNotBeCreatedException;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.services.ISnapshotManager;

@Controller
public class SnapshotController {

    @Autowired
    private ISnapshotManager snapshotManager;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/staff/exhibit/snapshot", method = RequestMethod.POST) 
    public ResponseEntity<ExhibitionSnapshot> createExhibitionSnapshot(HttpServletRequest request, HttpServletResponse response,  Model model) {
        ExhibitionSnapshot exhibitionSnapshot = null;
        try {      
            exhibitionSnapshot = snapshotManager.triggerExhibitionSnapshotCreation();
            return  ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(exhibitionSnapshot);
        } 
        catch (IOException | SnapshotCouldNotBeCreatedException | InterruptedException e) {
            logger.error("Could not download exhibition", e);
            return new ResponseEntity<ExhibitionSnapshot>(exhibitionSnapshot, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/staff/exhibit/download/{id}", method = RequestMethod.GET) 
    public ResponseEntity<Resource> downloadSnapshot(@PathVariable("id") String id, @RequestParam("folderName") String exhibitionSnapshotFolderName , HttpServletRequest request)
            throws ExhibitionSnapshotNotFoundException , IOException, FileStorageException {
        Resource resource = null;

        try {
            byte[] byteArrayResource = snapshotManager.getExhibitionSnapshot(id);
            resource = new ByteArrayResource(byteArrayResource);
            return  ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+exhibitionSnapshotFolderName+".zip")
                    .contentLength(resource.contentLength())
                    .header(HttpHeaders.CONTENT_TYPE, "application/zip")
                    .body(resource);
        } catch (ExhibitionSnapshotNotFoundException |  IOException e) {
            logger.error("Could not download exhibition", e);
            return new ResponseEntity<Resource>(resource, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
