package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
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
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.WebContext;

import edu.asu.diging.vspace.core.data.ExhibitionDownloadRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionDownloadNotFoundException;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.services.IDownloadsManager;
import edu.asu.diging.vspace.core.services.impl.DownloadsManager;

@Controller
public class DownloadsController {

    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    
    @Autowired
    IDownloadsManager downloadsManager;
    
    @Autowired
    ExhibitionDownloadRepository exhibitionDownloadRepository;
    
    @RequestMapping("/staff/downloads/list")
    public String listDownloads(Model model) {
        
        model.addAttribute("downloadsList" , exhibitionDownloadRepository.findAllByOrderByCreationDateDesc());
        return "exhibition/downloads/downloadList";
    }
    
    
    @RequestMapping(value = "/staff/exhibit/download/trigger", method = RequestMethod.GET) 
    public ResponseEntity<Resource> downloadExhibitionTrigger(HttpServletRequest request, HttpServletResponse response,  Model model) {


        Resource resource = null; 
        byte[] exhibitionDownload = null;
        try {     
            String pathToResources = request.getServletContext().getRealPath("") + "/resources";

            String exhibitionFolderName= downloadsManager.getExhibitionFolderName();        
            WebContext context = new WebContext(request, response, request.getServletContext());

                        long contentLength =0 ;
                        
                        
                        byte[] byteArrayResource = null;
                        AsyncResult<byte[]> futureTask =
                                downloadsManager.triggerDownloadExhibition(pathToResources, exhibitionFolderName, context);
                        
                        if(futureTask.isDone()) {
                            byteArrayResource = futureTask.get();
                        }
                        if(byteArrayResource!=null) {
                            resource = new ByteArrayResource(byteArrayResource);
                            contentLength= resource.contentLength();
                        }
                            
                            return  ResponseEntity.ok()
                                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+exhibitionFolderName+".zip")
                                    .contentLength(contentLength)
                                    .header(HttpHeaders.CONTENT_TYPE, "application/zip")
                                    .body(resource);
                            
//            exhibitionDownload = downloadsManager.triggerDownloadExhibition(pathToResources, exhibitionFolderName, context);
//            return  ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+exhibitionFolderName+".zip")
//                    .contentLength(resource.contentLength())
//                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                    .body(exhibitionDownload);
//            

        } 
        catch (Exception e) {
            logger.error("Could not download exhibition", e);
            return new ResponseEntity<Resource>(resource, HttpStatus.INTERNAL_SERVER_ERROR);


        }
    }
    
//    @RequestMapping(value = "/staff/exhibit/download", method = RequestMethod.GET) 
//    public ResponseEntity<CompletableFuture<byte[]>> downloadExhibition(HttpServletRequest request, HttpServletResponse response, Model model) {
//
//
//        Resource resource = null; 
//        CompletableFuture<byte[]> exhibitionDownload = null;
//        try {     
//            String pathToResources = request.getServletContext().getRealPath("") + "/resources";
//
//            String exhibitionFolderName= downloadsManager.getExhibitionFolderName();        
//
//            WebContext context = new WebContext(request, response, request.getServletContext());
//
//
//            //            byte[] byteArrayResource = downloadsManager.downloadExhibition(pathToResources, exhibitionFolderName, context);
//            //            resource = new ByteArrayResource(byteArrayResource);
//            //            return  ResponseEntity.ok()
//            //                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+exhibitionFolderName+".zip")
//            //                    .contentLength(resource.contentLength())
//            //                    .header(HttpHeaders.CONTENT_TYPE, "application/zip")
//            //                    .body(resource);
//
//            exhibitionDownload = downloadsManager.triggerDownloadExhibition(pathToResources, exhibitionFolderName, context);
//            return  ResponseEntity.ok()
//                    //                  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+exhibitionFolderName+".zip")
//                    .contentLength(resource.contentLength())
//                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                    .body(exhibitionDownload);
//
//        } 
//        catch (Exception e) {
//            logger.error("Could not download exhibition", e);
//            return new ResponseEntity<CompletableFuture<byte[]>>(exhibitionDownload, HttpStatus.INTERNAL_SERVER_ERROR);
//
//
//        }
//    }
    
  


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

}
