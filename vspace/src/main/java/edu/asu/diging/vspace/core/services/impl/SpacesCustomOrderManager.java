package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.data.SpacesCustomOrderRepository;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
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
    private SpaceRepository spaceRepo;
    
    @Override
    public void createNewCustomOrder(String customOrderName) {
        SpacesCustomOrder spacesCustomOrder = new SpacesCustomOrder();
        List<ISpace> spaces = new ArrayList<ISpace>();
        spacesCustomOrder.setCustomOrderName(customOrderName);// use integrity violation exception
        spaceRepo.findAll().forEach((space)->spaces.add(space));
        spacesCustomOrder.setCustomOrderedSpaces(spaces);
        
        spacesCustomOrderRepository.save(spacesCustomOrder);
        return;
    }

    
    @Override
    public List<SpacesCustomOrder> findAll(){
        return (List<SpacesCustomOrder>) spacesCustomOrderRepository.findAll();
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

}
