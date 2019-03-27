package edu.asu.diging.vspace.core.model;

public interface IContentBlock extends IVSpaceElement {
    
    String getId();
    
    void setId(String id);

    void setSlide(ISlide slide);

    ISlide getSlide();

    String getBlockInOrder();

    void setBlockInOrder(String blockInOrder);

//    ContentBlockType getType();
//
//    void setType(ContentBlockType type);
    

}
