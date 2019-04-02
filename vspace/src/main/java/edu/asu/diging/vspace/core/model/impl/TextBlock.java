package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;

import edu.asu.diging.vspace.core.model.ITextBlock;

@Entity
public class TextBlock extends ContentBlock implements ITextBlock {

    private String text;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ITextBlock#getText()
     */
    @Override
    public String getText() {
        return text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ITextBlock#setText(java.lang.String)
     */
    @Override
    public void setText(String text) {
        this.text = text;
    }

}
