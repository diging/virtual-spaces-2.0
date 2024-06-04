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
     * @param resourcesPath - the path to the resources directory
     * @param exhibitionFolderName - the name of the folder where the exhibition data is stored
     * @param sequenceHistory - the history of sequences to be included in the snapshot
     * @param exhibitionSnapshot - the snapshot object that will store the exhibition state
     * @throws IOException - if an I/O error occurs during the snapshot creation
     * @throws InterruptedException - if the snapshot creation process is interrupted
     * @throws FileStorageException - if an error occurs while storing the snapshot
     */
    void createSnapshot(String resourcesPath, String exhibitionFolderName, SequenceHistory sequenceHistory, ExhibitionSnapshot exhibitionSnapshot) 
            throws IOException, InterruptedException, FileStorageException ;

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

}
