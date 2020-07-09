package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.JsonObject;

import edu.asu.diging.vspace.core.services.IExhibitionManager;

@Controller
public class ExhibitionStartSpaceController {

    @Autowired
    private IExhibitionManager exhibitManager;
    
    @RequestMapping(value = "/staff/exhibit/start", method = RequestMethod.GET)
    public ResponseEntity<String> getStartSpaceId() {
        String exhibitionStartSpaceId = 
                ((exhibitManager.getStartExhibition() == null) || (exhibitManager.getStartExhibition().getStartSpace() == null)) ?
                        null : exhibitManager.getStartExhibition().getStartSpace().getId();
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("startSpace", exhibitionStartSpaceId);
        return new ResponseEntity<>(jsonObj.toString(), HttpStatus.OK);
    }
}
