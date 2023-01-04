package edu.asu.diging.vspace.core.services.impl;

import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.WebContext;

import edu.asu.diging.vspace.core.data.ExhibitionDownloadRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionDownloadNotFoundException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.services.IDownloadsManager;
import edu.asu.diging.vspace.core.services.ISnapshotManager;


@Service
public class DownloadsManager  implements  IDownloadsManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${page_size}")
    private int pageSize;

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
    private ExhibitionDownloadRepository exhibitionDownloadRepo;

    
    @Autowired
    ExhibitionDownloadRepository exhibitionDownloadRepository;

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
            
        }
        return false;
    }





    @Override
    public String getExhibitionFolderName() {
        return  "Exhibition"+ LocalDateTime.now();
    }

    
    @Override
    public Page<ExhibitionDownload> getAllExhibitionDownloads(int filesPagenum) {
        
        if (filesPagenum < 1) {
            filesPagenum = 1;
        }
        Pageable requestedPageForFiles = PageRequest.of(filesPagenum - 1, pageSize);

        Page<ExhibitionDownload> page =   exhibitionDownloadRepository.findAllByOrderByCreationDateDesc(requestedPageForFiles);
        return page.map(exhibitionDownload-> { return (ExhibitionDownload) exhibitionDownload; } );

        
       
    }

}
