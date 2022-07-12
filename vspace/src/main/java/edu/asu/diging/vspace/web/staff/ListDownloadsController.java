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
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.services.impl.DownloadsManager;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ListDownloadsController {

    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    
    @Autowired
    DownloadsManager downloadsManager;
    
    @RequestMapping("/staff/downloads/list")
    public String listDownloads(Model model) {
        
        return "exhibition/downloads/downloadList";
    }
    
    
    @RequestMapping(value = "/staff/download", method = RequestMethod.GET) 
    public ResponseEntity<ZipOutputStream> downloadExhibition(HttpServletRequest request) {

   
        ZipOutputStream resource = null;  
        try {
           ;      
            resource =   downloadsManager.downloadExhibition( request.getServletContext().getRealPath("") + "/resources" );

            ResponseEntity<ZipOutputStream> response = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SPA000000001.zip")
//                    .contentLength(resource.contentLength())
                    .header(HttpHeaders.CONTENT_TYPE, "application/zip")
                  
                    .body(resource);
          
            return response;
        } 
        catch (Exception e) {
            logger.error("Could not download exhibition", e);
            return new ResponseEntity<ZipOutputStream>(resource, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
  

}
