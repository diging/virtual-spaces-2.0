package edu.asu.diging.vspace.core.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IAsyncSnapshotCreator;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IRenderingManager;

@Service
public class AsyncSnapshotCreator implements IAsyncSnapshotCreator {
    Logger logger = LoggerFactory.getLogger(getClass());

    private final String RESOURCES_FOLDER_NAME = "resources";
    
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

    /**
     * Creates a snapshot and copies the spaces to exhibitionFolderPath
     * 
     * @param resourcesPath - the path to the resources directory
     * @param exhibitionFolderName - the name of the folder where the exhibition data is stored
     * @param sequenceHistory - the history of sequences to be included in the snapshot
     * @param exhibitionSnapshot - the snapshot object that will store the exhibition state
     * @return 
     * @throws IOException - if an I/O error occurs during the snapshot creation
     * @throws InterruptedException - if the snapshot creation process is interrupted
     * @throws FileStorageException - if an error occurs while storing the snapshot
     * @throws ImageCouldNotBeStoredException 
     */   
    @Async
    @Transactional
    public Future<SnapshotTask> createSnapshot(String resourcesPath, String exhibitionFolderName,SequenceHistory sequenceHistory, ExhibitionSnapshot exhibitionSnapshot) 
            throws IOException, InterruptedException, FileStorageException {
        storageEngineDownloads.copyToFolder(exhibitionFolderName + File.separator + RESOURCES_FOLDER_NAME, resourcesPath);
        List<Space> spaces= spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED);

        for(Space space : spaces) {
            renderingManager.createSpaceSnapshot(space, exhibitionFolderName, sequenceHistory);                
        }
        
        // Generate index.html as the entry point for the exhibition
        generateIndexHtml(exhibitionFolderName, spaces);
        
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
        StringBuilder htmlContent = new StringBuilder();
        
        // Get exhibition details
        var exhibition = exhibitionManager.getStartExhibition();
        String exhibitionTitle = exhibition != null ? exhibition.getTitle() : "Virtual Exhibition";
        Space startSpace = exhibition != null && exhibition.getStartSpace() != null ? 
            (Space) exhibition.getStartSpace() : (spaces.isEmpty() ? null : spaces.get(0));
        
        // Build HTML content
        htmlContent.append("<!DOCTYPE html>\n");
        htmlContent.append("<html>\n");
        htmlContent.append("<head>\n");
        htmlContent.append("    <meta charset='UTF-8'>\n");
        htmlContent.append("    <meta name='viewport' content='width=device-width, initial-scale=1'>\n");
        htmlContent.append("    <title>").append(exhibitionTitle).append("</title>\n");
        htmlContent.append("    <link href='./resources/bootstrap-4.1.2/css/bootstrap.min.css' rel='stylesheet'>\n");
        htmlContent.append("    <link href='./resources/extra/Home.css' rel='stylesheet'>\n");
        htmlContent.append("    <link href='./resources/extra/diging-icon-pack.css' rel='stylesheet'>\n");
        htmlContent.append("</head>\n");
        htmlContent.append("<body>\n");
        htmlContent.append("    <div class='container-fluid'>\n");
        htmlContent.append("        <div class='nav-bar' style='height:48px; margin-bottom: 20px;'>\n");
        htmlContent.append("            <h2 class='navbar-brand'>").append(exhibitionTitle).append("</h2>\n");
        htmlContent.append("        </div>\n");
        htmlContent.append("        <div class='row'>\n");
        htmlContent.append("            <div class='col-md-12'>\n");
        htmlContent.append("                <h1>Welcome to ").append(exhibitionTitle).append("</h1>\n");
        
        if (startSpace != null) {
            htmlContent.append("                <p>Click below to start exploring the exhibition:</p>\n");
            htmlContent.append("                <a href='./").append(startSpace.getId()).append("/").append(startSpace.getId()).append(".html' class='btn primary-btn btn-lg'>Start Exhibition</a>\n");
        }
        
        htmlContent.append("                <hr>\n");
        htmlContent.append("                <h3>Available Spaces:</h3>\n");
        htmlContent.append("                <div class='row'>\n");
        
        for (Space space : spaces) {
            htmlContent.append("                    <div class='col-md-4 mb-3'>\n");
            htmlContent.append("                        <div class='card'>\n");
            htmlContent.append("                            <div class='card-body'>\n");
            htmlContent.append("                                <h5 class='card-title'>").append(space.getName()).append("</h5>\n");
            if (space.getDescription() != null && !space.getDescription().trim().isEmpty()) {
                htmlContent.append("                                <p class='card-text'>").append(space.getDescription()).append("</p>\n");
            }
            htmlContent.append("                                <a href='./").append(space.getId()).append("/").append(space.getId()).append(".html' class='btn primary-btn'>Visit Space</a>\n");
            htmlContent.append("                            </div>\n");
            htmlContent.append("                        </div>\n");
            htmlContent.append("                    </div>\n");
        }
        
        htmlContent.append("                </div>\n");
        htmlContent.append("            </div>\n");
        htmlContent.append("        </div>\n");
        htmlContent.append("    </div>\n");
        htmlContent.append("</body>\n");
        htmlContent.append("</html>");
        
        // Store the index.html file
        storageEngineDownloads.storeFile(htmlContent.toString().getBytes(), "index.html", exhibitionFolderName);
    }
}
