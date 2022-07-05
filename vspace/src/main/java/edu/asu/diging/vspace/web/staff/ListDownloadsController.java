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

import javax.imageio.ImageIO;
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
        
        return "staff/downloads/downloadList";
    }
    
    
    @RequestMapping(value = "/staff/download", method = RequestMethod.GET) 
    public ResponseEntity<Resource> downloadExhibition(HttpServletRequest request) {

   
        String spaceId= "SPA000000001";
        Resource resource = null;  
        try {
            
            downloadsManager.downloadSpaces();
            //            System.out.println(request.+"://"+ request.getServerName());
            resource =  download("http://localhost:8080/vspace/exhibit/space/" + spaceId);


            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + spaceId)
                    .contentLength(resource.contentLength())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .body(resource);

        } catch (IOException e) {
            logger.error("Could not download exhibition", e);
            return new ResponseEntity<Resource>(resource, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public  Resource download(String urlString) throws IOException {
        URL url = new URL(urlString);
     
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            HTMLEditorKit htmlKit = new HTMLEditorKit();
            HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
            htmlKit.read(reader, htmlDoc, 0);
            for (HTMLDocument.Iterator iterator = htmlDoc.getIterator(HTML.Tag.A); iterator.isValid(); iterator.next()) {
                AttributeSet attributes = iterator.getAttributes();
                String imgSrc = (String) attributes.getAttribute(HTML.Attribute.HREF);

                System.out.println(imgSrc);
                if (imgSrc != null && (imgSrc.toLowerCase().endsWith(".jpg") || (imgSrc.endsWith(".png")) || (imgSrc.endsWith(".jpeg")) || (imgSrc.endsWith(".bmp")) || (imgSrc.endsWith(".ico")))) {
                    downloadImage(url.toString(), imgSrc);
                }
            }

          
//           String line;
//           while ((line = reader.readLine()) != null) {
//              writer.write(line);
//           }
           System.out.println("Page downloaded.");
        }catch(Exception e) {
            e.printStackTrace();
            logger.error("" + e);
        } 
         
           return new ByteArrayResource(IOUtils.toByteArray(url)); 
         
        
     }
    

    private static void downloadImage(String webUrl, String imgSrc) {
        BufferedImage image = null;
        try {
            if (!(imgSrc.startsWith("http"))) {
                String url = null;
                url = url + imgSrc;
            } else {
                String url = imgSrc;
            }
            imgSrc = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);
            String imageFormat = null;
            imageFormat = imgSrc.substring(imgSrc.lastIndexOf(".") + 1);
            String imgPath = null;
            imgPath = "/Users/prachikharge/virtual-spaces-2.0/vspace/uploads/images/" + imgSrc ;
            String url = null;
            URL imageUrl = new URL(url);
            image = ImageIO.read(imageUrl);
            if (image != null) {
                File file = new File(imgPath);
                ImageIO.write(image, imageFormat, file);
            }
        } catch (IOException ex) {
        }

    }
}
