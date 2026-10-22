package edu.asu.diging.vspace.core.services;

import java.util.List;
import java.util.Map;

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

    /**
     * Creates a snapshot of the given space and related modules into exhibitionFolder
     * and adds an entry for the space and every exported module and slide to searchIndex.
     * 
     * @param space the space object 
     * @param exhibitionFolderName the folder name where space contents will be stored
     * @param searchIndex the list the search entries of the exported pages are added to
     * @throws FileStorageException if an error occurs while storing files
     */
    void createSpaceSnapshot(Space space, String exhibitionFolderName, List<Map<String, String>> searchIndex) throws FileStorageException;
}
