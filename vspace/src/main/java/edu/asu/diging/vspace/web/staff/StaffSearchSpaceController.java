package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.StaffSearchSpaceResults;

@Controller
public class StaffSearchSpaceController {

    @Autowired
    private IStaffSearchManager staffSearchManager;
    
    @RequestMapping("/staff/spaces/search")
    public ResponseEntity<String> search(@RequestParam(value = "term", required = false) String search){
        List<ISpace> spaces = null;
        if (search != null && !search.trim().isEmpty()) {
            spaces = spaceManager.findByName(search);
        } else {
            spaces = spaceManager.getAllSpaces();
        }

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode idArray = mapper.createArrayNode();
        for (ISpace space : spaces) {
            ObjectNode spaceNode = mapper.createObjectNode();
            spaceNode.put("id", space.getId());
            spaceNode.put("name", space.getName());
            idArray.add(spaceNode);
        }
        return new ResponseEntity<String>(idArray.toString(), HttpStatus.OK);
    }
    
    @Autowired
    private ISpaceManager spaceManager;

    private List<ISpace> paginationForSpace(String spacePagenum, String searchTerm) {
        Page<ISpace> spacePage = staffSearchManager.searchInSpaces(searchTerm, Integer.parseInt(spacePagenum));
        return spacePage.getContent();
    }
    
}
