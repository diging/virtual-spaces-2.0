package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.ManyToOne;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;

public class SpaceBlock extends ContentBlock implements ISpaceBlock {
    private String title;
    
    @ManyToOne(targetEntity = Space.class)
    private ISpace space;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public ISpace getSpace() {
        return space;
    }

    @Override
    public void setSpace(ISpace space) {
        this.space = space;
    }
}
