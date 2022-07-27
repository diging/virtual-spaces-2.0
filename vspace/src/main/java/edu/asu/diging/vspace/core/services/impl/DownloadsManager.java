package edu.asu.diging.vspace.core.services.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import edu.asu.diging.vspace.core.data.ExhibitionDownloadRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;


@Service
public class DownloadsManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SpaceRepository spaceRepository;
    
    @Autowired
    StorageEngine storageEngine;
    
    @Value("${downloads_path}")
    private String downloadsPath;
    
    @Autowired
    SpringTemplateEngine springTemplateEngine;
       
    
    @Autowired
    private ExhibitionDownloadRepository exhibitionDownloadRepo;
    
    public byte[] downloadExhibition(String resourcesPath, String exhibitionFolderName, String localAddress) throws IOException {       
        byte[] resource = null;
        String exhibitionFolderPath =  storageEngine.createFolder(exhibitionFolderName, downloadsPath);
        copyResourcesToExhibition(exhibitionFolderPath,resourcesPath ); 

        List<Space> spaces= spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED);

        for(Space space : spaces) {
            downloadSpace(space, exhibitionFolderPath, localAddress);                
        }               
        resource = generateZipFolder(exhibitionFolderPath);
        exhibitionDownloadRepo.save( new ExhibitionDownload(exhibitionFolderPath));
        return resource;
    }

    public void downloadSpace(Space space, String exhibitionFolderPath, String localAddress) {

        String spaceFolderPath = storageEngine.createFolder(space.getId(), exhibitionFolderPath);

        addHtmlPage(space.getId(), spaceFolderPath , getApiToDownloadSpace(space,localAddress) );

        String imagesFolderPath = storageEngine.createFolder("images" , spaceFolderPath);

        copyImageToFolder(space.getImage(),imagesFolderPath) ;

        List<IModuleLink> moduleLinks = space.getModuleLinks();

        moduleLinks.forEach(moduleLink -> {
                     
            IModule module =   moduleLink.getModule();
            downloadModule(module, space,  imagesFolderPath, spaceFolderPath, localAddress);
            
        });        
    }

    public void downloadModule(IModule module, ISpace space, String imagesFolderPath, String spaceFolderPath, String localAddress) {
        ISequence startSequence = module.getStartSequence();
        if(startSequence!= null) {
            downloadSequence(startSequence, module, space, spaceFolderPath,imagesFolderPath , localAddress);
           
        }

    }

    public void downloadSequence(ISequence startSequence, IModule module, ISpace space, String spaceFolderPath,
            String imagesFolderPath, String localAddress) {
        List<ISlide> slides = startSequence.getSlides();
        slides.forEach(slide -> {
            if(slide instanceof BranchingPoint) {
                BranchingPoint branchingPoint = (BranchingPoint) slide;
                List<IChoice> choices  =  branchingPoint.getChoices();
                choices.forEach(choice -> {
                    if(!choice.getSequence().getId().equals(startSequence.getId())) {
                        downloadSequence(choice.getSequence(), module, space, spaceFolderPath, imagesFolderPath, localAddress); 
                    }
                });
            }
            else {
                IVSImage image = slide.getFirstImageBlock().getImage();
                copyImageToFolder(image, imagesFolderPath);
                String api = getApiToDownloadSlide(space, module, startSequence , slide, localAddress);
                addHtmlPage(slide.getId(), spaceFolderPath , api);
            }

        });
        
    }

    private String getApiToDownloadSlide(ISpace space, IModule module, ISequence startSequence, ISlide slide, String localAddress) {

        StringBuilder apiStringBuilder = new StringBuilder();
        apiStringBuilder.append(localAddress);
        apiStringBuilder.append("/vspace/exhibit/");
        apiStringBuilder.append(space.getId());
        apiStringBuilder.append("/module/");
        apiStringBuilder.append(module.getId());
        apiStringBuilder.append("/sequence/");
        apiStringBuilder.append(startSequence.getId());
        apiStringBuilder.append("/slide/");
        apiStringBuilder.append(slide.getId());
        apiStringBuilder.append("?isDownload=true");
        return apiStringBuilder.toString();
    }

    private String getApiToDownloadSpace(ISpace space, String localAddress) {
        StringBuilder apiStringBuilder = new StringBuilder();
        apiStringBuilder.append(localAddress);
        apiStringBuilder.append("/vspace/exhibit/space/");
        apiStringBuilder.append(space.getId());
        apiStringBuilder.append("?isDownload=true");
        return apiStringBuilder.toString();
    }

    public void copyResourcesToExhibition(String exhibitionFolderPath, String resourcesPath) {

        try {
            FileUtils.copyDirectory(new File(resourcesPath), new File(exhibitionFolderPath+ File.separator + "resources")); //TODO: constant
        } catch (IOException e) {
            logger.error("Could not copy resources" , e);
        } 
    }


    public void copyImageToFolder(IVSImage image, String imagesFolderPath) {
        try {
            byte[] byteArray = storageEngine.getImageContent(image.getId(), image.getFilename());
            storageEngine.storeFile(byteArray, image.getFilename(),image.getId(), imagesFolderPath );

        } catch (IOException | FileStorageException e) {
            logger.error("Could not copy images" , e);
        }     
    }


    public void addHtmlPage(String directory, String spaceFolderPath, String api ) {
        try {          
            
//            byte[] fileContent = download("http://localhost:8080/vspace/exhibit"+api);        
 
      
            
//            String response = springTemplateEngine.process("exhibition/downloads/spaceDownloadTemplate" , new Context());
            
            byte[] fileContent = download(api);
            storageEngine.storeFile(fileContent, directory+".html",null, spaceFolderPath );

         
        } catch (IOException | FileStorageException e) {
            logger.error("Could not copy template" , e);
        }   
        
       
        
    }
    
    
    public  byte[] download(String urlString) throws IOException {
        URL url = new URL(urlString);
       return IOUtils.toByteArray(url); 
         
        
     }

    public byte[] downloadExhibitionFolder(String id) throws Exception {
        Optional<ExhibitionDownload> exhibitionDownlaod = exhibitionDownloadRepo.findById(id);

        if(exhibitionDownlaod.isPresent()) {
            return  generateZipFolder(exhibitionDownlaod.get().getFolderPath());                
        }else {
            throw new Exception("Exhibition folder not found");
        }

    }

    public byte[] generateZipFolder(String folderPath) throws IOException {
        Path zipFile = Paths.get(folderPath);
        
        try (           
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
                ZipOutputStream responseZipStream = new ZipOutputStream(bufferedOutputStream);

                Stream<Path> paths = Files.walk(zipFile)) {
            paths
            .filter(path -> !Files.isDirectory(path))
            .forEach(path -> {
                ZipEntry  zipEntry = new ZipEntry(zipFile.relativize(path).toString());
                try {
                    responseZipStream.putNextEntry(zipEntry);
                    Files.copy(path, responseZipStream);
                    responseZipStream.closeEntry();

                } catch (IOException e) {
                    System.err.println(e);
                }
            });
            IOUtils.close(responseZipStream);
            IOUtils.close(bufferedOutputStream);
            IOUtils.close(byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            logger.error("Could not create zip folder" + e);
            throw new IOException(e);
        }      
        
    }

}
