package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.data.ModuleRepository;

@RestController
public class ModuleApiController {

	@Autowired
	private ModuleRepository moduleRepo;

	@RequestMapping(value="/staff/api/module/list")
	public String getSpaces() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode moduleArray = mapper.createArrayNode();
		moduleRepo.findAll().forEach(s -> {
			ObjectNode node = mapper.createObjectNode();
			node.put("id", s.getId());
			node.put("name", s.getName());
			node.put("description", s.getDescription());
			moduleArray.add(node);
		});
		
		return mapper.writeValueAsString(moduleArray);
	}
}
