package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;

import edu.asu.diging.vspace.core.model.ITextBlock;

@Entity
public class TextBlock extends ContentBlock implements ITextBlock {

    private String text;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

}
