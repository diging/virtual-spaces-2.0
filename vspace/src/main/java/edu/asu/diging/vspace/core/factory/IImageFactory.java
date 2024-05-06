package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IVSImage;

/**
 * (non-javadoc)
 * The IImageFactory interface defines a factory for creating instances of the {@link IVSImage}
 * interface. 
 * Implementations of this interface should provide an implementation for the
 * {@link IImageFactory#createImage(java.lang.String, java.lang.String)} method, which takes a filename and file type
 * as input parameters and returns an instance of the edu.asu.diging.vspace.core.model.IVSImage interface.
 * 
 */
public interface IImageFactory {

    /**
     * (non-javadoc)
     * Creates a new instance of the {@link IVSImage} interface, based on the given filename and
     * file type.
     * 
     * @param filename The filename of the image to create.
     * @param fileType The file type of the image to create.
     * @return An instance of the edu.asu.diging.vspace.core.model.IVSImage interface.
     */
    IVSImage createImage(String filename, String fileType);

}