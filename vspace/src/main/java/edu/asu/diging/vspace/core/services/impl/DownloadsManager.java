package edu.asu.diging.vspace.core.services.impl;

import java.beans.Beans;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.expression.ThymeleafEvaluationContext;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.DefaultTemplateResolver;

import edu.asu.diging.vspace.core.data.ExhibitionDownloadRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionDownloadNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlideNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IDownloadsManager;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISnapshotManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;


@Service
public class DownloadsManager  implements  IDownloadsManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());



    @Autowired
    @Qualifier("storageEngineDownloads")
    private IStorageEngine storageEngineDownloads;

    @Autowired
    @Qualifier("storageEngine")
    private IStorageEngine storageEngineUploads;

    @Autowired
    ISnapshotManager snapshotManager;




  


    @Autowired
    private SequenceHistory sequenceHistory;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ExhibitionDownloadRepository exhibitionDownloadRepo;



    /**
     * Downloads all the published spaces and related modules into a folder and returns the byte array.
     * 
     * @param resourcesPath
     * @param exhibitionFolderName
     * @param context
     * @return
     * @throws IOException
     * @throws InterruptedException 
     * @throws ExecutionException 
     */

    
    @Override
    public ExhibitionDownload triggerDownloadExhibition(String resourcesPath, String exhibitionFolderName, WebContext context) throws IOException, InterruptedException, ExecutionException {                 
        ExhibitionDownload exhibitionDownload = exhibitionDownloadRepo.findByFolderName(exhibitionFolderName);        
        if(exhibitionDownload ==null ) {
            exhibitionDownload = new ExhibitionDownload();
        }

        String exhibitionFolderPath =  storageEngineDownloads.createFolder(exhibitionFolderName);


        exhibitionDownload.setFolderPath(exhibitionFolderPath);
        exhibitionDownload.setFolderName(exhibitionFolderName);
        exhibitionDownload.setDownloadComplete(false);

        exhibitionDownloadRepo.save(exhibitionDownload);
        snapshotManager.createSnapShot(resourcesPath, exhibitionFolderName, context, sequenceHistory, exhibitionFolderPath, exhibitionDownload);
        //        new AsyncResult<String>(createSnapShot(resourcesPath, exhibitionFolderName, context, sequenceHistory, exhibitionFolderPath, exhibitionDownload));

        return exhibitionDownload;

    }
    
    
 


 

    /**
     * Downloads the given exhibition folder by the given ExhibitionDownload id
     * 
     * @param id
     * @return
     * @throws ExhibitionDownloadNotFoundException
     * @throws IOException
     */
    @Override
    public byte[] downloadExhibitionFolder(String id) throws ExhibitionDownloadNotFoundException, IOException {
        Optional<ExhibitionDownload> exhibitionDownlaod = exhibitionDownloadRepo.findById(id);

        if(exhibitionDownlaod.isPresent()) {
            
            try {
                return storageEngineDownloads.generateZipFolder(exhibitionDownlaod.get().getFolderPath());                
            }catch(FileSystemNotFoundException e) {
                logger.error("Zip folder not yet created", e);
                throw new ExhibitionDownloadNotFoundException(id);
            }
              
        } else {
            throw new ExhibitionDownloadNotFoundException(id);
        }

    }
 



    @Override
    public Boolean checkIfSnapshotCreated(String id) {
        Optional<ExhibitionDownload> exhibitionDownlaod = exhibitionDownloadRepo.findById(id);
        if(exhibitionDownlaod.isPresent()) {
     
                
                return exhibitionDownlaod.get().isDownloadComplete();
//                return storageEngineDownloads.checkIfFolderExists(exhibitionDownlaod.get().getFolderPath());        
            
            
        }

        return false;
    }





    @Override
    public String getExhibitionFolderName() {
        return  "Exhibition"+ LocalDateTime.now();
    }


}
