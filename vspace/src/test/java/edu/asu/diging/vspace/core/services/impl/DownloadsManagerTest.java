package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import edu.asu.diging.vspace.core.data.ExhibitionDownloadRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionDownloadNotFoundException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.file.impl.StorageEngineDownloads;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.VSImage;


public class DownloadsManagerTest {

    @Spy 
    @InjectMocks
    private DownloadsManager serviceToTest;

    @Mock
    StorageEngineDownloads storageEngine;

    @Spy
    private DownloadsManager serviceToTestSpy;

    @Mock   
    ExhibitionDownloadRepository exhibitionDownloadRepo;
    
    @Mock
    SpaceRepository spaceRepository;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
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
        when(storageEngine.generateZipFolder(filePath)).thenReturn(byteArray);

        byte[] response = serviceToTest.downloadExhibitionFolder("ID1") ;
        assertEquals(response, byteArray);
    }




    @Test
    public void test_downloadExhibitionFolder_exhibitionDownloadNotPresent() {
        when(exhibitionDownloadRepo.findById("ID")).thenReturn(Optional.ofNullable(null));
        assertThrows(ExhibitionDownloadNotFoundException.class, () ->  serviceToTest.downloadExhibitionFolder("ID") );
    }
    
    
  
}
