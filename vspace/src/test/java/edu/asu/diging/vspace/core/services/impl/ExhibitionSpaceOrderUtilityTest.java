package edu.asu.diging.vspace.core.services.impl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.model.ExhibitionSpaceOrderMode;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.SpacesCustomOrder;
import edu.asu.diging.vspace.core.services.IExhibitionManager;

public class ExhibitionSpaceOrderUtilityTest {
    
    @Mock
    private  IExhibitionManager exhibitionManager;
    
    @InjectMocks
    private ExhibitionSpaceOrderUtility exhibitionSpaceOrderUtility;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_sortSpaces_creationDate() {
        ISpace space1 = new Space();
        space1.setCreationDate(OffsetDateTime.now());
        ISpace space2 = new Space();
        space2.setCreationDate(OffsetDateTime.now());
        List<ISpace> publishedSpaces = new ArrayList<ISpace>();
        publishedSpaces.add(space2);
        publishedSpaces.add(space1);
        List<ISpace> actualSpaces = exhibitionSpaceOrderUtility.sortSpaces(publishedSpaces, ExhibitionSpaceOrderMode.CREATION_DATE);
        publishedSpaces.clear();
        publishedSpaces.add(space2);
        publishedSpaces.add(space1);
        Assert.assertEquals(publishedSpaces, actualSpaces);
    }
    
    @Test
    public void test_sortSpaces_alphabetical() {
        ISpace space1 = new Space();
        space1.setName("Name 1");
        ISpace space2 = new Space();
        space2.setName("Name 2");
        List<ISpace> publishedSpaces = new ArrayList<ISpace>();
        publishedSpaces.add(space2);
        publishedSpaces.add(space1);
        List<ISpace> actualSpaces = exhibitionSpaceOrderUtility.sortSpaces(publishedSpaces, ExhibitionSpaceOrderMode.ALPHABETICAL);
        publishedSpaces.clear();
        publishedSpaces.add(space1);
        publishedSpaces.add(space2);
        Assert.assertEquals(publishedSpaces, actualSpaces);
    }
    
    @Test
    public void test_sortSpaces_customOrder() {
        ISpace space1 = new Space();
        space1.setName("Name 1");
        space1.setSpaceStatus(SpaceStatus.PUBLISHED);
        ISpace space2 = new Space();
        space2.setName("Name 2");
        space2.setSpaceStatus(SpaceStatus.PUBLISHED);
        List<ISpace> publishedSpaces = new ArrayList<ISpace>();
        publishedSpaces.add(space2);
        publishedSpaces.add(space1);
        
        IExhibition exhibition = new Exhibition();
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        spacesCustomOrder.setCustomOrderedSpaces(publishedSpaces);
        exhibition.setSpacesCustomOrder(spacesCustomOrder);
        Mockito.when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);
        
        List<ISpace> actualSpaces = exhibitionSpaceOrderUtility.sortSpaces(publishedSpaces, ExhibitionSpaceOrderMode.CUSTOM);
        Assert.assertEquals(publishedSpaces, actualSpaces);
    }
    
    @Test
    public void test_sortSpaces_customOrderNotSet() {
        ISpace space1 = new Space();
        space1.setName("Name 1");
        space1.setSpaceStatus(SpaceStatus.PUBLISHED);
        ISpace space2 = new Space();
        space2.setName("Name 2");
        space2.setSpaceStatus(SpaceStatus.PUBLISHED);
        List<ISpace> publishedSpaces = new ArrayList<ISpace>();
        publishedSpaces.add(space2);
        publishedSpaces.add(space1);
        
        IExhibition exhibition = new Exhibition();
        Mockito.when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);
        
        List<ISpace> actualSpaces = exhibitionSpaceOrderUtility.sortSpaces(publishedSpaces, ExhibitionSpaceOrderMode.CUSTOM);
        publishedSpaces.clear();
        publishedSpaces.add(space1);
        publishedSpaces.add(space2);
        Assert.assertEquals(publishedSpaces, actualSpaces);
    }
}