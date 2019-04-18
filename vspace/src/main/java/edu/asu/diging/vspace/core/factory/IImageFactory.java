package edu.asu.diging.vspace.core.factory;

import java.io.FileNotFoundException;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

public interface IImageFactory {

    IVSImage createImage(String filename, String fileType);

    void editImage(String imageId, ImageForm imageForm) throws FileNotFoundException, FileStorageException;
}