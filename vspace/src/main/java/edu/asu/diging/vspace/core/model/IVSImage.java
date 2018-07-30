package edu.asu.diging.vspace.core.model;

public interface IVSImage extends IVSpaceElement {

	String getId();

	void setId(String id);

	String getFilename();

	void setFilename(String filename);

	String getParentPath();

	void setParentPath(String parentPath);

	void setFileType(String fileType);

	String getFileType();

}