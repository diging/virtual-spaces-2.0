package edu.asu.diging.vspace.core.model;

public interface IChoice extends IVSpaceElement{
	
	ISequence getLink();
	
	void setLink(ISequence link);
}
