package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.SpaceTextBlockRepository;
import edu.asu.diging.vspace.core.data.display.SpaceTextBlockDisplayRepository;
import edu.asu.diging.vspace.core.factory.ISpaceTextBlockDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceTextBlockFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceTextBlockDisplay;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceTextBlock;
import edu.asu.diging.vspace.core.services.ISpaceManager;

public class SpaceTextBlockManagerTest {
    @Mock
    private ISpaceManager spaceManager;

    @Mock
    private ISpaceTextBlockFactory spaceTextBlockFactory;

    @Mock
    private ISpaceTextBlockDisplayFactory spaceTextBlockDisplayFactory;

    @Mock
    private SpaceTextBlockRepository spaceTextBlockRepo;

    @Mock
    private SpaceTextBlockDisplayRepository spaceTextBlockDisplayRepo;

    @InjectMocks
    private SpaceTextBlockManager managerToTest = new SpaceTextBlockManager();

    private String spaceId1;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        spaceId1 = "SPA000000001";
    }

    @Test
    public void test_createTextBlock_success(){

        Space space = new Space();
        space.setId(spaceId1);
        
        SpaceTextBlock spaceTextBlock = new SpaceTextBlock();
        spaceTextBlock.setId("SPB001");
        
        SpaceTextBlockDisplay spaceTextBlockDisplay = new SpaceTextBlockDisplay();
        spaceTextBlockDisplay.setId("STBD001");
        spaceTextBlockDisplay.setPositionX(10);
        spaceTextBlockDisplay.setPositionY(30);
        spaceTextBlockDisplay.setWidth(40);
        spaceTextBlockDisplay.setHeight(50);
        spaceTextBlockDisplay.setSpaceTextBlock(spaceTextBlock);
        
        Mockito.when(spaceManager.getSpace(spaceId1)).thenReturn(space);
        Mockito.when(spaceTextBlockFactory.createSpaceTextBlock("New Text Block", space)).thenReturn(spaceTextBlock);
        Mockito.when(spaceTextBlockDisplayFactory.createSpaceTextBlockDisplay(spaceTextBlock, 10, 30, 40, 50, "#000000", "#000000")).thenReturn(spaceTextBlockDisplay);
        Mockito.when(spaceTextBlockRepo.save((SpaceTextBlock)spaceTextBlock)).thenReturn((SpaceTextBlock) spaceTextBlock);
        Mockito.when(spaceTextBlockDisplayRepo.save((SpaceTextBlockDisplay)spaceTextBlockDisplay)).thenReturn((SpaceTextBlockDisplay)spaceTextBlockDisplay);

        ISpaceTextBlockDisplay savedSpaceTextBlockDisplay = managerToTest.createTextBlock(spaceId1, 10, 30, "New Text Block", 40, 50, "#000000", "#000000");
        Assert.assertEquals(spaceTextBlockDisplay.getId(), savedSpaceTextBlockDisplay.getId());
        Assert.assertEquals(spaceTextBlockDisplay.getName(), savedSpaceTextBlockDisplay.getName());
        Assert.assertEquals(new Double(spaceTextBlockDisplay.getPositionX()), new Double(savedSpaceTextBlockDisplay.getPositionX()));
        Assert.assertEquals(new Double(spaceTextBlockDisplay.getPositionY()), new Double(savedSpaceTextBlockDisplay.getPositionY()));
        Assert.assertEquals(new Float(spaceTextBlockDisplay.getWidth()), new Float(savedSpaceTextBlockDisplay.getWidth()));
        Assert.assertEquals(new Float(spaceTextBlockDisplay.getHeight()), new Float(savedSpaceTextBlockDisplay.getHeight()));
        Assert.assertEquals(spaceTextBlockDisplay.getSpaceTextBlock(), savedSpaceTextBlockDisplay.getSpaceTextBlock());
        Assert.assertEquals(spaceTextBlockDisplay.getTextColor(), savedSpaceTextBlockDisplay.getTextColor());
        Assert.assertEquals(spaceTextBlockDisplay.getBorderColor(), savedSpaceTextBlockDisplay.getBorderColor());

        Mockito.verify(spaceTextBlockDisplayRepo).save((SpaceTextBlockDisplay)spaceTextBlockDisplay);
    }
    
    @Test
    public void test_updateTextBlock_success(){

        ISpace space = new Space();
        space.setId(spaceId1);
        
        SpaceTextBlock spaceTextBlock = new SpaceTextBlock();
        spaceTextBlock.setId("SPB001");
        
        SpaceTextBlockDisplay spaceTextBlockDisplay = new SpaceTextBlockDisplay();
        spaceTextBlockDisplay.setId("STBD001");
        spaceTextBlockDisplay.setPositionX(10);
        spaceTextBlockDisplay.setPositionY(30);
        spaceTextBlockDisplay.setWidth(40);
        spaceTextBlockDisplay.setHeight(50);
        spaceTextBlockDisplay.setSpaceTextBlock(spaceTextBlock);
        
        Optional<SpaceTextBlock> mockSpaceTextBlock = Optional.of(spaceTextBlock);
        Optional<SpaceTextBlockDisplay> mockSpaceTextBlockDisplay = Optional.of(spaceTextBlockDisplay);
        
        Mockito.when(spaceTextBlockRepo.findById(spaceTextBlock.getId())).thenReturn(mockSpaceTextBlock);
        Mockito.when(spaceTextBlockDisplayRepo.findById(spaceTextBlockDisplay.getId())).thenReturn(mockSpaceTextBlockDisplay);
        
        Mockito.when(spaceTextBlockRepo.save(spaceTextBlock)).thenReturn(spaceTextBlock);
        Mockito.when(spaceTextBlockDisplayRepo.save(spaceTextBlockDisplay)).thenReturn(spaceTextBlockDisplay);
    

        ISpaceTextBlockDisplay updatedSpaceTextBlockDisplay = managerToTest.updateTextBlock(spaceId1, 10, 30, "SPB001", "STBD001", "New Text Block", 40, 50, null, "#000000");
        Assert.assertEquals(spaceTextBlockDisplay.getId(), updatedSpaceTextBlockDisplay.getId());
        Assert.assertEquals(spaceTextBlockDisplay.getName(), updatedSpaceTextBlockDisplay.getName());
        Assert.assertEquals(new Double(spaceTextBlockDisplay.getPositionX()), new Double(updatedSpaceTextBlockDisplay.getPositionX()));
        Assert.assertEquals(new Double(spaceTextBlockDisplay.getPositionY()), new Double(updatedSpaceTextBlockDisplay.getPositionY()));
        Assert.assertEquals(new Float(spaceTextBlockDisplay.getWidth()), new Float(updatedSpaceTextBlockDisplay.getWidth()));
        Assert.assertEquals(new Float(spaceTextBlockDisplay.getHeight()), new Float(updatedSpaceTextBlockDisplay.getHeight()));
        Assert.assertEquals(spaceTextBlockDisplay.getSpaceTextBlock(), updatedSpaceTextBlockDisplay.getSpaceTextBlock());
        Assert.assertEquals(spaceTextBlockDisplay.getTextColor(), updatedSpaceTextBlockDisplay.getTextColor());
        Assert.assertEquals(spaceTextBlockDisplay.getBorderColor(), updatedSpaceTextBlockDisplay.getBorderColor());
    }
    
    @Test
    public void test_updateTextBlock_textBlockIdEmpty_failed(){
        
        SpaceTextBlock spaceTextBlock = new SpaceTextBlock();
        spaceTextBlock.setId("SPB001");
        
        SpaceTextBlockDisplay spaceTextBlockDisplay = new SpaceTextBlockDisplay();
        spaceTextBlockDisplay.setId("STBD001");
        spaceTextBlockDisplay.setPositionX(10);
        spaceTextBlockDisplay.setPositionY(30);
        spaceTextBlockDisplay.setWidth(40);
        spaceTextBlockDisplay.setHeight(50);
        spaceTextBlockDisplay.setSpaceTextBlock(spaceTextBlock);
        
        Optional<SpaceTextBlock> mockSpaceTextBlock = Optional.of(spaceTextBlock);
        Optional<SpaceTextBlockDisplay> mockSpaceTextBlockDisplay = Optional.of(spaceTextBlockDisplay);
        
        Mockito.when(spaceTextBlockRepo.findById(spaceTextBlock.getId())).thenReturn(Optional.empty());
        Mockito.when(spaceTextBlockDisplayRepo.findById(spaceTextBlockDisplay.getId())).thenReturn(mockSpaceTextBlockDisplay);
        
        managerToTest.updateTextBlock("", 10, 30, "SPB001", "STBD001", "test", 50, 40, spaceId1, null);
        Mockito.verify(spaceTextBlockRepo, Mockito.never()).save(spaceTextBlock);
    }
    
    @Test
    public void test_updateTextBlock_textBlockDisplayIdEmpty_failed(){
        
        SpaceTextBlock spaceTextBlock = new SpaceTextBlock();
        spaceTextBlock.setId("SPB001");
        
        SpaceTextBlockDisplay spaceTextBlockDisplay = new SpaceTextBlockDisplay();
        spaceTextBlockDisplay.setId("STBD001");
        spaceTextBlockDisplay.setPositionX(10);
        spaceTextBlockDisplay.setPositionY(30);
        spaceTextBlockDisplay.setWidth(40);
        spaceTextBlockDisplay.setHeight(50);
        spaceTextBlockDisplay.setSpaceTextBlock(spaceTextBlock);
        
        Optional<SpaceTextBlock> mockSpaceTextBlock = Optional.of(spaceTextBlock);
        
        Mockito.when(spaceTextBlockRepo.findById(spaceTextBlock.getId())).thenReturn(mockSpaceTextBlock);
        Mockito.when(spaceTextBlockDisplayRepo.findById(spaceTextBlockDisplay.getId())).thenReturn(Optional.empty());
        
        managerToTest.updateTextBlock("", 10, 30, "SPB001", "STBD001", "test", 50, 40, null, spaceId1);
        Mockito.verify(spaceTextBlockDisplayRepo, Mockito.never()).save(spaceTextBlockDisplay);
    }
    
    @Test
    public void test_getSpaceTextBlockDisplays_success() {
        
        ISpace space = new Space();
        space.setId(spaceId1);
        
        SpaceTextBlock spaceTextBlock = new SpaceTextBlock();
        spaceTextBlock.setId("SPB001");
        
        SpaceTextBlockDisplay spaceTextBlockDisplay = new SpaceTextBlockDisplay();
        spaceTextBlockDisplay.setId("STBD001");
        spaceTextBlockDisplay.setPositionX(10);
        spaceTextBlockDisplay.setPositionY(30);
        spaceTextBlockDisplay.setWidth(40);
        spaceTextBlockDisplay.setHeight(50);
        spaceTextBlockDisplay.setSpaceTextBlock(spaceTextBlock);
        
        List<ISpaceTextBlockDisplay> spaceTextBlockDisplayList = new ArrayList<ISpaceTextBlockDisplay>();
        spaceTextBlockDisplayList.add(spaceTextBlockDisplay);
        
        Mockito.when(spaceTextBlockDisplayRepo.findSpaceTextBlockDisplaysForSpace(spaceId1)).thenReturn(spaceTextBlockDisplayList);

        List<ISpaceTextBlockDisplay> spaceTextBlockDisplayListActual = managerToTest.getSpaceTextBlockDisplays(spaceId1);
        Assert.assertEquals(spaceTextBlockDisplayListActual.get(0).getId(), spaceTextBlockDisplayList.get(0).getId());
        Assert.assertEquals(spaceTextBlockDisplayListActual.get(0).getSpaceTextBlock(), spaceTextBlockDisplayList.get(0).getSpaceTextBlock());
    }
    
    @Test
    public void test_getSpaceTextBlockDisplays_failed() {
        
        List<ISpaceTextBlockDisplay> spaceTextBlockDisplayList = new ArrayList<ISpaceTextBlockDisplay>();
        
        Mockito.when(spaceTextBlockDisplayRepo.findSpaceTextBlockDisplaysForSpace(spaceId1)).thenReturn(spaceTextBlockDisplayList);

        List<ISpaceTextBlockDisplay> spaceTextBlockDisplayListActual = managerToTest.getSpaceTextBlockDisplays(spaceId1);
        Assert.assertTrue(spaceTextBlockDisplayListActual.isEmpty());
    }
    
    @Test
    public void test_deleteTextBlock_success() {
        
        SpaceTextBlock spaceTextBlock = new SpaceTextBlock();
        spaceTextBlock.setId("SPB001");
        
        Optional<SpaceTextBlock> mockSpaceTextBlock = Optional.of(spaceTextBlock);
        
        Mockito.when(spaceTextBlockRepo.findById("SPB001")).thenReturn(mockSpaceTextBlock);
        
        managerToTest.deleteTextBlock("SPB001");
        
        Mockito.verify(spaceTextBlockDisplayRepo).deleteBySpaceTextBlock(mockSpaceTextBlock.get());
        Mockito.verify(spaceTextBlockRepo).delete(mockSpaceTextBlock.get());
    }
    
    @Test
    public void test_deleteTextBlock_failed() {
        
        SpaceTextBlock spaceTextBlock = new SpaceTextBlock();
        spaceTextBlock.setId("SPB002");
        
        Optional<SpaceTextBlock> mockSpaceTextBlock = Optional.of(spaceTextBlock);
        
        Mockito.when(spaceTextBlockRepo.findById("SPB001")).thenReturn(Optional.empty());
        
        managerToTest.deleteTextBlock("SPB001");
        
        Mockito.verify(spaceTextBlockDisplayRepo, Mockito.never()).deleteBySpaceTextBlock(mockSpaceTextBlock.get());
        Mockito.verify(spaceTextBlockRepo, Mockito.never()).delete(mockSpaceTextBlock.get());
    }

}