package edu.asu.diging.vspace.core.services;

import java.util.Map;
import edu.asu.diging.vspace.core.exception.CitesphereTokenException;

/**
 * Service interface for Citesphere API operations
 */
public interface ICitesphereManager {
    
    /**
     * Get user information
     * @return User data as Map
     */
    Map<String, Object> getUser();
    
    /**
     * Check test endpoint
     * @return Test response as Map
     */
    Map<String, Object> checkTest();
    
    /**
     * Check access for a document
     * @param documentId Document ID to check access for
     * @return Access check response as Map
     */
    Map<String, Object> checkAccess(String documentId);
    
    /**
     * Get data by endpoint
     * @param endpoint API endpoint path
     * @return Data response as Map
     */
    Map<String, Object> getDataByEndpoint(String endpoint);
    
    /**
     * Get all groups
     * @return Groups data as Map
     */
    Map<String, Object> getGroups();
    
    /**
     * Get group information
     * @param groupId Group ID
     * @return Group information as Map
     */
    Map<String, Object> getGroupInfo(String groupId);
    
    /**
     * Get group items
     * @param zoteroGroupId Zotero group ID
     * @return Group items as Map
     */
    Map<String, Object> getGroupItems(String zoteroGroupId);
    
    /**
     * Get collections
     * @param zoteroGroupId Zotero group ID
     * @return Collections as Map
     */
    Map<String, Object> getCollections(String zoteroGroupId);
    
    /**
     * Get collection items
     * @param zoteroGroupId Zotero group ID
     * @param collectionId Collection ID
     * @param pageNumber Page number (optional, defaults to 0)
     * @return Collection items as Map
     */
    Map<String, Object> getCollectionItems(String zoteroGroupId, String collectionId, int pageNumber);
    
    /**
     * Get collection items (overloaded method without page number)
     * @param zoteroGroupId Zotero group ID
     * @param collectionId Collection ID
     * @return Collection items as Map
     */
    Map<String, Object> getCollectionItems(String zoteroGroupId, String collectionId);
    
    /**
     * Get item information
     * @param zoteroGroupId Zotero group ID
     * @param itemId Item ID
     * @return Item information as Map
     */
    Map<String, Object> getItemInfo(String zoteroGroupId, String itemId);
    
    /**
     * Get collections by collection ID
     * @param zoteroGroupId Zotero group ID
     * @param collectionId Collection ID
     * @return Collections as Map
     */
    Map<String, Object> getCollectionsByCollectionId(String zoteroGroupId, String collectionId);
    
    /**
     * Add item to group
     * @param groupId Group ID
     * @param data Item data
     * @param filePath Path to file to upload
     * @return Response from API
     */
    Object addItem(String groupId, Map<String, Object> data, String filePath);
    
    /**
     * Refresh the access token using refresh token
     * @return New CitesphereAuthToken with refreshed access token
     * @throws CitesphereTokenException if refresh fails
     */
    CitesphereAuthToken refreshToken() throws CitesphereTokenException;
    
    /**
     * Validate if current token is still valid
     * @return true if token is valid, false otherwise
     */
    boolean isTokenValid();
}
