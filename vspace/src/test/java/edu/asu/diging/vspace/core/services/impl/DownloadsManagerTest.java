package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.data.ExhibitionDownloadRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.VSImage;


public class DownloadsManagerTest {
    
    @Spy 
    @InjectMocks
    private DownloadsManager serviceToTest;
    
    @Mock
    private FileUtils fileUtils;
    
    @Mock
    StorageEngine storageEngine;
    
    @Spy
    private DownloadsManager serviceToTestSpy;
    
    
    @Mock   
    ExhibitionDownloadRepository exhibitionDownloadRepo;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_copyResourcesToExhibition_success(){


        String exhibitionFolderPath = "/Exhibition";
        String resourcesPath = "/Resources";
        serviceToTest.copyResourcesToExhibition(exhibitionFolderPath, resourcesPath);
        try {
            verify(fileUtils).copyDirectory(new File(resourcesPath), new File(exhibitionFolderPath+ File.separator + "resources"));
        } catch (IOException e) {

        }
    }
   
    @Test
    public void test_copyImageToFolder_success() throws IOException, FileStorageException {
        
        IVSImage image = new VSImage();       
        image.setFilename("IMAGE1");
        image.setId("ID1");
        String fileContent =  "file content"; 
        byte[] byteArray = fileContent.getBytes();      
        String filePath = "/path";     
        when(storageEngine.getImageContent(image.getId(), image.getFilename())).thenReturn(byteArray);          
        serviceToTest.copyImageToFolder(image, filePath)   ;                                     
        verify(storageEngine).storeFile(byteArray, image.getFilename(),image.getId(), filePath );        
        
    }
    
    @Test
    public void test_downloadExhibitionFolder_success() throws Exception {
        ExhibitionDownload exhibitionDownload = new ExhibitionDownload();
        exhibitionDownload.setId("ID1");
     
        String filePath = "/path"; 
        String fileContent =  "file content"; 
        exhibitionDownload.setFolderPath(filePath);
        byte[] byteArray = fileContent.getBytes();   
        when(exhibitionDownloadRepo.findById("ID1")).thenReturn(Optional.of(exhibitionDownload));
        doReturn(byteArray).when(serviceToTest.generateZipFolder(filePath));
        
//        when(serviceToTest.generateZipFolder(filePath)).thenReturn(byteArray);
       byte[] response = serviceToTest.downloadExhibitionFolder("ID1") ;
       
//       verify(serviceToTest).generateZipFolder(filePath);
       assertEquals(response, byteArray);
    }
    
    
    @Test
    public void test_downloadSpace_success() {
        Space space = new Space();
        space.setId("SPACE_ID");
        space.setModuleLinks(new ArrayList());
        ModuleLink moduleLink1 = new ModuleLink();
        
        IModule module1 = new Module();
        moduleLink1.setId("MODULE_LINK_1");
        moduleLink1.setModule(module1);
        
        ModuleLink moduleLink2 = new ModuleLink();
        IModule module2 = new Module();
        moduleLink2.setId("MODULE_LINK_2");
        moduleLink2.setModule(module2);

        
        space.getModuleLinks().add(moduleLink1);       
        space.getModuleLinks().add(moduleLink2);  
        
        String exhibitionFolderPath = "/Exhibition"; 
        String spaceFolderPath = exhibitionFolderPath + File.separator+ space.getId();
        String imagesFolderPath = exhibitionFolderPath + File.separator+ "images";
        when(storageEngine.createFolder(space.getId(), exhibitionFolderPath)).thenReturn(spaceFolderPath);
        doNothing().when(serviceToTest).addHtmlPage(exhibitionFolderPath, spaceFolderPath, spaceFolderPath);
        when(storageEngine.createFolder("images", spaceFolderPath)).thenReturn(imagesFolderPath);
        doNothing().when(serviceToTest).copyImageToFolder(Mockito.any(IVSImage.class), Mockito.any(String.class));

        serviceToTest.downloadSpace(space, exhibitionFolderPath, "");
        
        verify(serviceToTest, times(1)).downloadModule(module1, space, imagesFolderPath, spaceFolderPath, "");
        verify(serviceToTest, times(1)).downloadModule(module2, space, imagesFolderPath, spaceFolderPath, "");

    }
    
    
    
   
}
