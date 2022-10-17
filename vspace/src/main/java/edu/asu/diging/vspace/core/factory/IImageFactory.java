package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IVSImage;

public interface IImageFactory {

    IVSImage createImage(String filename, String fileType);

    IVSImage createDefaultImage(String filename, String fileType, String spaceId);

}