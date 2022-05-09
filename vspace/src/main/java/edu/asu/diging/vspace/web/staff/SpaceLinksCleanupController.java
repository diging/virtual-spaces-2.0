package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;

import edu.asu.diging.vspace.core.services.ISpaceLinkManager;

@Controller
public class SpaceLinksCleanupController {
    
    @Autowired
    private ISpaceLinkManager spaceLinkManager;
    
    @DeleteMapping(value = "/staff/dashboard/cleanup")
    public ResponseEntity<String> deleteSpaceLinkWithNullSource(){
        spaceLinkManager.deleteSpaceLinksWithSourceAsNull();
        return new ResponseEntity<String>("Ok", HttpStatus.OK);
    }

}
