package edu.asu.diging.vspace.core.services;

import java.io.IOException;

import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;

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
    void createSnapShot(String resourcesPath, String exhibitionFolderName, SequenceHistory sequenceHistory, String exhibitionFolderPath, ExhibitionDownload exhibitionDownload) throws IOException, InterruptedException ;

    void copyResourcesToExhibition(String exhibitionFolderPath, String resourcesPath) throws IOException;

    
    void downloadSpace(Space space, String exhibitionFolderPath,  SequenceHistory sequenceHistory);

    void downloadModule(IModule module, ISpace space, String imagesFolderPath, String spaceFolderPath);

    void downloadSequences(ISequence startSequence, IModule module, ISpace space, String spaceFolderPath,
            String imagesFolderPath) ;

    void storeTemplateForSlide(String slideId, String spaceFolderPath,  String spaceId,
            String moduleId, String sequenceId);

 
    void populateContextForSlide(Context context, String spaceId, String moduleId, String sequenceId, String slideId)
            throws SlidesInSequenceNotFoundException, SequenceNotFoundException, SlideNotFoundException;

    


    void storeTemplateForSpace(String directory, String spaceFolderPath,SequenceHistory sequenceHistory);

    void populateContextForSpace(Context context, String id, SequenceHistory sequenceHistory);

}
