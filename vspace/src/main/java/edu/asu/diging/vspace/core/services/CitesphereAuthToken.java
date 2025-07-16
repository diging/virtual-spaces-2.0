package edu.asu.diging.vspace.core.services;

import java.util.Map;

/**
 * Authentication token object for Citesphere API
 */
public class CitesphereAuthToken {
    
    private String authType;
    private Map<String, String> headers;
    private String accessToken;
    private String username;
    private String password;
    
    /**
     * Constructor for OAuth authentication
     * @param accessToken OAuth access token
     */
    public CitesphereAuthToken(String accessToken) {
        this.authType = "oauth";
        this.accessToken = accessToken;
    }
    
    /**
     * Constructor for Basic authentication
     * @param username Username
     * @param password Password
     */
    public CitesphereAuthToken(String username, String password) {
        this.authType = "basic";
        this.username = username;
        this.password = password;
    }
    
    // Getters and setters
    public String getAuthType() {
        return authType;
    }
    
    public void setAuthType(String authType) {
        this.authType = authType;
    }
    
    public Map<String, String> getHeaders() {
        return headers;
    }
    
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}