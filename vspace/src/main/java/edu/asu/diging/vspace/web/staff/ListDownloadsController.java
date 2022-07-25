package edu.asu.diging.vspace.web.staff;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.asu.diging.vspace.core.data.ExhibitionDownloadRepository;
import edu.asu.diging.vspace.core.services.impl.DownloadsManager;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ListDownloadsController {

    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    
    @Autowired
    DownloadsManager downloadsManager;
    
    @Autowired
    ExhibitionDownloadRepository exhibitionDownloadRepository;
    
    @RequestMapping("/staff/downloads/list")
    public String listDownloads(Model model) {
        
        model.addAttribute("downloadsList" , exhibitionDownloadRepository.findAll());
        return "exhibition/downloads/downloadList";
    }
    
    
    @RequestMapping(value = "/staff/download", method = RequestMethod.GET) 
    public ResponseEntity<Resource> downloadExhibition(HttpServletRequest request) {



        Resource resource = null; 
        try {     
            String pathToResources = request.getServletContext().getRealPath("") + "/resources";
String serverUrl = "http://" +request.getServerName() + ":" +request.getServerPort();
            ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromRequest(request);

            String exhibitionFolderName= "Exhibition"+ LocalDateTime.now();

            byte[] byteArrayResource = downloadsManager.downloadExhibition(pathToResources, exhibitionFolderName, serverUrl);
            resource = new ByteArrayResource(byteArrayResource);
            return  ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+exhibitionFolderName+".zip")
                    .contentLength(resource.contentLength())
                    .header(HttpHeaders.CONTENT_TYPE, "application/zip")
                    .body(resource);

        } 
        catch (Exception e) {
            logger.error("Could not download exhibition", e);
            return new ResponseEntity<Resource>(resource, HttpStatus.INTERNAL_SERVER_ERROR);


        }
    }
    
    @RequestMapping(value = "/exhibition/downloadFolder/{id}", method = RequestMethod.GET) 
    public ResponseEntity<Resource> downloadExhibitionFolder(@PathVariable("id") String id, HttpServletRequest request) {
        
        Resource resource = null;      
       
        try {
            byte[] byteArrayResource = downloadsManager.downloadExhibitionFolder(id);
            resource = new ByteArrayResource(byteArrayResource);
            return  ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exhibitionFolderName.zip")
                    .contentLength(resource.contentLength())
                    .header(HttpHeaders.CONTENT_TYPE, "application/zip")
                    .body(resource);
        } catch (Exception e) {
            logger.error("Could not download exhibition", e);
            return new ResponseEntity<Resource>(resource, HttpStatus.INTERNAL_SERVER_ERROR);

        }
   
    }

}
