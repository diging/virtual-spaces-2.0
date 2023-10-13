package edu.asu.diging.vspace.core.model;

public interface ILinkSlide<T extends IVSpaceElement> extends IVSpaceElement {
    
    ISlide getSlide();

    void setSlide(ISlide slide);

    T getTarget();

    void setTarget(T target);
}
