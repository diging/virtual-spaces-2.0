package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.SpacesCustomOrderRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
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
    public ISpacesCustomOrder create(List<String> spaceOrders,
            String name,
            String description) {
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        spacesCustomOrder.setCustomOrderName(name);
        spacesCustomOrder.setDescription(description);
        List<ISpace> orderedSpaces = spaceOrders.stream()
                .map(spaceId -> spaceManager.getSpace(spaceId))
                .collect(Collectors.toList());
        spacesCustomOrder.setCustomOrderedSpaces(orderedSpaces);
        return spacesCustomOrderRepository.save(spacesCustomOrder);
    }

    
    @Override
    public Iterable<SpacesCustomOrder> findAll(){
        return spacesCustomOrderRepository.findAll();
    }
    
    @Override
    public ISpacesCustomOrder get(String customSpaceOrderId) {
        Optional<SpacesCustomOrder> spacesCustomOrderOptional = spacesCustomOrderRepository.findById(customSpaceOrderId);
        if(spacesCustomOrderOptional.isPresent()) {
            return spacesCustomOrderOptional.get();
        }          
        return null;
    }
    
    @Override
    public void save(Iterable<SpacesCustomOrder> spacesCustomOrder) {
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
            List<ISpace> customOrderSpaces = spaceCustomOrder.getCustomOrderedSpaces();
            if(!customOrderSpaces.contains(space)) {
                spaceCustomOrder.getCustomOrderedSpaces().add(space);
            }
        }
        save(spacesCustomOrders);
    }
    
  

    @Override
    /**
     * This method updates the custom order name and description
     * @param spacesCustomOrderId
     * @param title
     * @param description
     */
    public void updateNameAndDescription(String spacesCustomOrderId, String title, String description) {
        ISpacesCustomOrder spaceCustomOrder = get(spacesCustomOrderId);
        spaceCustomOrder.setCustomOrderName(title);
        spaceCustomOrder.setDescription(description);
        spacesCustomOrderRepository.save((SpacesCustomOrder)spaceCustomOrder);
    }

    /**
     * This method updated custom order of spaces
     * @param spacesCustomOrderId
     * @param spacesIds
     */
    @Override
    public void updateSpaces(String spacesCustomOrderId, List<String> spacesIds) {
        List<ISpace> spaces = new ArrayList<ISpace>();
        for(String spaceId: spacesIds) {
            if(spaceManager.getSpace(spaceId) != null){
                spaces.add(spaceManager.getSpace(spaceId));
            }
        }
        ISpacesCustomOrder spaceCustomOrder = get(spacesCustomOrderId);
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
        if(spacesCustomOrder.isPresent())
            exhibition.setSpacesCustomOrder(spacesCustomOrder.get());
    }
    
    /**
     * This method deletes the @{SpaceCustomOrder} object with the given id.
     * @param id
     */
    @Override
    public void delete(String id) {
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
