package edu.asu.diging.vspace.core.model;

public interface ISpaceLink extends ILLink<ISpace> {

	ISpace getSourceSpace();

	void setSourceSpace(ISpace sourceSpace);

	ISpace getTargetSpace();

	void setTargetSpace(ISpace targetSpace);
}