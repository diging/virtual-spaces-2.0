package edu.asu.diging.vspace.core.services.impl;


//import com.citesphere.api.CitesphereService;
//import com.citesphere.api.auth.CitesphereAuthToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.vspace.core.services.CitesphereAuthToken;
import edu.asu.diging.vspace.core.services.ICitesphereManager;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
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
     */
    private Map<String, Object> executeCommand(String url) {
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
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    return objectMapper.readValue(responseBody, Map.class);
                }
            }
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
    public Map<String, Object> getGroupCollectionsMapping() {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> groups = (List<Map<String, Object>>) getGroups();

        for (Map<String, Object> group : groups) {
            String groupId = String.valueOf(group.get("id"));
            String groupName = (String) group.get("name");

            // Get collections for this group
            Map<String, Object> collectionData = getCollections(groupId);

            List<Map<String, Object>> collections = (List<Map<String, Object>>) collectionData.get("collections");
            List<String> collectionKeys = new ArrayList<>();

            for (Map<String, Object> collection : collections) {
                collectionKeys.add((String) collection.get("key"));
            }

            // Build group summary
            Map<String, Object> groupSummary = new HashMap<>();
            groupSummary.put("group_name", groupName);
            groupSummary.put("collections", collectionKeys);

            result.put(groupId, groupSummary);
        }

        return result;
    }

    
    @Override
    public Map<String, Object> getUser() {
        String url = api + "/v1/user";
        return executeCommand(url);
    }
    
    @Override
    public Map<String, Object> checkTest() {
        String url = api + "/v1/test";
        return executeCommand(url);
    }
    
    @Override
    public Map<String, Object> checkAccess(String documentId) {
        String url = api + "/files/giles/" + documentId + "/access/check";
        return executeCommand(url);
    }
    
    @Override
    public Map<String, Object> getDataByEndpoint(String endpoint) {
        String url = api + "/v1" + endpoint;
        return executeCommand(url);
    }
    
    @Override
    public Map<String, Object> getGroups() {
        String url = api + "/v1/groups";
        return executeCommand(url);
    }
    
    @Override
    public Map<String, Object> getGroupInfo(String groupId) {
        String url = api + "/v1/groups/" + groupId;
        return executeCommand(url);
    }
    
    @Override
    public Map<String, Object> getGroupItems(String zoteroGroupId) {
        String url = api + "/v1/groups/" + zoteroGroupId + "/items";
        return executeCommand(url);
    }
    
    @Override
    public Map<String, Object> getCollections(String zoteroGroupId) {
        String url = api + "/v1/groups/" + zoteroGroupId + "/collections";
        return executeCommand(url);
    }
    
    @Override
    public Map<String, Object> getCollectionItems(String zoteroGroupId, String collectionId, int pageNumber) {
        String url = api + "/v1/groups/" + zoteroGroupId + "/collections/" + collectionId + "/items";
        if (pageNumber > 0) {
            url += "?&page=" + pageNumber;
        }
        return executeCommand(url);
    }
    
    @Override
    public Map<String, Object> getCollectionItems(String zoteroGroupId, String collectionId) {
        return getCollectionItems(zoteroGroupId, collectionId, 0);
    }
    
    @Override
    public Map<String, Object> getItemInfo(String zoteroGroupId, String itemId) {
        String url = api + "/v1/groups/" + zoteroGroupId + "/items/" + itemId;
        return executeCommand(url);
    }
    
    @Override
    public Map<String, Object> getCollectionsByCollectionId(String zoteroGroupId, String collectionId) {
        String url = api + "/groups/" + zoteroGroupId + "/collections/" + collectionId + "/collections";
        return executeCommand(url);
    }
    
    @Override
    public Object addItem(String groupId, Map<String, Object> data, String filePath) {
        String url = api + "/v1/groups/" + groupId + "/items/create";
        return executePostRequest(url, data, filePath);
    }
}
