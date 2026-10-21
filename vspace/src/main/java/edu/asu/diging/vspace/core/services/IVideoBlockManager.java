package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.VideoCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IVideoBlockManager extends IGenericContentBlockManager<IVideoBlock> {

    CreationReturnValue createVideoBlock(String slideId, byte[] video, Long size, String fileName, 
            String url, String title) throws VideoCouldNotBeStoredException;

    void updateVideoBlock(IVideoBlock videoBlock, byte[] video, Long fileSize, String url, 
            String filename, String title) throws VideoCouldNotBeStoredException;

    void saveVideoBlock(IVideoBlock videoBlock);
}
