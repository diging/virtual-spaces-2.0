package edu.asu.diging.vspace.core.model;

public interface ISpaceLink extends IVSpaceElement {

	ISpace getSourceSpace();

	void setSourceSpace(ISpace sourceSpace);

	ISpace getTargetSpace();

	void setTargetSpace(ISpace targetSpace);
}