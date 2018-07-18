package edu.asu.diging.vspace.core.model;

public interface ISpaceLink extends IVSpaceElement {

	String getId();

	void setId(String id);

	ISpace getSourceSpace();

	void setSourceSpace(ISpace sourceSpace);

	ISpace getTargetSpace();

	void setTargetSpace(ISpace targetSpace);

}