package edu.asu.diging.vspace.core.model;

public interface IChoice extends IVSpaceElement {

    ISequence getSequence();

    void setSequence(ISequence link);
    
    IBranchingPoint getBranchingPoint();
    
    void setBranchingPoint(IBranchingPoint bpoint);
}
