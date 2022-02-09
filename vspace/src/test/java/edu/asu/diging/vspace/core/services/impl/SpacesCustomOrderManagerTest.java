package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import edu.asu.diging.vspace.core.data.SpacesCustomOrderRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

public class SpacesCustomOrderManagerTest{
    
    @Mock
    private SpacesCustomOrderRepository spacesCustomOrderRepository;
    
    @Mock
    private ISpaceManager spaceManager;
    
    @Mock
    private IExhibitionManager exhibitionManager;
    
    @InjectMocks
    private SpacesCustomOrderManager serviceToTest;
    
    @Spy
    private SpacesCustomOrderManager spyManager;
    
    private String spaceId = "spaceId";
    private String spaceCustomOrderId1, spaceCustomOrderId2;
    private IExhibition exhibition = new Exhibition();
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        spaceCustomOrderId1 = "SPC000000001";
        spaceCustomOrderId2 = "SPC000000001";

    }
    
    @Test
    public void test_deleteSpaceCustomOrderByIdWithNullSpacesCustomOrder_forSuccess() { 
        SpacesCustomOrder spacesCustomOrder = null;
        Mockito.when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);
        serviceToTest.deleteSpacesCustomOrderById(spaceCustomOrderId1);
        Mockito.verify(spacesCustomOrderRepository).deleteById(spaceCustomOrderId1);
    }
    
    @Test
    public void test_deleteSpaceCustomOrderByIdWithNotNullSpacesCustomOrder_forSuccess() { 
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        spacesCustomOrder.setId(spaceCustomOrderId1);
        exhibition.setSpacesCustomOrder(spacesCustomOrder);
        Mockito.when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);
        serviceToTest.deleteSpacesCustomOrderById(spaceCustomOrderId1);
        Mockito.verify(spacesCustomOrderRepository).deleteById(spaceCustomOrderId1);
        Mockito.verify(exhibitionManager).storeExhibition((Exhibition)exhibition);
    }
    
//    @Test
//    public void test_editSpacesCustomOrders_forSuccess() {
//        List<String> spaceIds = new ArrayList<String>();
//        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
//        SpacesCustomOrderManager classToTest = null;
//        SpacesCustomOrderManager spy = Mockito.spy(classToTest);
//        spaceIds.add("SPC001");
//        spaceIds.add("SPC002");
//        Mockito.doReturn(spacesCustomOrder).when(spy).getSpaceCustomOrderById(spaceCustomOrderId1);
//        serviceToTest.editSpacesCustomOrder(spaceCustomOrderId1, spaceIds);
//        Mockito.verify(spacesCustomOrderRepository).save(spacesCustomOrder);
//    }
    

   
    
}