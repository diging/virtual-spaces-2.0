package edu.asu.diging.vspace.core.model;

public interface ILink<T extends IVSpaceElement> extends IVSpaceElement{

    ISpace getSpace();

    void setSpace(ISpace space);

    T getTarget();

    void setTarget(T target);
}
