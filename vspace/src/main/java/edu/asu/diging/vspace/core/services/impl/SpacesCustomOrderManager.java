package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.SpacesCustomOrderRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.SpacesCustomOrder;
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
    
    @Override
    public ISpacesCustomOrder createNewCustomOrder(List<String> spaceOrders,
            String name,
            String description) {
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
    public Iterable<SpacesCustomOrder> findAll(){
        return spacesCustomOrderRepository.findAll();
    }
    
    @Override
    public ISpacesCustomOrder getSpaceCustomOrderById(String customSpaceOrderId) {
        Optional<SpacesCustomOrder> spacesCustomOrderOptional = spacesCustomOrderRepository.findById(customSpaceOrderId);
        return spacesCustomOrderOptional.get();
    }
    
    @Override
    public void saveCustomOrders(Iterable<SpacesCustomOrder> spacesCustomOrder) {
        spacesCustomOrderRepository.saveAll(spacesCustomOrder);
    }

    /**
     * This method adds the newly created spaces to all custom orders at the end of the list
     * @param space
     */
    @Override
    public void addSpaceToCustomOrders(ISpace space) {
        Iterable<SpacesCustomOrder> spacesCustomOrders = findAll();
        for(ISpacesCustomOrder spaceCustomOrder :  spacesCustomOrders) {
            spaceCustomOrder.getCustomOrderedSpaces().add(space);
        }
        saveCustomOrders(spacesCustomOrders);
    }
    
    /**
     *This method updates the custom order name or description
     */
    @Override
    public void updateSpacesCustomOrderNameDescription(String spacesCustomOrderId, String title, String description) {
        ISpacesCustomOrder spaceCustomOrder = getSpaceCustomOrderById(spacesCustomOrderId);
        spaceCustomOrder.setCustomOrderName(title);
        spaceCustomOrder.setDescription(description);
        spacesCustomOrderRepository.save((SpacesCustomOrder)spaceCustomOrder);
    }

    /**
     * This method edits the custom order
     * @param spacesCustomOrderId
     * @param spacesIds
     */
    @Override
    public void editSpacesCustomOrder(String spacesCustomOrderId, List<String> spacesIds) {
        List<ISpace> spaces = new ArrayList<ISpace>();
        for(String id : spacesIds) {
            spaces.add(spaceManager.getSpace(id));
        }
        ISpacesCustomOrder spaceCustomOrder = getSpaceCustomOrderById(spacesCustomOrderId);
        spaceCustomOrder.setCustomOrderedSpaces(spaces);
        spacesCustomOrderRepository.save((SpacesCustomOrder)spaceCustomOrder);
        
    }
    
    /**
     * This method sets the custom order to be used on exhibition page
     * @param customOrderId
     */
    @Override
    public void setExhibitionSpacesCustomOrder(String customOrderId) {
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        Optional<SpacesCustomOrder> spacesCustomOrder = spacesCustomOrderRepository
                .findById(customOrderId);
        exhibition.setSpacesCustomOrder(spacesCustomOrder.get());
    }
    
    /**
     * This method deletes the custom order by id
     * @param id
     */
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
