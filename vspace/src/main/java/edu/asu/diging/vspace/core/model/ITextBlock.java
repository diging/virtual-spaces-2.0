package edu.asu.diging.vspace.core.model;

public interface ITextBlock extends IContentBlock {

    void setText(String text);

    String getText();

    void setId(String id);

    String getId();
    
    String htmlRenderedText();

}
