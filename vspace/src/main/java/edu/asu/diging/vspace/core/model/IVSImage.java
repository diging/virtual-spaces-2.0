package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface IVSImage extends IVSMedia {
<<<<<<< HEAD

	String getFilename();

	void setFilename(String filename);

	String getParentPath();

	void setParentPath(String parentPath);

	void setFileType(String fileType);

	String getFileType();

	void setWidth(int width);

	int getWidth();

	void setHeight(int height);

	int getHeight();
=======
>>>>>>> develop

    void setCategories(List<ImageCategory> categories);

    List<ImageCategory> getCategories();

    void setTags(List<ITag> tags);

    List<ITag> getTags();

}