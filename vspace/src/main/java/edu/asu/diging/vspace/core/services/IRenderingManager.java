package edu.asu.diging.vspace.core.services;

import java.io.IOException;

import org.thymeleaf.context.Context;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlideNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;

public interface IRenderingManager {
    
    /**
     * Creates a snapshot and copies the spaces to exhibitionFolderPath
     * 
     * @param resourcesPath
     * @param exhibitionFolderName
     * @param sequenceHistory
     * @param exhibitionSnapshot
     * @throws IOException
     * @throws InterruptedException
     * @throws FileStorageException
     */
    void createSnapshot(String resourcesPath, String exhibitionFolderName, SequenceHistory sequenceHistory, ExhibitionSnapshot exhibitionSnapshot) 
            throws IOException, InterruptedException, FileStorageException ;

    /**
     * Populates the context with variables for slide template.
     * 
     * @param context
     * @param spaceId
     * @param moduleId
     * @param sequenceId
     * @param slideId
     * @throws SlidesInSequenceNotFoundException
     * @throws SequenceNotFoundException
     * @throws SlideNotFoundException
     */
    void populateContextForSlide(Context context, String spaceId, String moduleId, String sequenceId, String slideId)
            throws SlidesInSequenceNotFoundException, SequenceNotFoundException, SlideNotFoundException;

    /**
     * Populates context with variables to process space template
     * 
     * @param context
     * @param id
     * @param sequenceHistory
     */
    void populateContextForSpace(Context context, String id, SequenceHistory sequenceHistory);

}
