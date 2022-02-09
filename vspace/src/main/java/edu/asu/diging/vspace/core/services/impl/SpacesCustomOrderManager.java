package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.SpacesCustomOrderRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

/**
 * SpacesCustomOrderManager is the manager
 * to allow custom ordering of spaces
 * @author Glen D'souza
 *
 */

@Service
@Transactional(rollbackFor = { Exception.class })
public class SpacesCustomOrderManager implements ISpacesCustomOrderManager {

    @Autowired
    private SpacesCustomOrderRepository spacesCustomOrderRepository;
    
    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    private static Logger logger = LoggerFactory.getLogger(SpacesCustomOrderManager.class);
    
    @Override
    public SpacesCustomOrder createNewCustomOrder(List<String> spaceOrders,
            String name,
            String description) throws IllegalStateException {
        List<SpacesCustomOrder> spacesCustomOrderRepoValue = spacesCustomOrderRepository.
                findByCustomOrderName(name);
        if(!spacesCustomOrderRepoValue.isEmpty()) {
            throw new IllegalStateException("Order with same name present");
        }
        List<ISpace> orderedSpaces= new ArrayList<ISpace>();
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        spacesCustomOrder.setCustomOrderName(name);
        spacesCustomOrder.setDescription(description);
        for(String spaceId : spaceOrders) {
            orderedSpaces.add(spaceManager.getSpace(spaceId));
        }
        spacesCustomOrder.setCustomOrderedSpaces(orderedSpaces);
        return spacesCustomOrderRepository.save(spacesCustomOrder);
    }

    
    @Override
    public List<SpacesCustomOrder> findAll(){
        return (List<SpacesCustomOrder>) spacesCustomOrderRepository.findAll();
    }
    
    @Override
    public SpacesCustomOrder getSpaceCustomOrderById(String customSpaceOrderId) {
        Optional<SpacesCustomOrder> spacesCustomOrderOptional = spacesCustomOrderRepository.findById(customSpaceOrderId);
        return spacesCustomOrderOptional.get();
    }
    
    @Override
    public void saveCustomOrders(List<SpacesCustomOrder> spacesCustomOrder) {
        spacesCustomOrderRepository.saveAll(spacesCustomOrder);
        return;
    }

    /**
     * This method adds the newly created spaces to all custom orders at the end of the list
     * @param space
     */
    @Override
    public void addSpaceToCustomOrders(ISpace space) {
        List<SpacesCustomOrder> spacesCustomOrders = findAll();
        for(SpacesCustomOrder spaceCustomOrder :  spacesCustomOrders) {
            spaceCustomOrder.getCustomOrderedSpaces().add(space);
        }
        saveCustomOrders(spacesCustomOrders);
        return; 
    }
    
    @Override
    public void updateSpacesCustomOrderName(String spacesCustomOrderId, String name) {
        SpacesCustomOrder spaceCustomOrder = getSpaceCustomOrderById(spacesCustomOrderId);
        spaceCustomOrder.setCustomOrderName(name);
        spacesCustomOrderRepository.save(spaceCustomOrder);
        return;
    }
    
    @Override
    public void updateSpacesCustomOrderDescription(String spacesCustomOrderId, String name) {
        SpacesCustomOrder spaceCustomOrder = getSpaceCustomOrderById(spacesCustomOrderId);
        spaceCustomOrder.setDescription(name);
        spacesCustomOrderRepository.save(spaceCustomOrder);
        return;
    }


    @Override
    public void editSpacesCustomOrder(String spacesCustomOrderId, List<String> spacesIds) {
        List<ISpace> spaces = new ArrayList<ISpace>();
        for(String id : spacesIds) {
            spaces.add(spaceManager.getSpace(id));
        }
        SpacesCustomOrder spaceCustomOrder = getSpaceCustomOrderById(spacesCustomOrderId);
        spaceCustomOrder.setCustomOrderedSpaces(spaces);
        spacesCustomOrderRepository.save(spaceCustomOrder);
        return;
        
    }
    
    @Override
    public void setExhibitionSpacesCustomOrder(String customOrderId) {
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        Optional<SpacesCustomOrder> spacesCustomOrder = spacesCustomOrderRepository
                .findById(customOrderId);
        exhibition.setSpacesCustomOrder(spacesCustomOrder.get());
        return;
    }
    
    @Override
    public SpacesCustomOrder getExhibitionCurrentSpacesCustomOrder() {
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        return exhibition.getSpacesCustomOrder();
    }
    
    @Override
    public void deleteSpacesCustomOrderById(String id) {
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        SpacesCustomOrder spaceCustomOrder = exhibition
                .getSpacesCustomOrder();
        if(spaceCustomOrder!=null && spaceCustomOrder.getId().equals(id)) {
            exhibition.setSpacesCustomOrder(null);
            exhibitionManager.storeExhibition((Exhibition)exhibition);
        }
        spacesCustomOrderRepository.deleteById(id);
    }

}
