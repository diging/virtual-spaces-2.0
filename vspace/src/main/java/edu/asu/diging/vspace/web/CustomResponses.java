package edu.asu.diging.vspace.web;

public enum CustomResponses {
    
    INVALID_INPUT("Input is invalid. Please use appropriate type"),
    NULL_INPUT("Input provided is null"),
    NOT_FOUND("Entry was not found for given input"),
    SUCCESS("Success");
    
    private String responseMessage;
    
    private CustomResponses(String message) {
        this.responseMessage = message;
    }
    
    public String getResponseMessage() {
        return responseMessage;
    }
    
}
