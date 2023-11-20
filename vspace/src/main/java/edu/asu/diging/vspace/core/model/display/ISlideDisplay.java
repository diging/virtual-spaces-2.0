package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.ISlide;

public interface ISlideDisplay {
    
    String getId();

    void setId(String id);

    ISlide getSlide();

    void setSlide(ISlide slide);

    int getWidth();

    void setWidth(int width);

    int getHeight();

    void setHeight(int height);

}
