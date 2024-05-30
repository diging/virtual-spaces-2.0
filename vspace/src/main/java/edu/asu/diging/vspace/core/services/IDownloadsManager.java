package edu.asu.diging.vspace.core.services;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.AsyncResult;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;

import edu.asu.diging.vspace.core.exception.ExhibitionDownloadNotFoundException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlideNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SnapshotCouldNotBeCreatedException;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.Space;

public interface IDownloadsManager {
    ExhibitionDownload triggerDownloadExhibition(String exhibitionFolderName) throws IOException, InterruptedException, ExecutionException, SnapshotCouldNotBeCreatedException;
 
    byte[] downloadExhibitionFolder(String id) throws ExhibitionDownloadNotFoundException, IOException;


    Boolean checkIfSnapshotCreated(String id);

    String getExhibitionFolderName();


    Page<ExhibitionDownload> getAllExhibitionDownloads(int filesPagenum);
}
