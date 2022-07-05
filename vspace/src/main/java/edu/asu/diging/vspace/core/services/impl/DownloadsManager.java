package edu.asu.diging.vspace.core.services.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    
    
    public void downloadSpaces() {
        List<Space> spaces= spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED);
        
        spaces.forEach( space -> {
            
            String spaceFolderPath = storageEngine.createFolder(space.getId(), path);
            
            List<IModuleLink> moduleLinks = space.getModuleLinks();
            
            moduleLinks.forEach(moduleLink -> {
             IModule module =   moduleLink.getModule();

                  List<ISlide> slides = module.getSlides();
                  
                  slides.forEach(slide -> {
                    IVSImage image = slide.getFirstImageBlock().getImage();
                    
                    try {
                        byte[] byteArray = storageEngine.getImageContent(image.getId(), image.getFilename());
                        
                        storageEngine.storeFile(byteArray, image.getFilename(),image.getId(), spaceFolderPath );
                        
                        
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
    }
}
