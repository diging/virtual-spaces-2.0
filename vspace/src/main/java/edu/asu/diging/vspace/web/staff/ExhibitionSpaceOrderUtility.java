package edu.asu.diging.vspace.web.staff;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.impl.ExhibitionManager;

@Component
public class ExhibitionSpaceOrderUtility {
    
    @Autowired
    ExhibitionRepository exhibitRepo;
    
    @Autowired
    IExhibitionManager exhibitionManager;
    
    private List<ISpace> sortSpacesAlphabetically(List<ISpace> publishedSpaces){
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
    
    public List<ISpace> getCustomSpaceOrder(){
        IExhibition exhibition  = exhibitionManager.getStartExhibition();
        SpacesCustomOrder spacesCustomOrder = exhibition.getSpacesCustomOrder();
        return spacesCustomOrder.getCustomOrderedSpaces();
    }
    
    public List<ISpace> retrieveSpacesListInGivenOrder(List<ISpace> publishedSpaces, ExhibitionSpaceOrderMode mode){
        if(mode == null || mode == ExhibitionSpaceOrderMode.ALPHABETICAL) {
            return sortSpacesAlphabetically(publishedSpaces);
        }
        else if(mode == ExhibitionSpaceOrderMode.CREATION_DATE) {
            return sortSpacesOnCreationDate(publishedSpaces);
        }else {
            return getCustomSpaceOrder();
        }
    }
}
