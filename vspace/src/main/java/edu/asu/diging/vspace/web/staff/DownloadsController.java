package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
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
public class DownloadsController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
       
    @Autowired
    private ISnapshotManager snapshotManager;
    
    @RequestMapping("/staff/downloads/list")
    public String listDownloads(Model model, @RequestParam(value = "downloadsPagenum", required = false, defaultValue = "1") String downloadsPagenum) {
        Integer pageNum = 1;
        try {           
            pageNum =  Integer.parseInt(downloadsPagenum);
        } catch(NumberFormatException e) {
            logger.error("Invalid page number", e);
        }        
        Page<ExhibitionSnapshot> downloadsPage = snapshotManager.getAllExhibitionSnapshots(pageNum);
        model.addAttribute("downloadsList" , downloadsPage.getContent());
        model.addAttribute("downloadsCurrentPageNumber", Integer.parseInt(downloadsPagenum));
        model.addAttribute("downloadsTotalPages", downloadsPage.getTotalPages());
        model.addAttribute("downloadsCount", downloadsPage.getTotalElements());
        
        return "exhibition/downloads/downloadList";
    }

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
    public ResponseEntity<Resource> downloadExhibitionFolder(@PathVariable("id") String id, @RequestParam("folderName") String exhibitionSnapshotFolderName , HttpServletRequest request)
            throws ExhibitionSnapshotNotFoundException , IOException, FileStorageException {
        Resource resource = null;

        try {
            byte[] byteArrayResource = snapshotManager.getExhibitionFolder(id);
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

    
    @RequestMapping(value = "/staff/exhibit/download/checkStatus/{id}", method = RequestMethod.GET) 
    public ResponseEntity<Boolean> exhibitionDownloadStatus(@PathVariable("id") String id, @RequestParam("folderName") String exhibitionDownloadFolderName , HttpServletRequest request) throws ExhibitionSnapshotNotFoundException {
        if (snapshotManager.doesSnapshotExist(id)) {
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
        else {
            throw new ExhibitionSnapshotNotFoundException(id);
        }
    }
}
