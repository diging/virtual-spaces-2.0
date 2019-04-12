package edu.asu.diging.vspace.core.model;

public interface IImageBlock extends IContentBlock {

    void setImage(IVSImage image);

    IVSImage getImage();

    void setId(String id);

    String getId();

}
