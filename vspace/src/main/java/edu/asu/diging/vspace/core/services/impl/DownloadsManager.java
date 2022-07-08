package edu.asu.diging.vspace.core.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

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
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
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
    
    
    public Resource downloadSpaces(String resourcesPath) {
        
        Resource resource = null;
        String exhibitionFolderPath =  storageEngine.createFolder("Exhibition", path);
        
        copyResourcesToExhibition(exhibitionFolderPath,resourcesPath ); //TODO: 
        
        
        List<Space> spaces= spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED);
        
        for(Space space : spaces) {
            
            String spaceFolderPath = storageEngine.createFolder(space.getId(), exhibitionFolderPath);
            
          resource = addHtmlPage(space.getId(), spaceFolderPath);
           
            String imagesFolderPath = storageEngine.createFolder("images" , spaceFolderPath);
            
            copyImageToFolder(space.getImage(),imagesFolderPath) ;
            
            List<IModuleLink> moduleLinks = space.getModuleLinks();
            
            moduleLinks.forEach(moduleLink -> {
             IModule module =   moduleLink.getModule();

                  List<ISlide> slides = module.getSlides();
                  
                  slides.forEach(slide -> {
                     
                    IVSImage image = slide.getFirstImageBlock().getImage();
                    copyImageToFolder(image, imagesFolderPath);
             

              });
            });
            
            
        } 
        return resource;
    }


    private void copyResourcesToExhibition(String exhibitionFolderPath, String resourcesPath) {

        try {


            FileUtils.copyDirectory(new File(resourcesPath), new File(exhibitionFolderPath+ File.separator + "resources")); //TODO: constant


        } catch (IOException e) {
            logger.error("Could not copy resources" , e);
            e.printStackTrace();
        } 


    }


    private void copyImageToFolder(IVSImage image, String imagesFolderPath) {
        try {
            byte[] byteArray = storageEngine.getImageContent(image.getId(), image.getFilename());

            storageEngine.storeFile(byteArray, image.getFilename(),image.getId(), imagesFolderPath );


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileStorageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }


    private Resource addHtmlPage(String directory, String spaceFolderPath) {
        Resource resource  = null;
        try {          
            
            byte[] fileContent = download("http://localhost:8080/vspace/exhibit/space/download/" + directory);
            resource =  new ByteArrayResource(fileContent);
        
            storageEngine.storeFile(fileContent, directory+".html",null, spaceFolderPath );

         
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (FileStorageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resource;
        
    }
    
    
    public  byte[] download(String urlString) throws IOException {
        URL url = new URL(urlString);
       return IOUtils.toByteArray(url); 
         
        
     }
    

}
