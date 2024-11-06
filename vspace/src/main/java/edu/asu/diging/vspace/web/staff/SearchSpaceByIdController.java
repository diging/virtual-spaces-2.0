package edu.asu.diging.vspace.web.staff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.StaffSearchSlideResults;

@Controller
public class SearchSpaceByIdController {
    
    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping("/staff/search/space/{id}")
    public ResponseEntity<String> search(@PathVariable("id") String spaceId) {
        ISpace space = spaceManager.getSpace(spaceId);

        if (space == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode idArray = mapper.createArrayNode();
        ObjectNode spaceNode = mapper.createObjectNode();
        spaceNode.put("id", space.getId());
        spaceNode.put("name", space.getName());
        idArray.add(spaceNode);
        return new ResponseEntity <String> (idArray.toString(), HttpStatus.OK);
    }
}

