package edu.asu.diging.vspace.web.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSImage;

@RestController
public class ImageApiController {

	@Autowired
	private ImageRepository imageRepo;
	
	@Autowired
	private IStorageEngine storage;
	
	@RequestMapping("/api/image/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable String id) {
		IVSImage image = imageRepo.findById(id).get();
		byte[] imageContent = null;
		try {
			imageContent = storage.getImageContent(image.getId(), image.getFilename());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	    headers.setContentType(MediaType.parseMediaType(image.getFileType()));
	     
	    return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
	}
}
