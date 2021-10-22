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
import java.util.Optional;

import edu.asu.diging.vspace.core.data.SpacesCustomOrderRepository;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.ISpaceManager;

public class SpacesCustomOrderManagerTest{
    
    @Mock
    private SpacesCustomOrderRepository spacesCustomOrderRepository;
    
    @Mock
    private ISpaceManager spaceManager;
    
    @InjectMocks
    private SpacesCustomOrderManager serviceToTest;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }
    
   @Test
   public void test_findMaxCustomOrder_success() {
       String id="1";
       Integer order = 1;
       when(spacesCustomOrderRepository.findMaxCustomOrder(id)).thenReturn(order);
       
       Integer maxCustomOrder = serviceToTest.findMaxCustomOrder(id);
       Assert.assertEquals(maxCustomOrder,order);
   }
   
   @Test
   public void test_updateCustomOrder_success() {
       SpacesCustomOrder firstSpace = new SpacesCustomOrder();
       firstSpace.setId("SPAC01");
       firstSpace.setCustomOrder(Integer.valueOf(3));

       SpacesCustomOrder secondSpace = new SpacesCustomOrder();
       secondSpace.setId("SPAC02");
       secondSpace.setCustomOrder(Integer.valueOf(4));

       List<SpacesCustomOrder> spacesCustomOrder = new ArrayList<>();
       spacesCustomOrder.add(firstSpace);
       spacesCustomOrder.add(secondSpace);
       Optional<SpacesCustomOrder> optionalSpacesCustomOrder;

       when(spacesCustomOrderRepository.findById("SPAC01")).thenReturn(Optional.of(firstSpace));
       when(spacesCustomOrderRepository.findById("SPAC02")).thenReturn(Optional.of(secondSpace));

       serviceToTest.updateCustomOrder(spacesCustomOrder);
       assertEquals(Integer.valueOf(3), firstSpace.getCustomOrder());
       assertEquals(Integer.valueOf(4), secondSpace.getCustomOrder());

       List<SpacesCustomOrder> SpacesCustomList = new ArrayList<>();
       SpacesCustomList.add(firstSpace);
       SpacesCustomList.add(secondSpace);

       Mockito.verify(spacesCustomOrderRepository).saveAll(SpacesCustomList);
   }
   
   @Test
   public void test_persistPublishedSpacesToSpacesCustomOrder_success() {
       SpacesCustomOrder firstSpaceCustomOrder = new SpacesCustomOrder();
       firstSpaceCustomOrder.setId("SPAC01");
       firstSpaceCustomOrder.setCustomOrder(Integer.valueOf(3));
       ISpace space1 = new Space();
       space1.setId("space1");
       space1.setSpaceStatus(SpaceStatus.PUBLISHED);
       ISpace space2 = new Space();
       space2.setId("space2");
       List<ISpace> spaces = new ArrayList<ISpace>();
       spaces.add(space1);
       spaces.add(space2);
       when(spaceManager.getSpacesWithStatus(SpaceStatus.PUBLISHED)).thenReturn(spaces);
       when(spaceManager.getSpacesWithStatus(null)).thenReturn(spaces);
       
       when(spacesCustomOrderRepository.findBySpace_Id(Mockito.anyString())).thenReturn(Optional.of(firstSpaceCustomOrder));

       when(spacesCustomOrderRepository.findMaxCustomOrder()).thenReturn(null);
       
       serviceToTest.persistPublishedSpacesToSpacesCustomOrder();
   }
   
   @Test
   public void test_updateStatusChangeToUnpublished_success() {
       String space1Id="Space1";
       ISpace space = new Space();
       space.setId(space1Id);
       SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
       spacesCustomOrder.setCustomOrder(1);
       SpacesCustomOrder spacesCustomOrder2 = new SpacesCustomOrder();
       spacesCustomOrder2.setCustomOrder(2);
       List<SpacesCustomOrder> spacesCustomOrderList = new ArrayList<SpacesCustomOrder>();
       spacesCustomOrderList.add(spacesCustomOrder2);
       when(spacesCustomOrderRepository.findBySpace_Id(space1Id)).thenReturn(Optional.of(spacesCustomOrder));
       when(spacesCustomOrderRepository.findBySpace_IdAndCustomOrderGreaterThan(space1Id,1)).thenReturn(spacesCustomOrderList);
       doNothing().when(spacesCustomOrderRepository).delete(spacesCustomOrder);
       
       assertEquals(Integer.valueOf(2), spacesCustomOrder2.getCustomOrder());
       serviceToTest.updateStatusChangeToUnpublished(space);    
   }
   
    
}