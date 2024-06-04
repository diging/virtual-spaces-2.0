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

import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.services.ISnapshotManager;

@Controller
public class SnapshotTaskController {

    @Autowired
    ISnapshotManager snapshotManager;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = "/staff/exhibit/task/snapshot/status", method = RequestMethod.GET) 
    public ResponseEntity<SnapshotTask> getLatestSnapshotStatus(HttpServletRequest request, HttpServletResponse response,  Model model) {                     
        SnapshotTask snapshotTask = snapshotManager.getLatestSnapshotTask();   
        if(snapshotTask == null) {
            logger.error("Could not find snapshot task");
            return new ResponseEntity<SnapshotTask>(snapshotTask, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return  ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(snapshotTask);   
        }
    }
}
