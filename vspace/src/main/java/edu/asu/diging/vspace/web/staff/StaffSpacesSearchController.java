package edu.asu.diging.vspace.web.staff;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class StaffSpacesSearchController {
    private static final int PAGE_SIZE = 5;
    
    @Autowired
    private ISpaceManager spaceManager;
    
    @RequestMapping("/staff/spaces/search")
    public ResponseEntity<String> search(
            @RequestParam(value = "term", required = false) String search,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        
        Page<ISpace> spacesPage;
        
        if (search != null && !search.trim().isEmpty()) {
            spacesPage = spaceManager.findByNamePaginated(search, page, PAGE_SIZE);
        } else {
            spacesPage = spaceManager.getAllSpacesPaginated(page, PAGE_SIZE);
        }
        
        List<ISpace> spaces = spacesPage.getContent();
        
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode itemsArray = mapper.createArrayNode();
        
        for (ISpace space : spaces) {
            ObjectNode spaceNode = mapper.createObjectNode();
            spaceNode.put("id", space.getId());
            spaceNode.put("name", space.getName());
            itemsArray.add(spaceNode);
        }
        
        ObjectNode result = mapper.createObjectNode();
        result.set("items", itemsArray);
        result.put("hasMore", page < spacesPage.getTotalPages());
        result.put("totalPages", spacesPage.getTotalPages());
        result.put("currentPage", page);
        
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }
}