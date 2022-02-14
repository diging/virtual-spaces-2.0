package edu.asu.diging.vspace.web.staff;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.services.IModuleManager;

@Controller
public class DeleteModuleController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IModuleManager moduleManager;

    @RequestMapping(value="/staff/module/{moduleId}/delete", method=RequestMethod.DELETE)
    public ResponseEntity<String> deleteModule(@PathVariable("moduleId") String moduleId, Model model) {
        try {
            moduleManager.deleteModule(moduleId);
        }
        catch (IllegalStateException exception) {
            logger.error("Could not delete Module.", exception);
            return new ResponseEntity<String>("Sorry, unable to delete the module.",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("Ok", HttpStatus.OK);
    }
}
