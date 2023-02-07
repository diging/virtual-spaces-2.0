package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.data.display.SpaceDisplayRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ISpaceDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ILanguageDescriptionObject;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LanguageDescriptionObject;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

public class SpaceManagerTest {

    @Mock
    private SpaceRepository spaceRepo;

    @Mock
    private SpaceDisplayRepository spaceDisplayRepo;

    @Mock
    private ImageRepository imageRepo;

    @Mock
    private SpaceLinkRepository spaceLinkRepo;

    @Mock
    private SpaceLinkDisplayRepository spaceLinkDisplayRepo;

    @Mock
    private IStorageEngine storage;

    @Mock
    private IImageFactory imageFactory;

    @Mock
    private IImageService imageService;

    @Mock
    private ISpaceLinkFactory spaceLinkFactory;

    @Mock
    private ISpaceLinkDisplayFactory spaceLinkDisplayFactory;

    @Mock
    private ISpaceDisplayFactory spaceDisplayFactory;

    @Mock
    private ExhibitionManager exhibitionManager;
    
    @Mock
    private ExhibitionLanguageRepository exhibitionLanguageRepo;


    @InjectMocks
    private SpaceManager managerToTest;

    private final String IMG_FILENAME = "img";
    private final String IMG_CONTENT_TYPE = "content/type";
    private String spaceId = "spaceId";
    private String spaceId1, spaceId2;
    private String spaceLinkId1;
    private SpaceLink spaceLink;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        spaceId1 = "SPA000000001";
        spaceId2 = "SPA000000001";
        spaceLinkId1 = "SPL000000001";

    }

    @Test
    public void test_storeSpace_success() throws FileStorageException {
        IVSImage image = new VSImage();
        image.setFilename(IMG_FILENAME);
        image.setFileType(IMG_CONTENT_TYPE);
        Mockito.when(imageFactory.createImage(Mockito.anyString(), Mockito.anyString())).thenReturn(image);
        Mockito.when(imageRepo.save((VSImage) image)).thenReturn((VSImage) image);
        ISpaceDisplay spaceDisplay = new SpaceDisplay();
        Mockito.when(spaceDisplayFactory.createSpaceDisplay()).thenReturn(spaceDisplay);
        Mockito.when(imageService.getImageData(Mockito.isA(byte[].class))).thenReturn(new ImageData(500, 300));

        byte[] imageBytes = new byte[100];
        Arrays.fill(imageBytes, (byte) 1);
        String filename = "IMAGE_FILE";

        String dirName = "DIR";
        String storePath = "PATH";

        Mockito.when(storage.storeFile(imageBytes, filename, dirName)).thenReturn(storePath);

        Space space = new Space();
        Mockito.when(spaceRepo.save((Space) space)).thenReturn(space);

        CreationReturnValue returnVal = managerToTest.storeSpace((ISpace) space, imageBytes, filename);
        Assert.assertEquals(returnVal.getElement(), space);
        Assert.assertEquals(returnVal.getErrorMsgs(), new ArrayList<>());
        Mockito.verify(spaceRepo).save(space);
        Mockito.verify(spaceDisplayRepo).save((SpaceDisplay)spaceDisplay);
    }

    @Test
    public void test_deleteSpaceById_whenIdIsNull() throws SpaceDoesNotExistException{
        managerToTest.deleteSpaceById(null);
        Mockito.verify(spaceDisplayRepo, Mockito.never()).deleteBySpaceId(null);
        Mockito.verify(spaceRepo, Mockito.never()).deleteById(null);
    }

    @Test
    public void test_deleteSpaceById_forNonExistentId() {
        Mockito.when(spaceRepo.findById(spaceId)).thenReturn(Optional.empty());
        managerToTest.deleteSpaceById(spaceId);
        Mockito.verify(spaceDisplayRepo).deleteBySpaceId(spaceId);
        Mockito.verify(spaceRepo).deleteById(spaceId);
    }

    @Test
    public void test_deleteSpaceById_forSuccess() { 
        Mockito.when(spaceRepo.findById(spaceId1)).thenReturn(Optional.empty());
        managerToTest.deleteSpaceById(spaceId1);
        Mockito.verify(spaceDisplayRepo).deleteBySpaceId(spaceId1);
        Mockito.verify(spaceRepo).deleteById(spaceId1);
    }

    @Test
    public void test_deleteSpaceById_whenSpaceHasLinks() {  
        ISpace space = new Space();
        spaceLink = new SpaceLink();
        space.setId(spaceId2);
        spaceLink.setId(spaceLinkId1);
        spaceLink.setTargetSpace(space);
        Mockito.when(spaceRepo.findById(spaceId1)).thenReturn(Optional.empty());
        Mockito.when(spaceLinkRepo.getLinkedSpaces(spaceId1)).thenReturn(Arrays.asList(spaceLink));
        managerToTest.deleteSpaceById(spaceId1);
        Mockito.verify(spaceLinkRepo).deleteBySourceSpaceId(spaceId1);
        Mockito.verify(spaceLinkDisplayRepo).deleteBySpaceLinkId(spaceLinkId1);
        Mockito.verify(spaceDisplayRepo).deleteBySpaceId(spaceId1);
        Mockito.verify(spaceRepo).deleteById(spaceId1);
    }

    @Test
    public void test_deleteSpaceById_whenLinksToSpace() {  
        Space space = new Space();
        spaceLink = new SpaceLink();
        space.setId(spaceId1);
        spaceLink.setId(spaceLinkId1);
        spaceLink.setTargetSpace(space);
        Mockito.when(spaceRepo.findById(spaceId1)).thenReturn(Optional.of(space));
        Mockito.when(spaceLinkRepo.findByTargetSpace(space)).thenReturn(Arrays.asList(spaceLink));
        managerToTest.deleteSpaceById(spaceId1);
        Assert.assertEquals(spaceLink.getTargetSpace(), null);
        Mockito.verify(spaceLinkRepo).deleteBySourceSpaceId(spaceId1);
        Mockito.verify(spaceLinkRepo).save(spaceLink);
        Mockito.verify(spaceDisplayRepo).deleteBySpaceId(spaceId1);
        Mockito.verify(spaceRepo).deleteById(spaceId1);
    }


    @Test
    public void test_getSpacesWithStatus_whenStatusIsNull() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        Mockito.when(spaceRepo.findAllBySpaceStatus(null)).thenReturn(spaceList);
        List<ISpace> nullStatusSpaces= managerToTest.getSpacesWithStatus(null);
        String actualSpaceIdWithNull = nullStatusSpaces.get(0).getId();
        Assert.assertEquals(spaceId1, actualSpaceIdWithNull);
    }

    @Test
    public void test_getSpacesWithStatus_whenStatusAsPublished() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        space.setSpaceStatus(SpaceStatus.PUBLISHED);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.PUBLISHED)).thenReturn(spaceList);
        List<ISpace> spaceWithPublishedStatus= managerToTest.getSpacesWithStatus(SpaceStatus.PUBLISHED);
        String actualSpaceIdWithPubStatus = spaceWithPublishedStatus.get(0).getId();
        Assert.assertEquals(spaceId1, actualSpaceIdWithPubStatus);
    }

    @Test
    public void test_getSpacesWithStatus_whenStatusAsUnPublished() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        space.setSpaceStatus(SpaceStatus.UNPUBLISHED);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.UNPUBLISHED)).thenReturn(spaceList);
        List<ISpace> spaceWithPublishedStatus= managerToTest.getSpacesWithStatus(SpaceStatus.UNPUBLISHED);
        String actualSpaceIdWithUnPubStatus = spaceWithPublishedStatus.get(0).getId();
        Assert.assertEquals(spaceId1, actualSpaceIdWithUnPubStatus);
    }

    @Test
    public void test_getSpacesWithPublishedStatus_spaceDoesNotExist() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        space.setSpaceStatus(SpaceStatus.UNPUBLISHED);
        Space space2=new Space();
        space2.setId(spaceId2);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.UNPUBLISHED)).thenReturn(spaceList);
        Mockito.when(spaceRepo.findAllBySpaceStatus(null)).thenReturn(spaceList);
        List<ISpace> spaceWithPublishedStatus= managerToTest.getSpacesWithStatus(SpaceStatus.PUBLISHED);
        Assert.assertTrue(spaceWithPublishedStatus.isEmpty());
    }

    @Test
    public void test_getSpacesWithNullStatus_spaceDoesNotExist() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        space.setSpaceStatus(SpaceStatus.UNPUBLISHED);
        Space space2=new Space();
        space.setId(spaceId2);
        space.setSpaceStatus(SpaceStatus.PUBLISHED);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        spaceList.add(space2);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.UNPUBLISHED)).thenReturn(spaceList);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.PUBLISHED)).thenReturn(spaceList);
        List<ISpace> spaceWithNullStatus= managerToTest.getSpacesWithStatus(null);
        Assert.assertTrue(spaceWithNullStatus.isEmpty());
    }

    @Test
    public void test_getSpacesWithUnPublishedStatus_spaceDoesNotExist() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        space.setSpaceStatus(SpaceStatus.PUBLISHED);
        Space space2=new Space();
        space2.setId(spaceId2);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        spaceList.add(space2);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.PUBLISHED)).thenReturn(spaceList);
        Mockito.when(spaceRepo.findAllBySpaceStatus(null)).thenReturn(spaceList);
        List<ISpace> spaceWithNullStatus= managerToTest.getSpacesWithStatus(SpaceStatus.UNPUBLISHED);
        Assert.assertTrue(spaceWithNullStatus.isEmpty());
    }

    @Test
    public void test_getSpacesWithImageId_ImageIdIsValid(){
        Space space=new Space();
        space.setId(spaceId1);
        VSImage image = new VSImage();
        image.setId("IMG001");
        space.setImage(image);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        Mockito.when(spaceRepo.findAllByImageId("IMG001")).thenReturn(spaceList);
        Optional<VSImage> mockImage = Optional.of(image);
        Mockito.when(imageRepo.findById(image.getId())).thenReturn(mockImage);
        List<ISpace> foundSpaceWithImageId= managerToTest.getSpacesWithImageId("IMG001");
        String actualSpaceId = foundSpaceWithImageId.get(0).getId();
        Assert.assertEquals(spaceId1, actualSpaceId);
    }

    @Test
    public void test_getSpacesWithImageId_ImageIdIsInValid(){
        Space space=new Space();
        space.setId(spaceId1);
        VSImage image = new VSImage();
        image.setId("IMG001");
        space.setImage(image);
        List<Space> spaceList=new ArrayList<>();
        Mockito.when(spaceRepo.findAllByImageId("IMG002")).thenReturn(spaceList);
        Mockito.when(imageRepo.findById("IMG002")).thenReturn(Optional.empty());
        Assert.assertNull(managerToTest.getSpacesWithImageId("IMG002"));
    }

    @Test
    public void test_getSpacesWithImageId_ImageIdIsNull(){
        Assert.assertNull(managerToTest.getSpacesWithImageId(null));
    }

    @Test
    public void test_addSpaceDescription_success() {
        Space space=new Space();
        space.setId(spaceId1);

        List<LanguageDescriptionObject> descriptionList = new ArrayList();
        LanguageDescriptionObject languageObj = new LanguageDescriptionObject();
        languageObj.setText("Space description");

        ExhibitionLanguage lang = new ExhibitionLanguage();
        lang.setLabel("English");
        languageObj.setExhibitionLanguage(lang);
        descriptionList.add(languageObj);

        Mockito.when(exhibitionLanguageRepo.findByLabel(lang.getLabel())).thenReturn(lang);
        managerToTest.addSpaceDescription(space, descriptionList);
        Assert.assertEquals(space.getSpaceDescriptions().size(), 1);


    }
    
    @Test
    public void test_addSpaceName_success() {
        Space space=new Space();
        space.setId(spaceId1);
        
        List<LanguageDescriptionObject> nameList = new ArrayList();
        LanguageDescriptionObject languageObj = new LanguageDescriptionObject();
        languageObj.setText("Space Name");

        ExhibitionLanguage lang = new ExhibitionLanguage();
        lang.setLabel("English");
        languageObj.setExhibitionLanguage(lang);
        nameList.add(languageObj);

        Mockito.when(exhibitionLanguageRepo.findByLabel(lang.getLabel())).thenReturn(lang);
        managerToTest.addSpaceName(space, nameList);
        Assert.assertEquals(space.getSpaceNames().size(), 1);

    }
    
    
    @Test
    public void setNameAsDefaultLanguage() {
        Space space=new Space();
        space.setId(spaceId1);
        List<ILanguageDescriptionObject> nameList = new ArrayList();
        
        LanguageDescriptionObject languageObj1 = new LanguageDescriptionObject();
        languageObj1.setText("Space Name English");
        ExhibitionLanguage lang1 = new ExhibitionLanguage();
        lang1.setLabel("English");
        lang1.setDefault(false);
        languageObj1.setExhibitionLanguage(lang1);
        nameList.add(languageObj1);
        
        LanguageDescriptionObject languageObj2 = new LanguageDescriptionObject();
        languageObj2.setText("Raumname");
        ExhibitionLanguage lang2 = new ExhibitionLanguage();
        lang2.setLabel("German");
        lang2.setDefault(true);
        languageObj2.setExhibitionLanguage(lang1);
        nameList.add(languageObj2);
        
        Mockito.when(exhibitionLanguageRepo.findByLabel(lang1.getLabel())).thenReturn(lang1);
        Mockito.when(exhibitionLanguageRepo.findByLabel(lang2.getLabel())).thenReturn(lang2);

        space.setSpaceNames(nameList);
//        Assert.assertEquals(space.getName(), null);
        
        managerToTest.setNameAsDefaultLanguage(space);
        
        
        Assert.assertEquals(space.getName(), "Raumname");
        
        
    }

    

}
