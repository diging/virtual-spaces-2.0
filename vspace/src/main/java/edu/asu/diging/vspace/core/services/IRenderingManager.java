package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.model.impl.Space;

public interface IRenderingManager {

    /**
     * Creates a snapshot of the given space and related modules into exhibitionFolder.
     * 
     * @param space the space object 
     * @param exhibitionFolderName the folder name where space contents will be stored           
     * @throws FileStorageException if an error occurs while storing files
     */
    void createSpaceSnapshot(Space space, String exhibitionFolderName) throws FileStorageException;
}
