package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface IVSImage extends IVSMedia {

    void setCategories(List<ImageCategory> categories);

    List<ImageCategory> getCategories();

    void setTags(List<ITag> tags);

    List<ITag> getTags();

}