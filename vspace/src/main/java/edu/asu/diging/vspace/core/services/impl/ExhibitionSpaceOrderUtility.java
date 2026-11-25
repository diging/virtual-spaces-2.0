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
 * Utility class for Exhibition Spaces custom order
 * @author prachikharge
 *
 */
@Component
public class ExhibitionSpaceOrderUtility implements IExhibitionSpaceOrderUtility {
    
    @Autowired
    private  IExhibitionManager exhibitionManager;
    
    /**
     * Sorts the spaces in alphabetical order by name (case-insensitive).
     * This method sorts the spaces in place, so the passed-in list of spaces
     * will be sorted after the method is done.
     * 
     * @param publishedSpaces the list of spaces to be sorted alphabetically
     * @return the same list of spaces, now sorted alphabetically by name
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
     * Sorts the spaces based on their creation date in ascending order.
     * This method sorts the spaces in place, so the passed-in list of spaces
     * will be sorted after the method is done.
     * 
     * @param publishedSpaces the list of spaces to be sorted by creation date
     * @return the same list of spaces, now sorted by creation date
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
     * Sorts the spaces based on user-defined custom order.
     * If no custom order is defined (spacesCustomOrder is null), the method
     * falls back to alphabetical sorting and the passed-in list will be modified.
     * Otherwise, a new filtered list is returned containing only published spaces
     * from the custom order, and the original passed-in list remains unchanged.
     * 
     * @param publishedSpaces the list of spaces to be sorted (may or may not be modified)
     * @return either the same list sorted alphabetically (if no custom order exists)
     *         or a new filtered list based on custom order containing only published spaces
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
     * Sorts the spaces based on the specified ordering mode.
     * 
     * @param publishedSpaces the list of spaces to be sorted
     * @param mode the ordering mode to apply (ALPHABETICAL, CREATION_DATE, or CUSTOM).
     *             If null, defaults to ALPHABETICAL ordering
     * @return the sorted list of spaces according to the specified mode.
     *         Note: For ALPHABETICAL and CREATION_DATE modes, the original list is modified in place.
     *         For CUSTOM mode, behavior depends on whether custom order exists - see sortSpacesByCustomOrder()
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
