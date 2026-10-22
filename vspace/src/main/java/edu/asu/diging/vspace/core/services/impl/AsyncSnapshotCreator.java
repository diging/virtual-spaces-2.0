package edu.asu.diging.vspace.core.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IAsyncSnapshotCreator;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IRenderingManager;

@Service
public class AsyncSnapshotCreator implements IAsyncSnapshotCreator {
    Logger logger = LoggerFactory.getLogger(getClass());

    private final String RESOURCES_FOLDER_NAME = "resources";
    
    private final String INDEX_DOWNLOAD_TEMPLATE = "exhibition/downloads/indexDownloadTemplate";

    private final String SEARCH_DOWNLOAD_TEMPLATE = "exhibition/downloads/searchDownloadTemplate";

    private final String SEARCH_INDEX_FILE_NAME = "search-index.js";
    
    @Autowired
    private SpaceRepository spaceRepository;
    
    @Autowired
    @Qualifier("storageEngineDownloads")
    private IStorageEngine storageEngineDownloads;
    
    @Autowired
    private IRenderingManager renderingManager;
    
    @Autowired
    private SnapshotTaskRepository snapshotTaskRepository;
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @Autowired
    private SpringTemplateEngine springTemplateEngine;   

    /**
     * Creates a snapshot and copies the spaces to exhibitionFolderPath.
     * 
     * @param resourcesPath the path to the resources directory
     * @param exhibitionFolderName the name of the folder where the exhibition data is stored
     * @param exhibitionSnapshot the snapshot object that will store the exhibition state
     * @return Future containing the completed SnapshotTask
     * @throws IOException if an I/O error occurs during the snapshot creation
     * @throws InterruptedException if the snapshot creation process is interrupted
     * @throws FileStorageException if an error occurs while storing the snapshot
     */   
    @Async
    @Transactional
    public Future<SnapshotTask> createSnapshot(String resourcesPath, String exhibitionFolderName, ExhibitionSnapshot exhibitionSnapshot) 
            throws IOException, InterruptedException, FileStorageException {
        storageEngineDownloads.copyToFolder(exhibitionFolderName + File.separator + RESOURCES_FOLDER_NAME, resourcesPath);
        List<Space> spaces = spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED);

        List<Map<String, String>> searchIndex = new ArrayList<>();
        for (Space space : spaces) {
            renderingManager.createSpaceSnapshot(space, exhibitionFolderName, searchIndex);                
        }
        
        // Generate index.html as the entry point for the exhibition
        generateIndexHtml(exhibitionFolderName, spaces);

        generateSearch(exhibitionFolderName, searchIndex);
        
        SnapshotTask snapshotTask = exhibitionSnapshot.getSnapshotTask();
        snapshotTask.setTaskComplete(true);
        return new AsyncResult<SnapshotTask>(snapshotTaskRepository.save(snapshotTask));   
    }
    
    /**
     * Generates an index.html file that serves as the entry point for the static exhibition
     * 
     * @param exhibitionFolderName - the folder where the exhibition is stored
     * @param spaces - list of published spaces in the exhibition
     * @throws FileStorageException - if an error occurs while storing the index file
     */
    private void generateIndexHtml(String exhibitionFolderName, List<Space> spaces) throws FileStorageException {
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        String exhibitionTitle = exhibition != null ? exhibition.getTitle() : "Virtual Exhibition";
        // Only published spaces are exported, so fall back to the first one if the start space is not published
        ISpace startSpace = exhibition != null && exhibition.getStartSpace() != null
                && exhibition.getStartSpace().getSpaceStatus() == SpaceStatus.PUBLISHED ?
            exhibition.getStartSpace() : (spaces.isEmpty() ? null : spaces.get(0));
        
        // Create Thymeleaf context and populate with data
        Context context = new Context();
        context.setVariable("exhibitionTitle", exhibitionTitle);
        context.setVariable("startSpace", startSpace);
        context.setVariable("spaces", spaces);
        
        // Render the template
        String htmlContent = springTemplateEngine.process(INDEX_DOWNLOAD_TEMPLATE, context);
        
        // Store the index.html file
        storageEngineDownloads.storeFile(htmlContent.getBytes(), "index.html", exhibitionFolderName);
    }

    /**
     * Generates search.html and the search index it reads, so the static exhibition
     * can be searched in the browser without a server.
     * The index is stored as a script rather than JSON, so it also loads when the
     * exhibition is opened directly from disk.
     * 
     * @param exhibitionFolderName - the folder where the exhibition is stored
     * @param searchIndex - the search entries of all exported pages
     * @throws FileStorageException - if an error occurs while storing the files
     */
    private void generateSearch(String exhibitionFolderName, List<Map<String, String>> searchIndex) throws FileStorageException {
        // A module linked more than once from a space would otherwise show up several times
        Map<String, Map<String, String>> uniqueEntries = new LinkedHashMap<>();
        for (Map<String, String> entry : searchIndex) {
            uniqueEntries.putIfAbsent(entry.get("type") + entry.get("href"), entry);
        }
        String indexJson;
        try {
            indexJson = new ObjectMapper().writeValueAsString(uniqueEntries.values());
        } catch (JsonProcessingException e) {
            throw new FileStorageException("Could not create search index.", e);
        }
        String indexScript = "window.SEARCH_INDEX = " + indexJson + ";";
        storageEngineDownloads.storeFile(indexScript.getBytes(StandardCharsets.UTF_8), SEARCH_INDEX_FILE_NAME, exhibitionFolderName);

        IExhibition exhibition = exhibitionManager.getStartExhibition();
        Context context = new Context();
        context.setVariable("exhibitionTitle", exhibition != null ? exhibition.getTitle() : null);
        String htmlContent = springTemplateEngine.process(SEARCH_DOWNLOAD_TEMPLATE, context);
        storageEngineDownloads.storeFile(htmlContent.getBytes(StandardCharsets.UTF_8), "search.html", exhibitionFolderName);
    }
}
