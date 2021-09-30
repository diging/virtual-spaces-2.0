package edu.asu.diging.vspace.web.staff;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.model.ISpace;

@Component
public class ExhibitionSpaceOrderUtility {

    public List<ISpace> sortSpacesAlphabetically(List<ISpace> publishedSpaces){
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
    
    public List<ISpace> sortSpacesOnCreationDate(List<ISpace> publishedSpaces){
        Collections.sort(publishedSpaces, new Comparator<ISpace>() {
            @Override
            public int compare(ISpace space1, ISpace space2) {
                return (space1.getCreationDate()).compareTo(space2.getCreationDate());
            }
        });
        return publishedSpaces;
    }
}
