package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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

        List<ISpace> spaces;
        boolean hasMore;

        if (search != null && !search.trim().isEmpty()) {
            spaces = spaceManager.findByNamePaginated(search, page, PAGE_SIZE);
            hasMore = spaces.size() == PAGE_SIZE;
        } else {
            spaces = spaceManager.getAllSpacesPaginated(page, PAGE_SIZE);
            hasMore = spaces.size() == PAGE_SIZE;
        }

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
        result.put("hasMore", hasMore);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }
}

