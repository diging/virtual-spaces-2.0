package edu.asu.diging.vspace.core.services;

import java.io.IOException;

import org.thymeleaf.context.WebContext;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Space;

public interface IDownloadsManager {
    byte[] downloadExhibition(String resourcesPath, String exhibitionFolderName, WebContext context) throws IOException ;

    void downloadSpace(Space space, String exhibitionFolderPath, WebContext context);

    void downloadModule(IModule module, ISpace space, String imagesFolderPath, String spaceFolderPath, WebContext context);

    void downloadSequence(ISequence startSequence, IModule module, ISpace space, String spaceFolderPath,
            String imagesFolderPath,  WebContext context) ;

    void storeTemplateForSlide(String slideId, String spaceFolderPath, WebContext context, String spaceId,
            String moduleId, String sequenceId);
}
