package edu.asu.diging.vspace.core.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.asu.diging.vspace.core.services.CitesphereAuthToken;
import edu.asu.diging.vspace.core.services.ICitesphereManager;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

import okhttp3.*;

/**
 * Implementation of ICitesphereManager for Citesphere API operations.
 * 
 * This class handles authentication, HTTP requests, and data management
 * for interactions with the Citesphere API service.
 * 
 * @author ASU Digital Innovation Group
 * @version 1.0
 */
public class CitesphereManager implements ICitesphereManager {
    
    private static final Logger LOGGER = Logger.getLogger(CitesphereManager.class.getName());
    
    private static final String OAUTH_AUTH_TYPE = "oauth";
    private static final String BASIC_AUTH_TYPE = "basic";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String BASIC_PREFIX = "Basic ";
    private static final String API_VERSION_PATH = "/v1";
    
    private final String apiBaseUrl;
    private final CitesphereAuthToken authTokenObject;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    /**
     * Constructor for CitesphereManager.
     * 
     * @param apiBaseUrl The base URL for the Citesphere API
     * @param authTokenObject Authentication token object containing credentials
     * @throws IllegalArgumentException if authentication parameters are invalid
     */
    public CitesphereManager(String apiBaseUrl, CitesphereAuthToken authTokenObject) {
        this.apiBaseUrl = apiBaseUrl;
        this.authTokenObject = authTokenObject;
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();

        validateAuthenticationParameters();
        configureAuthenticationHeaders();
    }

    /**
     * Validates the authentication token object parameters.
     * 
     * @throws IllegalArgumentException if authentication parameters are missing or invalid
     */
    private void validateAuthenticationParameters() {
        if (authTokenObject.getAuthType() == null) {
            throw new IllegalArgumentException("Authentication type (authType) is required");
        }
        
        if (authTokenObject.getAccessToken() == null) {
            if (authTokenObject.getUsername() == null || authTokenObject.getPassword() == null) {
                throw new IllegalArgumentException(
                    "Either access token or username/password combination is required");
            }
        }
        
        if (!OAUTH_AUTH_TYPE.equals(authTokenObject.getAuthType()) && 
            !BASIC_AUTH_TYPE.equals(authTokenObject.getAuthType())) {
            throw new IllegalArgumentException(
                "Authentication type must be either '" + OAUTH_AUTH_TYPE + "' or '" + BASIC_AUTH_TYPE + "'");
        }
    }
    
    /**
     * Configures authentication headers based on the authentication type.
     */
    private void configureAuthenticationHeaders() {
        Map<String, String> headers = new HashMap<>();
        
        if (OAUTH_AUTH_TYPE.equals(authTokenObject.getAuthType())) {
            headers.put(AUTHORIZATION_HEADER, BEARER_PREFIX + authTokenObject.getAccessToken());
        } else if (BASIC_AUTH_TYPE.equals(authTokenObject.getAuthType())) {
            String credentials = authTokenObject.getUsername() + ":" + authTokenObject.getPassword();
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
            headers.put(AUTHORIZATION_HEADER, BASIC_PREFIX + encodedCredentials);
        }
        
        authTokenObject.setHeaders(headers);
    }
    
    /**
     * Executes a GET request to the specified URL.
     * 
     * @param url The target URL for the GET request
     * @return Response data as a Map, or error information if the request fails
     */
    private Map<String, Object> executeGetRequest(String url) {
        try {
            Request.Builder requestBuilder = new Request.Builder().url(url);
            
            // Add authentication headers
            addAuthenticationHeaders(requestBuilder);
            
            Request request = requestBuilder.build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                return parseJsonResponse(response);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error executing GET request to " + url, e);
            return createErrorResponse("Network error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during GET request to " + url, e);
            return createErrorResponse("Unexpected error: " + e.getMessage());
        }
    }
    
    /**
     * Adds authentication headers to the request builder.
     * 
     * @param requestBuilder The request builder to add headers to
     */
    private void addAuthenticationHeaders(Request.Builder requestBuilder) {
        if (authTokenObject.getHeaders() != null) {
            authTokenObject.getHeaders().forEach(requestBuilder::addHeader);
        }
    }
    
    /**
     * Parses JSON response from HTTP response.
     * 
     * @param response The HTTP response to parse
     * @return Parsed JSON as Map
     * @throws IOException if parsing fails
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJsonResponse(Response response) throws IOException {
        if (response.body() != null) {
            String responseBody = response.body().string();
            if (!responseBody.isEmpty()) {
                return objectMapper.readValue(responseBody, Map.class);
            }
        }
        return new HashMap<>();
    }
    
    /**
     * Creates an error response map.
     * 
     * @param errorMessage The error message to include
     * @return Map containing error information
     */
    private Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", true);
        errorResponse.put("error_message", errorMessage);
        return errorResponse;
    }
    
    /**
     * Executes a POST request with optional file upload.
     * 
     * @param url The target URL for the POST request
     * @param data Form data to include in the request
     * @param filePath Path to file for upload (optional)
     * @return HTTP Response object or error message
     */
    private Object executePostRequest(String url, Map<String, Object> data, String filePath) {
        try {
            MultipartBody.Builder formBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
            
            // Add form data parameters
            addFormDataParameters(formBuilder, data);
            
            // Add file if specified
            addFileToForm(formBuilder, filePath);
            
            RequestBody requestBody = formBuilder.build();
            Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);
            
            addAuthenticationHeaders(requestBuilder);
            Request request = requestBuilder.build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                LOGGER.info("POST request completed with response code: " + response.code());
                return response;
            }
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO error during POST request to " + url, e);
            return "IO error during file upload: " + e.getMessage();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during POST request to " + url, e);
            return "Unexpected error during request: " + e.getMessage();
        }
    }
    
    /**
     * Adds form data parameters to the multipart form builder.
     * 
     * @param formBuilder The form builder to add parameters to
     * @param data The data parameters to add
     */
    private void addFormDataParameters(MultipartBody.Builder formBuilder, Map<String, Object> data) {
        if (data != null) {
            data.forEach((key, value) -> 
                formBuilder.addFormDataPart(key, String.valueOf(value))
            );
        }
    }
    
    /**
     * Adds a file to the multipart form if the file path is provided and valid.
     * 
     * @param formBuilder The form builder to add the file to
     * @param filePath The path to the file to upload
     */
    private void addFileToForm(MultipartBody.Builder formBuilder, String filePath) {
        if (filePath != null && !filePath.trim().isEmpty()) {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/pdf"));
                formBuilder.addFormDataPart("files", file.getName(), fileBody);
            } else {
                LOGGER.warning("File not found or invalid: " + filePath);
            }
        }
    }
    
    // API endpoint methods
    
    @Override
    public Map<String, Object> getUser() {
        return executeGetRequest(apiBaseUrl + API_VERSION_PATH + "/user");
    }
    
    @Override
    public Map<String, Object> checkTest() {
        return executeGetRequest(apiBaseUrl + API_VERSION_PATH + "/test");
    }
    
    @Override
    public Map<String, Object> checkAccess(String documentId) {
        if (documentId == null || documentId.trim().isEmpty()) {
            return createErrorResponse("Document ID is required");
        }
        return executeGetRequest(apiBaseUrl + "/files/giles/" + documentId + "/access/check");
    }
    
    @Override
    public Map<String, Object> getDataByEndpoint(String endpoint) {
        if (endpoint == null || endpoint.trim().isEmpty()) {
            return createErrorResponse("Endpoint is required");
        }
        return executeGetRequest(apiBaseUrl + API_VERSION_PATH + endpoint);
    }
    
    @Override
    public Map<String, Object> getGroups() {
        return executeGetRequest(apiBaseUrl + API_VERSION_PATH + "/groups");
    }
    
    @Override
    public Map<String, Object> getGroupInfo(String groupId) {
        if (groupId == null || groupId.trim().isEmpty()) {
            return createErrorResponse("Group ID is required");
        }
        return executeGetRequest(apiBaseUrl + API_VERSION_PATH + "/groups/" + groupId);
    }
    
    @Override
    public Map<String, Object> getGroupItems(String zoteroGroupId) {
        if (zoteroGroupId == null || zoteroGroupId.trim().isEmpty()) {
            return createErrorResponse("Zotero Group ID is required");
        }
        return executeGetRequest(apiBaseUrl + API_VERSION_PATH + "/groups/" + zoteroGroupId + "/items");
    }
    
    @Override
    public Map<String, Object> getCollections(String zoteroGroupId) {
        if (zoteroGroupId == null || zoteroGroupId.trim().isEmpty()) {
            return createErrorResponse("Zotero Group ID is required");
        }
        return executeGetRequest(apiBaseUrl + API_VERSION_PATH + "/groups/" + zoteroGroupId + "/collections");
    }
    
    @Override
    public Map<String, Object> getCollectionItems(String zoteroGroupId, String collectionId, int pageNumber) {
        if (zoteroGroupId == null || zoteroGroupId.trim().isEmpty()) {
            return createErrorResponse("Zotero Group ID is required");
        }
        if (collectionId == null || collectionId.trim().isEmpty()) {
            return createErrorResponse("Collection ID is required");
        }
        
        String url = apiBaseUrl + API_VERSION_PATH + "/groups/" + zoteroGroupId + 
                    "/collections/" + collectionId + "/items";
        
        if (pageNumber > 0) {
            url += "?page=" + pageNumber;
        }
        
        return executeGetRequest(url);
    }
    
    @Override
    public Map<String, Object> getCollectionItems(String zoteroGroupId, String collectionId) {
        return getCollectionItems(zoteroGroupId, collectionId, 0);
    }
    
    @Override
    public Map<String, Object> getItemInfo(String zoteroGroupId, String itemId) {
        if (zoteroGroupId == null || zoteroGroupId.trim().isEmpty()) {
            return createErrorResponse("Zotero Group ID is required");
        }
        if (itemId == null || itemId.trim().isEmpty()) {
            return createErrorResponse("Item ID is required");
        }
        return executeGetRequest(apiBaseUrl + API_VERSION_PATH + "/groups/" + zoteroGroupId + "/items/" + itemId);
    }
    
    @Override
    public Map<String, Object> getCollectionsByCollectionId(String zoteroGroupId, String collectionId) {
        if (zoteroGroupId == null || zoteroGroupId.trim().isEmpty()) {
            return createErrorResponse("Zotero Group ID is required");
        }
        if (collectionId == null || collectionId.trim().isEmpty()) {
            return createErrorResponse("Collection ID is required");
        }
        return executeGetRequest(apiBaseUrl + "/groups/" + zoteroGroupId + "/collections/" + collectionId + "/collections");
    }
    
    @Override
    public Object addItem(String groupId, Map<String, Object> data, String filePath) {
        if (groupId == null || groupId.trim().isEmpty()) {
            return "Group ID is required";
        }
        String url = apiBaseUrl + API_VERSION_PATH + "/groups/" + groupId + "/items/create";
        return executePostRequest(url, data, filePath);
    }
}