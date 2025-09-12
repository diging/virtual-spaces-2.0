package edu.asu.diging.vspace.core.services;

import java.util.Map;

/**
 * Authentication token object for Citesphere API
 */
public class CitesphereAuthToken {
    
    private String authType;
    private Map<String, String> headers;
    private String accessToken;
    private String refreshToken;
    private String username;
    private String password;
    private long tokenExpiryTime;
    
    /**
     * Constructor for OAuth authentication
     * @param accessToken OAuth access token
     */
    public CitesphereAuthToken(String accessToken) {
        this.authType = "oauth";
        this.accessToken = accessToken;
    }
    
    /**
     * Constructor for OAuth authentication with refresh token
     * @param accessToken OAuth access token
     * @param refreshToken OAuth refresh token
     * @param expiryTime Token expiry time in milliseconds
     */
    public CitesphereAuthToken(String accessToken, String refreshToken, long expiryTime) {
        this.authType = "oauth";
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenExpiryTime = expiryTime;
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
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public long getTokenExpiryTime() {
        return tokenExpiryTime;
    }
    
    public void setTokenExpiryTime(long tokenExpiryTime) {
        this.tokenExpiryTime = tokenExpiryTime;
    }
    
    /**
     * Check if the access token is expired
     * @return true if token is expired, false otherwise
     */
    public boolean isTokenExpired() {
        return tokenExpiryTime > 0 && System.currentTimeMillis() > tokenExpiryTime;
    }
}