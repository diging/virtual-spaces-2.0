package edu.asu.diging.vspace.core.model;

public interface IModuleLink extends IVSpaceElement {

	String getId();

	void setId(String id);

	ISpace getSpace();

	void setSpace(ISpace space);

	IModule getModule();

	void setModule(IModule module);

}