package edu.asu.diging.vspace.core.services.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import edu.asu.diging.vspace.core.data.SpacesCustomOrderRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
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
    public void test_deleteSpaceCustomOrderByIdWithNullSpacesCustomOrder_forSuccess() { 
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
    
    @Test
    public void test_editSpacesCustomOrders_Success() {
        String spaceId1 = "SPC001";
        String spaceId2 = "SPC002";
        List<String> spaceIdList = new ArrayList<String>();
        spaceIdList.add(spaceId1);
        spaceIdList.add(spaceId2);
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        when(spacesCustomOrderRepository.findById(spaceCustomOrderId1)).thenReturn(Optional.of(spacesCustomOrder));
        serviceToTest.editSpacesCustomOrder(spaceCustomOrderId1, spaceIdList);
        Mockito.verify(spacesCustomOrderRepository).save(spacesCustomOrder);
    }
    
    @Test
    public void test_addSpacesToCustomOrders_Success() {
        Space space = new Space();
        List<SpacesCustomOrder> spacesCustomOrders = new ArrayList<SpacesCustomOrder>();
        when(spacesCustomOrderRepository.findAll()).thenReturn(spacesCustomOrders);
        serviceToTest.addSpaceToCustomOrders(space);
        Mockito.verify(spacesCustomOrderRepository).saveAll(spacesCustomOrders);
        
    }
    
    @Test
    public void test_updateSpacesCustomOrderName_Success() {
        String spaceId1 = "SPC001";
        String spaceId2 = "SPC002";
        List<String> spaceIdList = new ArrayList<String>();
        spaceIdList.add(spaceId1);
        spaceIdList.add(spaceId2);
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        spacesCustomOrder.setCustomOrderName("hello");
        when(spacesCustomOrderRepository.findById(spaceCustomOrderId1)).thenReturn(Optional.of(spacesCustomOrder));
        serviceToTest.updateSpacesCustomOrderName(spaceCustomOrderId2, "updated");
        Mockito.verify(spacesCustomOrderRepository).save(spacesCustomOrder);
    }
    
    @Test
    public void test_updateSpacesCustomOrderDescription_Success() {
        String spaceId1 = "SPC001";
        String spaceId2 = "SPC002";
        List<String> spaceIdList = new ArrayList<String>();
        spaceIdList.add(spaceId1);
        spaceIdList.add(spaceId2);
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        spacesCustomOrder.setCustomOrderName("hello");
        when(spacesCustomOrderRepository.findById(spaceCustomOrderId1)).thenReturn(Optional.of(spacesCustomOrder));
        serviceToTest.updateSpacesCustomOrderDescription(spaceCustomOrderId2, "updated");
        Mockito.verify(spacesCustomOrderRepository).save(spacesCustomOrder);
    }
    
}