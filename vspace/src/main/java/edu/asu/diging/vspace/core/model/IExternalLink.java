package edu.asu.diging.vspace.core.model;

public interface IExternalLink extends IVSpaceElement {
	
	ISpace getSpace();

	void setSpace(ISpace space);
	
	IExternal getExternal();
	
	void setExternal(IExternal external) ;

}
