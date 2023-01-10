package edu.asu.diging.vspace.web.staff;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.context.WebContext;

import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;

@Controller
public class SnapshotTaskController {
    
    @Autowired
    SnapshotTaskRepository snapshotTaskRepository;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = "/staff/exhibit/snapshotTask/status", method = RequestMethod.GET) 
    public ResponseEntity<SnapshotTask> getLatestSnapshotStatus(HttpServletRequest request, HttpServletResponse response,  Model model) {
        Boolean isSnapshotTaskComplete = false;
        SnapshotTask snapshotTask = null;
        try {     
//            String pathToResources = request.getServletContext().getRealPath("") + "/resources";
            
            snapshotTask= snapshotTaskRepository.findFirstByOrderByCreationDateDesc();   
            
//             isSnapshotTaskComplete = snapshotTask!= null ? snapshotTask.isTaskComplete(): true;
//            WebContext context = new WebContext(request, response, request.getServletContext());
//            exhibitionDownload =
//                    downloadsManager.triggerDownloadExhibition(pathToResources, exhibitionFolderName, context);
//
//            
            

            return  ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+exhibitionFolderName+".zip")
//                  .header(exhibitionFolderName, null)
//                    .contentLength(contentLength)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(snapshotTask); 

  } 
  catch (Exception e) {
      logger.error("Could not find snapshot task", e);
      return new ResponseEntity<SnapshotTask>(snapshotTask, HttpStatus.INTERNAL_SERVER_ERROR);


  }
    }
    
    

}
