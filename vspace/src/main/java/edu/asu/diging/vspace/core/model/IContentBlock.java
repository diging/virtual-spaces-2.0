package edu.asu.diging.vspace.core.model;

public interface IContentBlock extends IVSpaceElement {
    
    String getId();
    
    void setId(String id);

    void setSlide(ISlide slide);

    ISlide getSlide();

    Integer getBlockInOrder();

    void setBlockInOrder(Integer blockInOrder);

//    ContentBlockType getType();
//
//    void setType(ContentBlockType type);
    

}
