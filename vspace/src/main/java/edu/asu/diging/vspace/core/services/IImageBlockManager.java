package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IImageBlockManager extends IGenericContentBlockManager<IImageBlock> {

    CreationReturnValue createImageBlock(String slideId, byte[] image, String filename) 
            throws ImageCouldNotBeStoredException;

    CreationReturnValue createImageBlock(String slideId, IVSImage image);

    void updateImageBlock(IImageBlock imageBlock, byte[] image, String filename) 
            throws ImageCouldNotBeStoredException;

    void updateImageBlock(IImageBlock imageBlock, IVSImage image);
}
