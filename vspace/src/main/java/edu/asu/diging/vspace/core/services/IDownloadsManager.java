package edu.asu.diging.vspace.core.services;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.springframework.scheduling.annotation.AsyncResult;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;

import edu.asu.diging.vspace.core.exception.ExhibitionDownloadNotFoundException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlideNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.Space;

public interface IDownloadsManager {
    ExhibitionDownload triggerDownloadExhibition(String resourcesPath, String exhibitionFolderName, WebContext context) throws IOException, InterruptedException, ExecutionException ;

    void downloadSpace(Space space, String exhibitionFolderPath, WebContext context, SequenceHistory sequenceHistory);

    void downloadModule(IModule module, ISpace space, String imagesFolderPath, String spaceFolderPath, WebContext context);

    void downloadSequences(ISequence startSequence, IModule module, ISpace space, String spaceFolderPath,
            String imagesFolderPath,  WebContext context) ;

    void storeTemplateForSlide(String slideId, String spaceFolderPath, WebContext context, String spaceId,
            String moduleId, String sequenceId);

 
    void populateContextForSlide(Context context, String spaceId, String moduleId, String sequenceId, String slideId)
            throws SlidesInSequenceNotFoundException, SequenceNotFoundException, SlideNotFoundException;

 
    void copyResourcesToExhibition(String exhibitionFolderPath, String resourcesPath) throws IOException;

    void storeTemplateForSpace(String directory, String spaceFolderPath, WebContext context, SequenceHistory sequenceHistory);

    void populateContextForSpace(Context context, String id, SequenceHistory sequenceHistory);

    byte[] downloadExhibitionFolder(String id) throws ExhibitionDownloadNotFoundException, IOException;

    String createSnapShot(String resourcesPath, String exhibitionFolderName, WebContext context, SequenceHistory sequenceHistory, String exhibitionFolderPath) throws IOException, InterruptedException ;

    byte[] downloadExhibition(AsyncResult<byte[]> asyncResult) throws IOException, ExecutionException;


    Boolean checkIfSnapshotCreated(String id);

    String getExhibitionFolderName();


}
