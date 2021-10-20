package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;
import edu.asu.diging.vspace.core.services.impl.SpaceManager;

@Component
public class ExhibitionSpaceOrderUtility {
    
    @Autowired
    ISpacesCustomOrderManager spacesCustomOrderManager;
    
    private List<ISpace> sortSpacesAlphabetically(List<ISpace> publishedSpaces){
        //TO Do sort alphabetically
        /* (non-Javadoc)
         * Published spaces to be shown alphabetically by default
         */
        Collections.sort(publishedSpaces, new Comparator<ISpace>() {
            @Override
            public int compare(ISpace space1, ISpace space2) {
                return (space1.getName().toLowerCase()).compareTo(space2.getName().toLowerCase());
            }
        });
        return publishedSpaces;
    }
    
    private List<ISpace> sortSpacesOnCreationDate(List<ISpace> publishedSpaces){
        Collections.sort(publishedSpaces, new Comparator<ISpace>() {
            @Override
            public int compare(ISpace space1, ISpace space2) {
                return (space1.getCreationDate()).compareTo(space2.getCreationDate());
            }
        });
        return publishedSpaces;
    }
    
    public List<ISpace> customSpaceOrder(){
        List<SpacesCustomOrder> spacesCustomOrder = new ArrayList<>();
        List<ISpace> spaces = new ArrayList<>();
        for(SpacesCustomOrder eachSpace: spacesCustomOrderManager.findAll()) {
            if(eachSpace.getSpace().getSpaceStatus() == SpaceStatus.PUBLISHED || eachSpace.getSpace().getSpaceStatus() == null) {
                spacesCustomOrder.add(eachSpace);
            }
        }
        Collections.sort(spacesCustomOrder, Comparator.comparing(SpacesCustomOrder::getCustomOrder));
        for(SpacesCustomOrder eachSpace: spacesCustomOrder) {
            spaces.add(eachSpace.getSpace());
        }
        return spaces;
    }
    
    public List<ISpace> retrieveSpacesListInGivenOrder(List<ISpace> publishedSpaces, ExhibitionSpaceOrderMode mode){
        if(mode == null || mode == ExhibitionSpaceOrderMode.ALPHABETICAL) {
            return sortSpacesAlphabetically(publishedSpaces);
        }
        else if(mode == ExhibitionSpaceOrderMode.CREATION_DATE) {
            return sortSpacesOnCreationDate(publishedSpaces);
        }else {
            return customSpaceOrder();
        }
    }
}
