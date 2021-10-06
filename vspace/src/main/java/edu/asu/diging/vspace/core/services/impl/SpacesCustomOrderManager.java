package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.SpacesCustomOrderRepository;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

@Service
@Transactional(rollbackFor = { Exception.class })
public class SpacesCustomOrderManager implements ISpacesCustomOrderManager {

    @Autowired
    SpacesCustomOrderRepository spacesCustomOrderRepository;
    
    @Override
    public Integer findMaxContentOrder(String spaceId) {
        
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

}
