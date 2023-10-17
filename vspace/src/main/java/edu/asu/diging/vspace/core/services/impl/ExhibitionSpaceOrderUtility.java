package edu.asu.diging.vspace.core.services.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.model.ExhibitionSpaceOrderMode;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.SpacesCustomOrder;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IExhibitionSpaceOrderUtility;

/**
 * Utility class to order the spaces in the Exhibition using a user defined custom order.
 * @author prachikharge
 *
 */
@Component
public class ExhibitionSpaceOrderUtility implements IExhibitionSpaceOrderUtility {
    
    @Autowired
    private  IExhibitionManager exhibitionManager;
    
    /**
     * used to sort spaces alphabetically
     */
    private List<ISpace> sortSpacesAlphabetically(List<ISpace> publishedSpaces){
        Collections.sort(publishedSpaces, new Comparator<ISpace>() {
            @Override
            public int compare(ISpace space1, ISpace space2) {
                return (space1.getName().toLowerCase()).compareTo(space2.getName().toLowerCase());
            }
        });
        return publishedSpaces;
    }
    
    /**
     * used to sort spaces based on creation date
     */
    private List<ISpace> sortSpacesOnCreationDate(List<ISpace> publishedSpaces){
        Collections.sort(publishedSpaces, new Comparator<ISpace>() {
            @Override
            public int compare(ISpace space1, ISpace space2) {
                return (space1.getCreationDate()).compareTo(space2.getCreationDate());
            }
        });
        return publishedSpaces;
    }
    
    /**
     * used to sort spaces based on user defined order
     */
    private List<ISpace>  sortSpacesByCustomOrder(List<ISpace> publishedSpaces){
        IExhibition exhibition  = exhibitionManager.getStartExhibition();
        SpacesCustomOrder spacesCustomOrder = exhibition.getSpacesCustomOrder();
        if(spacesCustomOrder == null) {
            return sortSpacesAlphabetically(publishedSpaces);
        }
        List<ISpace> allSpaces = spacesCustomOrder.getCustomOrderedSpaces();
        
        //only show published spaces        
        return allSpaces.stream()
                .filter(space -> space.getSpaceStatus() == SpaceStatus.PUBLISHED)
                .collect(Collectors.toList());
    }
    
    /**
     * used to sort spaces based on the selected mode
     */
    @Override
    public List<ISpace> sortSpaces(List<ISpace> publishedSpaces, ExhibitionSpaceOrderMode mode){
        if(mode == null || mode == ExhibitionSpaceOrderMode.ALPHABETICAL) {
            return sortSpacesAlphabetically(publishedSpaces);
        } else if(mode == ExhibitionSpaceOrderMode.CREATION_DATE) {
            return sortSpacesOnCreationDate(publishedSpaces);
        } else {
            return sortSpacesByCustomOrder(publishedSpaces);
        }
    }
}
