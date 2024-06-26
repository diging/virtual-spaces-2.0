package edu.asu.diging.vspace.core.services;

import java.io.IOException;

import org.thymeleaf.context.Context;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlideNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.Space;

public interface IRenderingManager {

    /**
     * 
     * Creates a snapshot of the given space and related modules into exhibitionFolderPath
     * 
     * @param space                            the space object 
     * @param exhibitionFolderName             the folder name of the exhibition where space contents will be stored           
     * @param sequenceHistory                  the sequence history object having the history of sequences                        
     * @throws FileStorageException 
     */
    void createSpaceSnapshot(Space space, String exhibitionFolderName, SequenceHistory sequenceHistory)
            throws FileStorageException;
}
