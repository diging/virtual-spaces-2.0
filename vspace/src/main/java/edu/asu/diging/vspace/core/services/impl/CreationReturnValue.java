package edu.asu.diging.vspace.core.services.impl;

import java.util.List;

import edu.asu.diging.vspace.core.model.IVSpaceElement;

public class CreationReturnValue {

	private IVSpaceElement element;
	private List<String> errorMsgs;
	
	public IVSpaceElement getElement() {
		return element;
	}
	public void setElement(IVSpaceElement element) {
		this.element = element;
	}
	public List<String> getErrorMsgs() {
		return errorMsgs;
	}
	public void setErrorMsgs(List<String> errorMsgs) {
		this.errorMsgs = errorMsgs;
	}
	
}
