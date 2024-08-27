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
 * SpacesCustomOrderManager manages custom ordering of exhibition spaces
 * It has methods for creating, updating, retrieving, and deleting 
 * custom orders of exhibition spaces.
 * 
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
    public ISpacesCustomOrder create(List<String> spaceOrder,
            String name,
            String description) {
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        spacesCustomOrder.setCustomOrderName(name);
        spacesCustomOrder.setDescription(description);
        List<ISpace> orderedSpaces = spaceOrder.stream()
                .map(spaceId -> spaceManager.getSpace(spaceId))
                .filter(space -> space.getSpaceStatus() == SpaceStatus.PUBLISHED )
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

    /**
     * This method adds the newly created spaces to all custom orders at the end of the list
     * @param space
     */
    @Override
    public void addSpaceToCustomOrders(ISpace space) {
        Iterable<SpacesCustomOrder> spacesCustomOrder = findAll();
        for(ISpacesCustomOrder spaceCustomOrder :  spacesCustomOrder) {
            List<ISpace> customOrderSpaces = spaceCustomOrder.getCustomOrderedSpaces();
            if(!customOrderSpaces.contains(space)) {
                spaceCustomOrder.getCustomOrderedSpaces().add(space);
            }
        }
        spacesCustomOrderRepository.saveAll(spacesCustomOrder);
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
     * This method updates list of spaces in a custom order
     * 
     * <p>
     * This method takes a list of space IDs, retrieves its corresponding {@link ISpace} objects,
     * and updates the custom order ({@code spacesCustomOrderId}) with the retrieved spaces.
     * </p>
     * 
     * @param spacesCustomOrderId - The ID of the custom space order to be updated
     * @param spacesIds - The list of space IDs to set as the custom ordered spaces
     * @return
     */
    @Override
    public void updateSpaces(String spacesCustomOrderId, List<String> spacesIds) {
        List<ISpace> spaces = new ArrayList<ISpace>();
        spacesIds.stream()
            .filter(spaceId -> spaceManager.getSpace(spaceId) != null)
            .forEach(id -> spaces.add(spaceManager.getSpace(id)));
  
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
        if(spacesCustomOrder.isPresent()) {
            exhibition.setSpacesCustomOrder(spacesCustomOrder.get());
        }            
    }
    
    /**
     * This method deletes the @{SpaceCustomOrder} object with the given id.
     * @param id
     */
    @Override
    public void delete(String id) {
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        SpacesCustomOrder spaceCustomOrder = exhibition.getSpacesCustomOrder();
        if(spaceCustomOrder!=null && spaceCustomOrder.getId().equals(id)) {
            exhibition.setSpacesCustomOrder(null);
            exhibitionManager.storeExhibition((Exhibition)exhibition);
        }
        spacesCustomOrderRepository.deleteById(id);
    }
    
    @Override
    public void removeSpaceFromAllCustomOrders(ISpace space) {
        Iterable<SpacesCustomOrder> spacesCustomOrder = findAll();
        for(SpacesCustomOrder spaceCustomOrder : spacesCustomOrder) {
            if(spaceCustomOrder.getCustomOrderedSpaces().remove(space)) {
                spacesCustomOrderRepository.save(spaceCustomOrder);
            }
        } 
    }
}
