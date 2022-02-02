package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.data.SpacesCustomOrderRepository;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;
import edu.asu.diging.vspace.web.staff.SpacesCustomOrderingController;
import edu.asu.diging.vspace.web.staff.forms.SpacesCustomOrderForm;

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
    private SpaceRepository spaceRepo;
    
    @Autowired
    private ISpaceManager spaceManager;
    
    private static Logger logger = LoggerFactory.getLogger(SpacesCustomOrderManager.class);
    
    @Override
    public SpacesCustomOrder createNewCustomOrder(SpacesCustomOrderForm spacesCustomorderForm) {
        //TODO add description to spacescustomorder
        List<ISpace> orderedSpaces= new ArrayList<ISpace>();
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        spacesCustomOrder.setCustomOrderName(spacesCustomorderForm.getName());// use integrity violation exception
        for(String spaceId : spacesCustomorderForm.getOrderedSpaces()) {
            orderedSpaces.add(spaceManager.getSpace(spaceId));
        }
        spacesCustomOrder.setCustomOrderedSpaces(orderedSpaces);
        logger.info("spaces list is {}", spacesCustomOrder.getCustomOrderedSpaces().get(0).getId());
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
    public void updateSpacesCustomOrder(SpacesCustomOrder spacesCustomOrder) {
        spacesCustomOrderRepository.save((SpacesCustomOrder) spacesCustomOrder);
    }

}
