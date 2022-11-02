package edu.asu.diging.vspace.core.services;

import java.io.IOException;

import org.thymeleaf.context.WebContext;

import edu.asu.diging.vspace.core.exception.ExhibitionDownloadNotFoundException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlideNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.Space;

public interface IDownloadsManager {
    ExhibitionDownload downloadExhibition(String resourcesPath, String exhibitionFolderName, WebContext context) throws IOException ;

    void downloadSpace(Space space, String exhibitionFolderPath, WebContext context);

    void downloadModule(IModule module, ISpace space, String imagesFolderPath, String spaceFolderPath, WebContext context);

    void downloadSequence(ISequence startSequence, IModule module, ISpace space, String spaceFolderPath,
            String imagesFolderPath,  WebContext context) ;

    void storeTemplateForSlide(String slideId, String spaceFolderPath, WebContext context, String spaceId,
            String moduleId, String sequenceId);

 
    void populateContextForSlide(WebContext context, String spaceId, String moduleId, String sequenceId, String slideId)
            throws SlidesInSequenceNotFoundException, SequenceNotFoundException, SlideNotFoundException;

 
    void copyResourcesToExhibition(String exhibitionFolderPath, String resourcesPath) throws IOException;

    void storeTemplateForSpace(String directory, String spaceFolderPath, WebContext context);

    void populateContextForSpace(WebContext context, String id);

    byte[] downloadExhibitionFolder(String id) throws ExhibitionDownloadNotFoundException, IOException;

    byte[] createSnapShot(String resourcesPath, String exhibitionFolderName, WebContext context) throws IOException ;
}
