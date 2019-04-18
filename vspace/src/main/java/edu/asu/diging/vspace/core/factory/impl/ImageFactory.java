package edu.asu.diging.vspace.core.factory.impl;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

@Service
public class ImageFactory implements IImageFactory {

    @Autowired
    private ImageRepository imageRepo;
    
    @Autowired
    private IStorageEngine storage;
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.IImageFactory#createImage(java.lang.
     * String, java.lang.String)
     */
    @Override
    public IVSImage createImage(String filename, String fileType) {
        IVSImage image = new VSImage();
        image.setFilename(filename);
        image.setFileType(fileType);
        return image;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.IImageFactory#createImage(java.lang.
     * String, java.lang.String)
     */
    @Override
    public void editImage(String imageId, ImageForm imageForm) throws FileNotFoundException, FileStorageException {
        if (imageRepo.findById(imageId).isPresent()) {
            VSImage image = imageRepo.findById(imageId).get();
            if (storage.renameFile(imageId, image.getFilename(), imageForm.getFileName())) {
                image.setFilename(imageForm.getFileName());
                image.setDescription(imageForm.getDescription());
                imageRepo.save(image);
            } else {
                throw new FileNotFoundException();
            }
        } else {
            throw new FileStorageException();
        }
    }
}
