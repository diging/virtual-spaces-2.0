package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.web.staff.ExhibitionSpaceOrderMode;

public interface IExhibition extends IVSpaceElement {

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getSpace()
     */
    ISpace getStartSpace();

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ISpacee#setSpace(edu.asu.diging.vspace.
     * core.model.impl.Space)
     */
    void setStartSpace(ISpace space);

    void setTitle(String title);

    String getTitle();
    
    ExhibitionSpaceOrderMode getSpaceOrderMode();
    
    void setSpaceOrderMode(ExhibitionSpaceOrderMode exhibitionSpaceOrderMode);

    SpacesCustomOrder getSpacesCustomOrder();

    void setSpacesCustomOrder(SpacesCustomOrder spacesCustomOrder);

}
