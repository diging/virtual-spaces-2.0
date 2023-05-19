package edu.asu.diging.vspace.core.services;

import java.io.IOException;

import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlideNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.Space;

public interface ISnapshotManager {
    
    void createSnapshot(String resourcesPath, String exhibitionFolderName, SequenceHistory sequenceHistory, ExhibitionDownload exhibitionDownload) throws IOException, InterruptedException, FileStorageException ;

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
