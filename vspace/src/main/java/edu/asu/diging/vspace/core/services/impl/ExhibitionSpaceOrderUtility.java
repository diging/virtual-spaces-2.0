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
     * This method sorts the list of spaces in alphabetical order in-place. 
     * Note: The original list passed is modified and returned, since sorting is in-place
     * 
     * @param list of {@link ISpace} objects to be sorted
     * @return list of {@link ISpace}
     */
    private List<ISpace> sortSpacesAlphabetically(List<ISpace> spaces){
        Collections.sort(spaces, new Comparator<ISpace>() {
            @Override
            public int compare(ISpace space1, ISpace space2) {
                return (space1.getName().toLowerCase()).compareTo(space2.getName().toLowerCase());
            }
        });
        return spaces;
    }

    /**
     * This method sorts the spaces based on the space creation date in-place.
     * Note: The original list passed is modified and returned, since sorting is in-place
     * 
     * @param spaces list of {@link ISpace} objects to be sorted
     * @return list of {@link ISpace}
     */
    private List<ISpace> sortSpacesOnCreationDate(List<ISpace> spaces){
        Collections.sort(spaces, new Comparator<ISpace>() {
            @Override
            public int compare(ISpace space1, ISpace space2) {
                return (space1.getCreationDate()).compareTo(space2.getCreationDate());
            }
        });
        return spaces;
    }
    

    /**
     * This method sorts the spaces based on user defined order.
     * @param spaces
     * @return list of {@link ISpace}
     */
    private List<ISpace>  sortSpacesByCustomOrder(List<ISpace> spaces){
        IExhibition exhibition  = exhibitionManager.getStartExhibition();
        SpacesCustomOrder spacesCustomOrder = exhibition.getSpacesCustomOrder();
        if(spacesCustomOrder == null) {
            return sortSpacesAlphabetically(spaces);
        }
        return spacesCustomOrder.getCustomOrderedSpaces();
    }
    

    /**
     * This method used to sort spaces based on given mode.
     * @param spaces - list of spaces
     * @param mode - the sorting mode of the exhibition spaces
     * @return list of {@link ISpace}
     */
    @Override
    public List<ISpace> sortSpaces(List<ISpace> spaces, ExhibitionSpaceOrderMode mode){
        if(mode == null || mode == ExhibitionSpaceOrderMode.ALPHABETICAL) {
            return sortSpacesAlphabetically(spaces);
        }
        if(mode == ExhibitionSpaceOrderMode.CREATION_DATE) {
            return sortSpacesOnCreationDate(spaces);
        }
        return sortSpacesByCustomOrder(spaces);
    }
}
