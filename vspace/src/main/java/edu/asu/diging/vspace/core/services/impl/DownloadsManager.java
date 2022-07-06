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

import org.apache.commons.io.IOUtils;
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

    
    @Autowired
    SpaceRepository spaceRepository;
    
    @Autowired
    StorageEngine storageEngine;
    
    @Value("${downloads_path}")
    private String path;
    
    
    public Resource downloadSpaces() {
        List<Space> spaces= spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED);
        
        spaces.forEach( space -> {
            
            String spaceFolderPath = storageEngine.createFolder(space.getId(), path);
            
           Resource resource =  addHtmlPage(space.getId(), spaceFolderPath);
            String imagesFolderPath = storageEngine.createFolder("images" , spaceFolderPath);
            
            List<IModuleLink> moduleLinks = space.getModuleLinks();
            
            moduleLinks.forEach(moduleLink -> {
             IModule module =   moduleLink.getModule();

                  List<ISlide> slides = module.getSlides();
                  
                  slides.forEach(slide -> {
                    IVSImage image = slide.getFirstImageBlock().getImage();
                    
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

              });
            });
            
            
        } );
        return new ByteArrayResource(null);
    }


    private Resource addHtmlPage(String directory, String spaceFolderPath) {
        // TODO Auto-generated method stub
        
        try {
            
            
            
            
//         byte[] fileContent =   storageEngine.getFileContent("template", "downloadTemplate.html", path); //TODO: to be made constant
//         storageEngine.storeFile(fileContent, directory+".html",null, spaceFolderPath );
        return download("http://localhost:8080/vspace/exhibit/space/download/" + directory);
         
         
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
//        catch (FileStorageException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        return null;
        
    }
    
    
    public  Resource download(String urlString) throws IOException {
        URL url = new URL(urlString);
       return new ByteArrayResource(IOUtils.toByteArray(url)); 
         
        
     }
    

}
