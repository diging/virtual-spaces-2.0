package edu.asu.diging.vspace.core.model;

public enum SortWithField {
	CREATION_DATE("creationDate");
	
	private final String value;
	
	private SortWithField(String value) {
		this.value = value;
	}
	
	@Override public String toString() {
	    return value; 
	  }
}
