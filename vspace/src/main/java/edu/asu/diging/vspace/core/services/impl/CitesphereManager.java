package edu.asu.diging.vspace.core.services.impl;


//import com.citesphere.api.CitesphereService;
//import com.citesphere.api.auth.CitesphereAuthToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import edu.asu.diging.vspace.core.services.CitesphereAuthToken;
import edu.asu.diging.vspace.core.services.ICitesphereManager;
import edu.asu.diging.vspace.core.exception.CitesphereTokenException;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.*;

/**
 * Implementation of CitesphereService for API operations
 */
public class CitesphereManager implements ICitesphereManager {
    
    private final String api;
    private final CitesphereAuthToken authTokenObject;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    
    /**
     * Constructor
     * @param api API base URL
     * @param authTokenObject Authentication token object
     * @return 
     */
    public CitesphereManager(String api, CitesphereAuthToken authTokenObject) {
        this.api = api;
        this.authTokenObject = authTokenObject;
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();

        validate();
        handleApiParams();
    }

    
    /**
     * Validate authentication token object
     */
    private void validate() {
        if (authTokenObject.getAuthType() == null) {
            throw new IllegalArgumentException("Missing authType attribute");
        }
        
        if (authTokenObject.getAccessToken() == null) {
            if (authTokenObject.getUsername() == null || authTokenObject.getPassword() == null) {
                throw new IllegalArgumentException(
                    "Either username and password or access_token should be present");
            }
        }
        
        if (!"oauth".equals(authTokenObject.getAuthType()) && 
            !"basic".equals(authTokenObject.getAuthType())) {
            throw new IllegalArgumentException("authType should be either oauth or basic");
        }
    }
    
    /**
     * Handle API parameters and set headers
     */
    private void handleApiParams() {
        Map<String, String> headers = new HashMap<>();
        
        if ("oauth".equals(authTokenObject.getAuthType())) {
            headers.put("Authorization", "Bearer " + authTokenObject.getAccessToken());
        } else if ("basic".equals(authTokenObject.getAuthType())) {
            String authStr = authTokenObject.getUsername() + ":" + authTokenObject.getPassword();
            String authB64 = Base64.getEncoder().encodeToString(authStr.getBytes());
            headers.put("Authorization", "Basic " + authB64);
        }
        
        authTokenObject.setHeaders(headers);
    }
    
    /**
     * Execute GET command
     * @param url Request URL
     * @return Response data as Map
     * @throws CitesphereTokenException if token is invalid or expired
     */
    private Map<String, Object> executeCommand(String url) throws CitesphereTokenException {
        try {
            Request.Builder requestBuilder = new Request.Builder().url(url);
            
            // Add headers
            if (authTokenObject.getHeaders() != null) {
                for (Map.Entry<String, String> header : authTokenObject.getHeaders().entrySet()) {
                    requestBuilder.addHeader(header.getKey(), header.getValue());
                }
            }
            
            Request request = requestBuilder.build();
            
            try (Response response = client.newCall(request).execute()) {
                // Check for unauthorized or forbidden responses
                if (response.code() == 401 || response.code() == 403) {
                    throw new CitesphereTokenException(
                        "Invalid or expired access token", 
                        response.code(), 
                        true
                    );
                }
                
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    
                    // Check if response is an array or object
                    if (responseBody.trim().startsWith("[")) {
                        // Response is an array, wrap it in a data object
                        List<Object> responseList = objectMapper.readValue(responseBody, new TypeReference<List<Object>>(){});
                        Map<String, Object> responseMap = new HashMap<>();
                        responseMap.put("data", responseList);
                        return responseMap;
                    } else {
                        // Response is an object
                        @SuppressWarnings("unchecked")
                        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                        return responseMap;
                    }
                }
            }
        } catch (CitesphereTokenException e) {
            throw e; // Re-throw token exceptions
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            return errorMap;
        }
        
        return new HashMap<>();
    }
    
    /**
     * Execute POST request
     * @param url Request URL
     * @param data Request data
     * @param filePath File path for upload
     * @return Response object
     */
    private Object executePostRequest(String url, Map<String, Object> data, String filePath) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
            
            // Add data parameters
            if (data != null) {
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    builder.addFormDataPart(entry.getKey(), entry.getValue().toString());
                }
            }
            
            // Add file if provided
            if (filePath != null) {
                File file = new File(filePath);
                if (file.exists()) {
                    RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/pdf"));
                    builder.addFormDataPart("files", file.getName(), fileBody);
                }
            }
            
            RequestBody requestBody = builder.build();
            
            Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);
            
            // Add headers
            if (authTokenObject.getHeaders() != null) {
                for (Map.Entry<String, String> header : authTokenObject.getHeaders().entrySet()) {
                    requestBuilder.addHeader(header.getKey(), header.getValue());
                }
            }
            
            Request request = requestBuilder.build();
            
            try (Response response = client.newCall(request).execute()) {
                System.out.println("Response code: " + response.code());
                return response;
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] -------- Error during API request with " + filePath + ": " + e.getMessage());
            return "Error loading/reading file";
        }
    }
    
    @Override
    public Map<String, Object> getUser() {
        try {
            String url = api + "/v1/user";
            return executeCommand(url);
        } catch (CitesphereTokenException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            errorMap.put("token_expired", e.isTokenExpired());
            return errorMap;
        }
    }
    
    @Override
    public Map<String, Object> checkTest() {
        try {
            String url = api + "/v1/test";
            return executeCommand(url);
        } catch (CitesphereTokenException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            errorMap.put("token_expired", e.isTokenExpired());
            return errorMap;
        }
    }
    
    @Override
    public Map<String, Object> checkAccess(String documentId) {
        try {
            String url = api + "/files/giles/" + documentId + "/access/check";
            return executeCommand(url);
        } catch (CitesphereTokenException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            errorMap.put("token_expired", e.isTokenExpired());
            return errorMap;
        }
    }
    
    @Override
    public Map<String, Object> getDataByEndpoint(String endpoint) {
        try {
            String url = api + "/v1" + endpoint;
            return executeCommand(url);
        } catch (CitesphereTokenException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            errorMap.put("token_expired", e.isTokenExpired());
            return errorMap;
        }
    }
    
    @Override
    public Map<String, Object> getGroups() {
        try {
            String url = api + "/v1/groups";
            return executeCommand(url);
        } catch (CitesphereTokenException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            errorMap.put("token_expired", e.isTokenExpired());
            return errorMap;
        }
    }
    
    @Override
    public Map<String, Object> getGroupInfo(String groupId) {
        try {
            String url = api + "/v1/groups/" + groupId;
            return executeCommand(url);
        } catch (CitesphereTokenException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            errorMap.put("token_expired", e.isTokenExpired());
            return errorMap;
        }
    }
    
    @Override
    public Map<String, Object> getGroupItems(String zoteroGroupId) {
        try {
            String url = api + "/v1/groups/" + zoteroGroupId + "/items";
            return executeCommand(url);
        } catch (CitesphereTokenException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            errorMap.put("token_expired", e.isTokenExpired());
            return errorMap;
        }
    }
    
    @Override
    public Map<String, Object> getCollections(String zoteroGroupId) {
        try {
            String url = api + "/v1/groups/" + zoteroGroupId + "/collections";
            return executeCommand(url);
        } catch (CitesphereTokenException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            errorMap.put("token_expired", e.isTokenExpired());
            return errorMap;
        }
    }
    
    @Override
    public Map<String, Object> getCollectionItems(String zoteroGroupId, String collectionId, int pageNumber) {
        try {
            String url = api + "/v1/groups/" + zoteroGroupId + "/collections/" + collectionId + "/items";
            if (pageNumber > 0) {
                url += "?&page=" + pageNumber;
            }
            return executeCommand(url);
        } catch (CitesphereTokenException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            errorMap.put("token_expired", e.isTokenExpired());
            return errorMap;
        }
    }
    
    @Override
    public Map<String, Object> getCollectionItems(String zoteroGroupId, String collectionId) {
        return getCollectionItems(zoteroGroupId, collectionId, 0);
    }
    
    @Override
    public Map<String, Object> getItemInfo(String zoteroGroupId, String itemId) {
        try {
            String url = api + "/v1/groups/" + zoteroGroupId + "/items/" + itemId;
            return executeCommand(url);
        } catch (CitesphereTokenException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            errorMap.put("token_expired", e.isTokenExpired());
            return errorMap;
        }
    }
    
    @Override
    public Map<String, Object> getCollectionsByCollectionId(String zoteroGroupId, String collectionId) {
        try {
            String url = api + "/groups/" + zoteroGroupId + "/collections/" + collectionId + "/collections";
            return executeCommand(url);
        } catch (CitesphereTokenException e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error_message", e.getMessage());
            errorMap.put("token_expired", e.isTokenExpired());
            return errorMap;
        }
    }
    
    @Override
    public Object addItem(String groupId, Map<String, Object> data, String filePath) {
        String url = api + "/v1/groups/" + groupId + "/items/create";
        return executePostRequest(url, data, filePath);
    }
    
    @Override
    public CitesphereAuthToken refreshToken() throws CitesphereTokenException {
        if (authTokenObject.getRefreshToken() == null || authTokenObject.getRefreshToken().isEmpty()) {
            throw new CitesphereTokenException("No refresh token available");
        }
        
        try {
            String tokenUrl = api + "/oauth/token";
            
            RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", authTokenObject.getRefreshToken())
                .build();

            Request request = new Request.Builder()
                .url(tokenUrl)
                .post(formBody)
                .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 401 || response.code() == 403) {
                    throw new CitesphereTokenException(
                        "Refresh token is invalid or expired", 
                        response.code(), 
                        true
                    );
                }
                
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> tokenResponse = objectMapper.readValue(responseBody, Map.class);
                    
                    String newAccessToken = (String) tokenResponse.get("access_token");
                    String newRefreshToken = (String) tokenResponse.get("refresh_token");
                    Number expiresIn = (Number) tokenResponse.get("expires_in");
                    
                    if (newAccessToken != null) {
                        long expiryTime = 0;
                        if (expiresIn != null) {
                            expiryTime = System.currentTimeMillis() + (expiresIn.longValue() * 1000);
                        }
                        
                        // Update current token object
                        authTokenObject.setAccessToken(newAccessToken);
                        if (newRefreshToken != null) {
                            authTokenObject.setRefreshToken(newRefreshToken);
                        }
                        authTokenObject.setTokenExpiryTime(expiryTime);
                        
                        // Re-initialize headers with new token
                        handleApiParams();
                        
                        return authTokenObject;
                    }
                }
                
                throw new CitesphereTokenException("Failed to refresh token: " + response.code());
            }
        } catch (CitesphereTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new CitesphereTokenException("Error refreshing token: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isTokenValid() {
        if (authTokenObject.getAccessToken() == null || authTokenObject.getAccessToken().isEmpty()) {
            return false;
        }
        
        // Check if token is expired based on stored expiry time
        if (authTokenObject.isTokenExpired()) {
            return false;
        }
        
        // Optionally, test token with a simple API call
        try {
            String url = api + "/v1/test";
            Request.Builder requestBuilder = new Request.Builder().url(url);
            
            if (authTokenObject.getHeaders() != null) {
                for (Map.Entry<String, String> header : authTokenObject.getHeaders().entrySet()) {
                    requestBuilder.addHeader(header.getKey(), header.getValue());
                }
            }
            
            Request request = requestBuilder.build();
            
            try (Response response = client.newCall(request).execute()) {
                return response.code() != 401 && response.code() != 403;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
