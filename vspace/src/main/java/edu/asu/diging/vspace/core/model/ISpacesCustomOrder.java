package edu.asu.diging.vspace.core.model;

public interface ISpacesCustomOrder extends IVSpaceElement { 
    
    String getId();

    void setId(String id);

    Integer getCustomOrder();

    void setCustomOrder(Integer customOrder);

    ISpace getSpace();

}
