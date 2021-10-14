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
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

@Service
@Transactional(rollbackFor = { Exception.class })
public class SpacesCustomOrderManager implements ISpacesCustomOrderManager {

    @Autowired
    SpacesCustomOrderRepository spacesCustomOrderRepository;
    
    @Autowired
    ISpaceManager spaceManager;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public Integer findMaxCustomOrder(String spaceId) {
        
        return spacesCustomOrderRepository.findMaxCustomOrder(spaceId);
    }
    
    @Override
    public void updateCustomOrder(List<SpacesCustomOrder> spacesCustomOrderCurrentList) {

        if (spacesCustomOrderCurrentList == null) {
            return;
        }
        List<SpacesCustomOrder> spacesCustomOrderNewList = new ArrayList<>();
        for (SpacesCustomOrder eachSpace : spacesCustomOrderCurrentList) {
            String spaceId = eachSpace.getId();
            int customOrder = eachSpace.getCustomOrder();
            Optional<SpacesCustomOrder> spaceCustomOrderOptional = spacesCustomOrderRepository.findById(spaceId);
            if (spaceCustomOrderOptional.isPresent()) {
                SpacesCustomOrder spaceCustomOrder = spaceCustomOrderOptional.get();
                spaceCustomOrder.setCustomOrder(customOrder);
                spacesCustomOrderNewList.add(spaceCustomOrder);
            }
        }
        spacesCustomOrderRepository.saveAll(spacesCustomOrderNewList);
    }
    
    @Override
    public void persistPublishedSpacesToSpacesCustomOrder() {
        List<ISpace> spaces = (List<ISpace>) spaceManager.getSpacesWithStatus(SpaceStatus.PUBLISHED);
        spaces.addAll(spaceManager.getSpacesWithStatus(null));
        List<SpacesCustomOrder> spacesCustomOrderNewList = new ArrayList<>();
        int customOrder = 0;
        if(spacesCustomOrderRepository.findMaxCustomOrder() != null) {
            customOrder = spacesCustomOrderRepository.findMaxCustomOrder();
        }
        for(ISpace space : spaces) {
            Optional<SpacesCustomOrder> spaceCustomOrderOptional = spacesCustomOrderRepository.findBySpace_Id(space.getId());
            if(!spaceCustomOrderOptional.isPresent()) {
                spacesCustomOrderNewList.add(new SpacesCustomOrder(space, ++customOrder));
                logger.info("custom order is {}", customOrder);
            }
        }
        spacesCustomOrderRepository.saveAll(spacesCustomOrderNewList);
    }
    
    @Override
    public List<SpacesCustomOrder> findAll(){
        return (List<SpacesCustomOrder>) spacesCustomOrderRepository.findAll();
    }
    
    @Override
    public void updateStatusChange(ISpace space, SpaceStatus status) {
        if(status == SpaceStatus.PUBLISHED || status == null) {
            updateStatusChangeToPublished(space);
        }else {
            updateStatusChangeToUnpublished(space);
        }
    }
    
    @Override
    public void updateStatusChangeToPublished(ISpace space) {
        Optional<SpacesCustomOrder> spaceCustomOrderOptional = spacesCustomOrderRepository.findBySpace_Id(space.getId());
        if(!spaceCustomOrderOptional.isPresent()) {
            int order = spacesCustomOrderRepository.findMaxCustomOrder() + 1;
            SpacesCustomOrder spaceCustomOrder = new SpacesCustomOrder(space, order);
            spaceCustomOrder.setCustomOrder(order);
            spacesCustomOrderRepository.save(spaceCustomOrder);
        }
        
    }
    
    @Override
    public void updateStatusChangeToUnpublished(ISpace space) {
        Optional<SpacesCustomOrder> spaceCustomOrderOptional = spacesCustomOrderRepository.findBySpace_Id(space.getId());
        if(spaceCustomOrderOptional.isPresent()) {
            SpacesCustomOrder spaceCustomOrder = spaceCustomOrderOptional.get();
            int order = spaceCustomOrder.getCustomOrder();
            List<SpacesCustomOrder> spacesCustomOrderGreaterThan =  spacesCustomOrderRepository.findBySpace_IdAndCustomOrderGreaterThan(space.getId(), order);
            spacesCustomOrderRepository.delete(spaceCustomOrder);
            
            for(SpacesCustomOrder spaceIteration : spacesCustomOrderGreaterThan) {
                spaceIteration.setCustomOrder(order);
                order++;
            }
            spacesCustomOrderRepository.saveAll(spacesCustomOrderGreaterThan);
        }
    }

}
