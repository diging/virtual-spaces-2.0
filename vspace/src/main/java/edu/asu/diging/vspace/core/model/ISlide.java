package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface ISlide extends IVSpaceElement {

    void setImage(List<IVSImage> image);

    List<IVSImage> getImage();
}
