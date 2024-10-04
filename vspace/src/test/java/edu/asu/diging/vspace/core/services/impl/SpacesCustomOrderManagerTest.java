package edu.asu.diging.vspace.core.services.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.SpacesCustomOrderRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpacesCustomOrder;
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
    
    private String spaceCustomOrderId1, spaceCustomOrderId2;
    private IExhibition exhibition = new Exhibition();
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        spaceCustomOrderId1 = "SPC000000001";
        spaceCustomOrderId2 = "SPC000000001";
    }
    
    @Test
    public void test_deleteSpaceCustomOrderById_customOrderNotSet() { 
        Mockito.when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);
        serviceToTest.delete(spaceCustomOrderId1);
        Mockito.verify(spacesCustomOrderRepository).deleteById(spaceCustomOrderId1);
    }
    
    @Test
    public void test_deleteSpaceCustomOrderById_customOrderSet() { 
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        spacesCustomOrder.setId(spaceCustomOrderId1);
        exhibition.setSpacesCustomOrder(spacesCustomOrder);
        Mockito.when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);
        serviceToTest.delete(spaceCustomOrderId1);
        Mockito.verify(spacesCustomOrderRepository).deleteById(spaceCustomOrderId1);
        Mockito.verify(exhibitionManager).storeExhibition((Exhibition)exhibition);
    }
    
    @Test
    public void test_updateSpaces_success() {
        String spaceId1 = "SPC001";
        String spaceId2 = "SPC002";
        List<String> spaceIdList = new ArrayList<String>();
        spaceIdList.add(spaceId1);
        spaceIdList.add(spaceId2);
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        when(spacesCustomOrderRepository.findById(spaceCustomOrderId1)).thenReturn(Optional.of(spacesCustomOrder));
        serviceToTest.updateSpaces(spaceCustomOrderId1, spaceIdList);
        Mockito.verify(spacesCustomOrderRepository).save(spacesCustomOrder);
    }
    
    @Test
    public void test_addSpacesToCustomOrders_success() {
        Space space = new Space();
        List<SpacesCustomOrder> spacesCustomOrders = new ArrayList<SpacesCustomOrder>();
        when(spacesCustomOrderRepository.findAll()).thenReturn(spacesCustomOrders);
        serviceToTest.addSpaceToCustomOrders(space);
        Mockito.verify(spacesCustomOrderRepository).saveAll(spacesCustomOrders);
        
    }
    
    @Test
    public void test_updateSpacesCustomOrder_updateName() {
        String spaceId1 = "SPC001";
        String spaceId2 = "SPC002";
        List<String> spaceIdList = new ArrayList<String>();
        spaceIdList.add(spaceId1);
        spaceIdList.add(spaceId2);
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        spacesCustomOrder.setCustomOrderName("hello");
        when(spacesCustomOrderRepository.findById(spaceCustomOrderId1)).thenReturn(Optional.of(spacesCustomOrder));
        serviceToTest.updateNameAndDescription(spaceCustomOrderId2, "updated", "name");
        Mockito.verify(spacesCustomOrderRepository).save(spacesCustomOrder);
    }
    
    @Test
    public void test_updateSpacesCustomOrder_updateDescription() {
        String spaceId1 = "SPC001";
        String spaceId2 = "SPC002";
        List<String> spaceIdList = new ArrayList<String>();
        spaceIdList.add(spaceId1);
        spaceIdList.add(spaceId2);
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        spacesCustomOrder.setCustomOrderName("hello");
        when(spacesCustomOrderRepository.findById(spaceCustomOrderId1)).thenReturn(Optional.of(spacesCustomOrder));
        serviceToTest.updateNameAndDescription(spaceCustomOrderId2, "updated", "description");
        Mockito.verify(spacesCustomOrderRepository).save(spacesCustomOrder);
    }
    
}