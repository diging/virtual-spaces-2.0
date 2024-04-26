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
import edu.asu.diging.vspace.core.exception.ExhibitionDownloadNotFoundException;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.services.IDownloadsManager;

@Controller
public class DownloadsController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
       
    @Autowired
    IDownloadsManager downloadsManager;
    
    @RequestMapping("/staff/downloads/list")
    public String listDownloads(Model model, @RequestParam(value = "downloadsPagenum", required = false, defaultValue = "1") String downloadsPagenum) {
        Integer pageNum = 1;
        try {           
            pageNum =  Integer.parseInt(downloadsPagenum);
        } catch(NumberFormatException e) {
            logger.error("Invalid page number", e);
        }        
        Page<ExhibitionDownload> downloadsPage = downloadsManager.getAllExhibitionDownloads(pageNum);
        model.addAttribute("downloadsList" , downloadsPage.getContent());
        model.addAttribute("downloadsCurrentPageNumber", Integer.parseInt(downloadsPagenum));
        model.addAttribute("downloadsTotalPages", downloadsPage.getTotalPages());
        model.addAttribute("downloadsCount", downloadsPage.getTotalElements());
        
        return "exhibition/downloads/downloadList";
    }

    @RequestMapping(value = "/staff/exhibit/download", method = RequestMethod.POST) 
    public ResponseEntity<ExhibitionDownload> downloadExhibitionTrigger(HttpServletRequest request, HttpServletResponse response,  Model model) {
        ExhibitionDownload exhibitionDownload = null;
        try {     
            String pathToResources = request.getServletContext().getRealPath("") + "resources";
            String exhibitionFolderName= downloadsManager.getExhibitionFolderName();        
            exhibitionDownload =
                    downloadsManager.triggerDownloadExhibition(pathToResources, exhibitionFolderName);

            return  ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(exhibitionDownload);
        } 
        catch (Exception e) {
            logger.error("Could not download exhibition", e);
            return new ResponseEntity<ExhibitionDownload>(exhibitionDownload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/staff/exhibit/download/{id}", method = RequestMethod.GET) 
    public ResponseEntity<Resource> downloadExhibitionFolder(@PathVariable("id") String id, @RequestParam("folderName") String exhibitionDownloadFolderName , HttpServletRequest request)
            throws ExhibitionDownloadNotFoundException , IOException {
        Resource resource = null;      

        try {
            byte[] byteArrayResource = downloadsManager.downloadExhibitionFolder(id);
            resource = new ByteArrayResource(byteArrayResource);
            return  ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+exhibitionDownloadFolderName+".zip")
                    .contentLength(resource.contentLength())
                    .header(HttpHeaders.CONTENT_TYPE, "application/zip")
                    .body(resource);
        } catch (ExhibitionDownloadNotFoundException |  IOException e) {
            logger.error("Could not download exhibition", e);
            return new ResponseEntity<Resource>(resource, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @RequestMapping(value = "/staff/exhibit/download/checkStatus/{id}", method = RequestMethod.GET) 
    public ResponseEntity<Boolean> exhibitionDownloadStatus(@PathVariable("id") String id, @RequestParam("folderName") String exhibitionDownloadFolderName , HttpServletRequest request) {
        try {
            return new ResponseEntity<Boolean>(downloadsManager.checkIfSnapshotCreated(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Could not check the exhibition Download status ",e);
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
