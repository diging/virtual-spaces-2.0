package edu.asu.diging.vspace.core.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
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
    private String path;
    
    
    public ZipOutputStream downloadExhibition(String resourcesPath) {
        
        ZipOutputStream resource = null;
        String exhibitionFolderPath =  storageEngine.createFolder("Exhibition", path);

        copyResourcesToExhibition(exhibitionFolderPath,resourcesPath ); //TODO: 


        List<Space> spaces= spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED);

        for(Space space : spaces) {
            downloadSpace(space, exhibitionFolderPath);

//space.getSpaceLinks();
            
//            List<IExternalLink> externalLinks = space.getExternalLinks();
//            externalLinks.forEach(externalLink -> {
//               externalLink. 
//            });
            
            
                    
        } 
        
        try {
            resource= zip(exhibitionFolderPath, path + File.separator + "Exhibition.zip");//TODO change folder name unique
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resource;
    }

    private void downloadSpace(Space space, String exhibitionFolderPath) {

        String spaceFolderPath = storageEngine.createFolder(space.getId(), exhibitionFolderPath);

        addHtmlPage(space.getId(), spaceFolderPath , "/space/download/"+ space.getId() );

        String imagesFolderPath = storageEngine.createFolder("images" , spaceFolderPath);

        copyImageToFolder(space.getImage(),imagesFolderPath) ;

        List<IModuleLink> moduleLinks = space.getModuleLinks();

        moduleLinks.forEach(moduleLink -> {
                     
            IModule module =   moduleLink.getModule();
            downloadModule(module, space,  imagesFolderPath, spaceFolderPath);
            
        });        
    }

    private void downloadModule(IModule module, ISpace space, String imagesFolderPath, String spaceFolderPath) {
        ISequence startSequence = module.getStartSequence();
        if(startSequence!= null) {
            downloadSequence(startSequence, module, space, spaceFolderPath,imagesFolderPath );
           
        }

    }

    private void downloadSequence(ISequence startSequence, IModule module, ISpace space, String spaceFolderPath,
            String imagesFolderPath) {
        List<ISlide> slides = startSequence.getSlides();
        slides.forEach(slide -> {
            if(slide instanceof BranchingPoint) {
                BranchingPoint branchingPoint = (BranchingPoint) slide;
           List<IChoice> choices  =  branchingPoint.getChoices();
           choices.forEach(choice -> {
               downloadSequence(choice.getSequence(), module, space, spaceFolderPath, imagesFolderPath); 
           });
            }
            else if(slide.getClass().equals(ISlide.class)) {
                IVSImage image = slide.getFirstImageBlock().getImage();
                copyImageToFolder(image, imagesFolderPath);
                String api = getApiToDownloadSlide(space, module, startSequence , slide);
                addHtmlPage(slide.getId(), spaceFolderPath ,"/" +space.getId() + "/module/" + module.getId() + "/sequence/"+ startSequence.getId()+"/slide/"+slide.getId() + "?isDownload=true");
            }

        });
        
    }

    private String getApiToDownloadSlide(ISpace space, IModule module, ISequence startSequence, ISlide slide) {
        StringBuilder apiStringBuilder = new StringBuilder();
        apiStringBuilder.append("/");
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

    public ZipOutputStream zip( String sourcDirPath,  String zipPath) throws IOException {
        Path zipFile = Files.createFile(Paths.get(zipPath));

        Path sourceDirPath = Paths.get(sourcDirPath);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile));
             Stream<Path> paths = Files.walk(sourceDirPath)) {
            paths
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            Files.copy(path, zipOutputStream);
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
            return zipOutputStream;
            
        } catch (IOException e1) {
            throw new IOException(e1);
        }

        
//        logger.debug("Zip is created at : "+zipFile);
//        return new ByteArrayResource(Files.readAllBytes(zipFile));
      }

    private void copyResourcesToExhibition(String exhibitionFolderPath, String resourcesPath) {

        try {


            FileUtils.copyDirectory(new File(resourcesPath), new File(exhibitionFolderPath+ File.separator + "resources")); //TODO: constant


        } catch (IOException e) {
            logger.error("Could not copy resources" , e);
        } 


    }


    private void copyImageToFolder(IVSImage image, String imagesFolderPath) {
        try {
            byte[] byteArray = storageEngine.getImageContent(image.getId(), image.getFilename());

            storageEngine.storeFile(byteArray, image.getFilename(),image.getId(), imagesFolderPath );


        } catch (IOException | FileStorageException e) {
            logger.error("Could not copy images" , e);
        }     
    }


    private void addHtmlPage(String directory, String spaceFolderPath, String api) {
        try {          
            
            byte[] fileContent = download("http://localhost:8080/vspace/exhibit"+api);        
            storageEngine.storeFile(fileContent, directory+".html",null, spaceFolderPath );

         
        } catch (IOException | FileStorageException e) {
            logger.error("Could not copy template" , e);
        }   
        
       
        
    }
    
    
    public  byte[] download(String urlString) throws IOException {
        URL url = new URL(urlString);
       return IOUtils.toByteArray(url); 
         
        
     }
    

}
