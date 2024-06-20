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
     * Populates the context with variables for slide template.
     * 
     * @param context - the context to be populated with slide content
     * @param spaceId - id of the space containing the module
     * @param moduleId - id of module having the sequence
     * @param sequenceId - id of the sequence containing the slide
     * @param slideId - id of the slide 
     * @throws SlidesInSequenceNotFoundException - if the given sequence has no slides 
     * @throws SequenceNotFoundException - if sequence is not found
     * @throws SlideNotFoundException - if the slide is not found
     */
    void populateContextForSlide(Context context, String spaceId, String moduleId, String sequenceId, String slideId)
            throws SlidesInSequenceNotFoundException, SequenceNotFoundException, SlideNotFoundException;

    /**
     * Populates context with variables to process space template
     * 
     * @param context - the context to be populated with space content
     * @param id - the space id
     * @param sequenceHistory - Sequence history object of the space
     */
    void populateContextForSpace(Context context, String id, SequenceHistory sequenceHistory);
    
    /**
     * 
     * Creates a snapshot of space and related modules into exhibitionFolderPath
     * 
     * @param space                  - space object of the space to be downloaded
     * @param exhibitionFolderName   - The name of the folder where the space contents will be stored
     * @param sequenceHistory        - The SequenceHistory object containing the history of sequences
     * @throws FileStorageException
     */
    void createSpaceSnapshot(Space space, String exhibitionFolderName,  SequenceHistory sequenceHistory) throws FileStorageException;

}
