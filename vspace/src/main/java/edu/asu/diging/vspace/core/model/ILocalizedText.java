package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;

public interface ILocalizedText {
    String getId();
    
    String getText();

    void setText(String text);
    
    ExhibitionLanguage getExhibitionLanguage();
    
    void setExhibitionLanguage( ExhibitionLanguage exhibitionLanguage);

}

