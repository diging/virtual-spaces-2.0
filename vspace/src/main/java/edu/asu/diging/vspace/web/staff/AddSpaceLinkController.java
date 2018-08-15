package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class AddSpaceLinkController {

	@Autowired
	private ISpaceManager spaceManager;

	@RequestMapping(value = "/staff/space/{id}/spacelink", method = RequestMethod.POST)
	public ResponseEntity<String> createSpaceLink(@PathVariable("id") String id, @RequestParam("x") String x,
			@RequestParam("y") String y, @RequestParam("rotation") String rotation, @RequestParam("linkedSpace") String linkedSpaceId) throws JsonProcessingException, NumberFormatException, SpaceDoesNotExistException {

		ISpace source = spaceManager.getSpace(id);
		if (source == null) {
			return new ResponseEntity<>("{'error': 'Space could not be found.'}", HttpStatus.NOT_FOUND);
		}

		ISpaceLinkDisplay display = spaceManager.createSpaceLink("test", source, new Float(x), new Float(y),
				new Integer(rotation), linkedSpaceId);
		display.setRotation(new Integer(rotation));

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode linkNode = mapper.createObjectNode();
		linkNode.put("id", display.getLink().getId());
		linkNode.put("displayId", display.getId());
		linkNode.put("x", display.getPositionX());
		linkNode.put("y", display.getPositionY());

		return new ResponseEntity<>(mapper.writeValueAsString(linkNode), HttpStatus.OK);
	}
}
