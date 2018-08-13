package edu.asu.diging.vspace.web.staff.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.data.SpaceRepository;

@RestController
public class SpaceListApiController {
	
	@Autowired
	private SpaceRepository spaceRepo;

	@RequestMapping(value="/staff/api/space/list")
	public String getSpaces() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode spaceArray = mapper.createArrayNode();
		spaceRepo.findAll().forEach(s -> {
			ObjectNode node = mapper.createObjectNode();
			node.put("id", s.getId());
			node.put("name", s.getName());
			node.put("description", s.getDescription());
			spaceArray.add(node);
		});
		
		return mapper.writeValueAsString(spaceArray);
	}
}
