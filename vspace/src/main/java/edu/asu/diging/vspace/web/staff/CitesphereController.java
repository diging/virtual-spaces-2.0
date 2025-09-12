package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.services.CitesphereAuthToken;
import edu.asu.diging.vspace.core.services.ICitesphereManager;
import edu.asu.diging.vspace.core.services.IReferenceManager;
import edu.asu.diging.vspace.core.services.impl.CitesphereManager;
import edu.asu.diging.vspace.core.exception.CitesphereTokenException;

@Controller
public class CitesphereController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${citesphere.api.url:}")
    private String citesphereApiUrl;

    @Value("${citesphere.client.id:}")
    private String citesphereClientId;

    @Value("${citesphere.client.secret:}")
    private String citesphereClientSecret;

    @Value("${app.base.url:http://localhost:8080}")
    private String appBaseUrl;

    @Autowired
    private IReferenceManager referenceManager;


    /**
     * Initiate OAuth authorization with Citesphere
     */
    @RequestMapping(value = "/staff/citesphere/oauth/authorize", method = RequestMethod.GET)
    public String initiateOAuth(HttpSession session, RedirectAttributes redirectAttributes) {
        logger.info("[DEBUG] Initiating OAuth authorization with Citesphere");
        logger.info("[DEBUG] Citesphere API URL: {}", citesphereApiUrl);
        logger.info("[DEBUG] Client ID configured: {}", (citesphereClientId != null && !citesphereClientId.isEmpty()));
        logger.info("[DEBUG] Client Secret configured: {}", (citesphereClientSecret != null && !citesphereClientSecret.isEmpty()));
        
        if (citesphereClientId == null || citesphereClientId.isEmpty()) {
            logger.error("[DEBUG] OAuth not configured - missing client ID");
            redirectAttributes.addFlashAttribute("error", "Citesphere OAuth is not configured. Please contact your administrator.");
            return "redirect:/staff/dashboard";
        }

        try {
            // Generate state parameter for security
            String state = java.util.UUID.randomUUID().toString();
            session.setAttribute("citesphere_oauth_state", state);

            // Build authorization URL  
            String baseUrl = citesphereApiUrl;
            String redirectUri = getCurrentBaseUrl() + "/staff/citesphere/oauth/callback";
            
            logger.info("[DEBUG] Building OAuth URL - Base URL: {}", baseUrl);
            logger.info("[DEBUG] Redirect URI: {}", redirectUri);
            logger.info("[DEBUG] OAuth State: {}", state);
            
            String authUrl = baseUrl + "/oauth/authorize" +
                    "?response_type=code" +
                    "&client_id=" + java.net.URLEncoder.encode(citesphereClientId, "UTF-8") +
                    "&state=" + java.net.URLEncoder.encode(state, "UTF-8") +
                    "&redirect_uri=" + java.net.URLEncoder.encode(redirectUri, "UTF-8");

            logger.info("[DEBUG] Final OAuth URL: {}", authUrl);
            return "redirect:" + authUrl;
        } catch (UnsupportedEncodingException e) {
            logger.error("Error encoding OAuth parameters", e);
            redirectAttributes.addFlashAttribute("error", "Error initiating OAuth flow.");
            return "redirect:/staff/dashboard";
        }
    }

    /**
     * Handle OAuth callback from Citesphere
     */
    @RequestMapping(value = "/staff/citesphere/oauth/callback", method = RequestMethod.GET)
    public String handleOAuthCallback(
            @RequestParam String code,
            @RequestParam String state,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        logger.info("[DEBUG] OAuth callback received");
        logger.info("[DEBUG] Authorization code: {}", code != null ? "[PRESENT]" : "[MISSING]");
        logger.info("[DEBUG] State parameter: {}", state);
        
        try {
            // Verify state parameter
            String sessionState = (String) session.getAttribute("citesphere_oauth_state");
            logger.info("[DEBUG] Session state: {}", sessionState);
            logger.info("[DEBUG] Received state: {}", state);
            logger.info("[DEBUG] State match: {}", sessionState != null && sessionState.equals(state));
            
            if (sessionState == null || !sessionState.equals(state)) {
                logger.error("[DEBUG] OAuth state validation failed");
                redirectAttributes.addFlashAttribute("error", "Invalid OAuth state. Please try again.");
                return "redirect:/staff/dashboard";
            }
            
            // Exchange code for access token
            logger.info("[DEBUG] Exchanging authorization code for access token");
            String accessToken = exchangeCodeForToken(code, session);
            logger.info("[DEBUG] Access token received: {}", accessToken != null ? "[SUCCESS]" : "[FAILED]");
            
            if (accessToken != null) {
                // Store token in session
                session.setAttribute("citesphere_access_token", accessToken);
                logger.info("[DEBUG] Access token stored in session");
                redirectAttributes.addFlashAttribute("success", "Successfully connected to Citesphere!");
            } else {
                logger.error("[DEBUG] Failed to obtain access token");
                redirectAttributes.addFlashAttribute("error", "Failed to obtain access token from Citesphere.");
            }
            
        } catch (Exception e) {
            logger.error("Error handling OAuth callback", e);
            redirectAttributes.addFlashAttribute("error", "OAuth authentication failed: " + e.getMessage());
        } finally {
            // Clean up session
            session.removeAttribute("citesphere_oauth_state");
        }
        
        return "redirect:/staff/dashboard";
    }


    /**
     * Get user groups from Citesphere
     */
    @RequestMapping(value = "/staff/citesphere/groups", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getGroups(HttpSession session) {
        logger.info("[DEBUG] API call: getGroups() - Fetching user groups from Citesphere");
        
        try {
            ICitesphereManager citesphereManager = createCitesphereManager(session);
            logger.info("[DEBUG] CitesphereManager created successfully, making API call to get groups");
            Map<String, Object> groups = citesphereManager.getGroups();
            
            // Check for token expiry in response
            if (isTokenExpiredResponse(groups)) {
                logger.warn("[DEBUG] Token expired response detected, requesting re-authentication");
                session.removeAttribute("citesphere_access_token");
                session.removeAttribute("citesphere_refresh_token");
                session.removeAttribute("citesphere_token_expiry");
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Authentication expired");
                error.put("require_auth", true);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            logger.info("[DEBUG] Groups retrieved successfully: {} groups found", 
                       groups != null && groups.containsKey("data") ? "[DATA_PRESENT]" : "[NO_DATA]");
            logger.info("[DEBUG] Groups data: {}", groups);
            return ResponseEntity.ok(groups);
        } catch (IllegalStateException e) {
            logger.error("Authentication error: {}", e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("require_auth", true);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            logger.error("Error fetching groups from Citesphere", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch groups: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get collections for a specific group
     */
    @RequestMapping(value = "/staff/citesphere/groups/{groupId}/collections", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCollections(@PathVariable String groupId, HttpSession session) {
        logger.info("[DEBUG] API call: getCollections() - Group ID: {}", groupId);
        
        try {
            ICitesphereManager citesphereManager = createCitesphereManager(session);
            logger.info("[DEBUG] CitesphereManager created, fetching collections for group: {}", groupId);
            Map<String, Object> collections = citesphereManager.getCollections(groupId);
            logger.info("[DEBUG] Collections retrieved successfully for group {}: {} collections found", 
                       groupId, collections != null && collections.containsKey("data") ? "[DATA_PRESENT]" : "[NO_DATA]");
            return ResponseEntity.ok(collections);
        } catch (Exception e) {
            logger.error("Error fetching collections from Citesphere for group: " + groupId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch collections: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get items for a specific collection
     */
    @RequestMapping(value = "/staff/citesphere/groups/{groupId}/collections/{collectionId}/items", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCollectionItems(
            @PathVariable String groupId,
            @PathVariable String collectionId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            HttpSession session) {
        logger.info("[DEBUG] API call: getCollectionItems() - Group: {}, Collection: {}, Page: {}", groupId, collectionId, page);
        
        try {
            ICitesphereManager citesphereManager = createCitesphereManager(session);
            logger.info("[DEBUG] CitesphereManager created, fetching items for collection: {} in group: {}", collectionId, groupId);
            Map<String, Object> items = citesphereManager.getCollectionItems(groupId, collectionId, page);
            logger.info("[DEBUG] Collection items retrieved successfully: {} items found", 
                       items != null && items.containsKey("data") ? "[DATA_PRESENT]" : "[NO_DATA]");
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            logger.error("Error fetching collection items from Citesphere", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch collection items: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get all items for a specific group
     */
    @RequestMapping(value = "/staff/citesphere/groups/{groupId}/items", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getGroupItems(@PathVariable String groupId, HttpSession session) {
        logger.info("[DEBUG] API call: getGroupItems() - Group ID: {}", groupId);
        
        try {
            ICitesphereManager citesphereManager = createCitesphereManager(session);
            logger.info("[DEBUG] CitesphereManager created, fetching all items for group: {}", groupId);
            Map<String, Object> items = citesphereManager.getGroupItems(groupId);
            logger.info("[DEBUG] Group items retrieved successfully: {} items found", 
                       items != null && items.containsKey("data") ? "[DATA_PRESENT]" : "[NO_DATA]");
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            logger.error("Error fetching group items from Citesphere for group: " + groupId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch group items: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Import selected references from Citesphere to bibliography
     */
    @RequestMapping(value = "/staff/module/{moduleId}/slide/{slideId}/bibliography/{biblioId}/citesphere/import", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> importCitesphereReferences(
            @PathVariable String moduleId,
            @PathVariable String slideId,
            @PathVariable String biblioId,
            @RequestBody List<Map<String, Object>> selectedReferences) {
        
        logger.info("[DEBUG] API call: importCitesphereReferences() - Module: {}, Slide: {}, Bibliography: {}", moduleId, slideId, biblioId);
        logger.info("[DEBUG] Number of references to import: {}", selectedReferences != null ? selectedReferences.size() : 0);
        
        try {
            List<IReference> createdReferences = new ArrayList<>();
            
            for (Map<String, Object> refData : selectedReferences) {
                String title = extractField(refData, "title");
                String author = extractCreators(refData);
                String year = extractYear(refData);
                String journal = extractField(refData, "publicationTitle");
                String url = extractField(refData, "url");
                String volume = extractField(refData, "volume");
                String issue = extractField(refData, "issue");
                String pages = extractField(refData, "pages");
                String editors = extractField(refData, "editor");
                String type = extractField(refData, "itemType");
                String note = extractField(refData, "note");

                IReference reference = referenceManager.createReference(
                    biblioId, title, author, year, journal, url, volume, issue, pages, editors, type, note
                );
                
                createdReferences.add(reference);
                logger.info("Created reference: {}", title);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("imported_count", createdReferences.size());
            response.put("references", createdReferences);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error importing references from Citesphere", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to import references: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private ICitesphereManager createCitesphereManager(HttpSession session) {
        logger.info("[DEBUG] Creating CitesphereManager");
        
        // Check for OAuth token
        String accessToken = (String) session.getAttribute("citesphere_access_token");
        String refreshToken = (String) session.getAttribute("citesphere_refresh_token");
        Long expiryTime = (Long) session.getAttribute("citesphere_token_expiry");
        
        logger.info("[DEBUG] Access token in session: {}", accessToken != null ? "[PRESENT]" : "[MISSING]");
        logger.info("[DEBUG] Refresh token in session: {}", refreshToken != null ? "[PRESENT]" : "[MISSING]");
        logger.info("[DEBUG] Token expiry time: {}", expiryTime != null ? "[PRESENT]" : "[MISSING]");
        logger.info("[DEBUG] API URL: {}", citesphereApiUrl);
        
        if (accessToken != null) {
            // Create auth token with all available information
            CitesphereAuthToken authToken;
            if (refreshToken != null && expiryTime != null) {
                authToken = new CitesphereAuthToken(accessToken, refreshToken, expiryTime);
            } else {
                authToken = new CitesphereAuthToken(accessToken);
            }
            
            logger.info("[DEBUG] Creating CitesphereManager with API URL: {} and auth token", citesphereApiUrl);
            ICitesphereManager manager = new CitesphereManager(citesphereApiUrl, authToken);
            
            // Check if token is valid and refresh if needed
            if (!manager.isTokenValid()) {
                logger.info("[DEBUG] Token is invalid or expired, attempting refresh");
                try {
                    CitesphereAuthToken refreshedToken = manager.refreshToken();
                    
                    // Update session with new token information
                    session.setAttribute("citesphere_access_token", refreshedToken.getAccessToken());
                    if (refreshedToken.getRefreshToken() != null) {
                        session.setAttribute("citesphere_refresh_token", refreshedToken.getRefreshToken());
                    }
                    if (refreshedToken.getTokenExpiryTime() > 0) {
                        session.setAttribute("citesphere_token_expiry", refreshedToken.getTokenExpiryTime());
                    }
                    
                    logger.info("[DEBUG] Token refreshed successfully");
                    return manager;
                    
                } catch (CitesphereTokenException e) {
                    logger.error("[DEBUG] Token refresh failed: {}", e.getMessage());
                    // Clear invalid tokens from session
                    session.removeAttribute("citesphere_access_token");
                    session.removeAttribute("citesphere_refresh_token");
                    session.removeAttribute("citesphere_token_expiry");
                    throw new IllegalStateException("Citesphere token is invalid and cannot be refreshed. Please re-authenticate.", e);
                }
            }
            
            return manager;
        } else {
            // No authentication available
            logger.error("[DEBUG] No access token available in session - authentication required");
            throw new IllegalStateException("No Citesphere access token available. Please authenticate first.");
        }
    }

    /**
     * Exchange authorization code for access token
     */
    private String exchangeCodeForToken(String code, HttpSession session) {
        logger.info("[DEBUG] Starting token exchange process");
        logger.info("[DEBUG] Authorization code: {}", code != null ? "[PRESENT]" : "[MISSING]");
        
        try {
            OkHttpClient client = new OkHttpClient();
            String redirectUri = getCurrentBaseUrl() + "/staff/citesphere/oauth/callback";
            String tokenUrl = citesphereApiUrl + "oauth/token";
            
            logger.info("[DEBUG] Token exchange URL: {}", tokenUrl);
            logger.info("[DEBUG] Redirect URI: {}", redirectUri);
            logger.info("[DEBUG] Client ID: {}", citesphereClientId != null ? "[PRESENT]" : "[MISSING]");
            logger.info("[DEBUG] Client Secret: {}", citesphereClientSecret != null ? "[PRESENT]" : "[MISSING]");
            
            okhttp3.RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("client_id", citesphereClientId)
                .add("client_secret", citesphereClientSecret)
                .add("redirect_uri", redirectUri)
                .build();

            Request request = new Request.Builder()
                .url(tokenUrl)
                .post(formBody)
                .build();

            logger.info("[DEBUG] Making token exchange request to: {}", tokenUrl);
            
            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                logger.info("[DEBUG] Token exchange response code: {}", response.code());
                logger.info("[DEBUG] Token exchange response body: {}", responseBody);
                
                if (response.isSuccessful() && !responseBody.isEmpty()) {
                    logger.info("[DEBUG] Token exchange successful, parsing response");
                    ObjectMapper mapper = new ObjectMapper();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> tokenResponse = mapper.readValue(responseBody, Map.class);
                    String accessToken = (String) tokenResponse.get("access_token");
                    String refreshToken = (String) tokenResponse.get("refresh_token");
                    Number expiresIn = (Number) tokenResponse.get("expires_in");
                    
                    logger.info("[DEBUG] Access token extracted: {}", accessToken != null ? "[SUCCESS]" : "[FAILED]");
                    logger.info("[DEBUG] Refresh token extracted: {}", refreshToken != null ? "[SUCCESS]" : "[MISSING]");
                    logger.info("[DEBUG] Expires in: {}", expiresIn != null ? expiresIn.toString() + " seconds" : "[MISSING]");
                    
                    // Store additional token information in session
                    if (refreshToken != null) {
                        session.setAttribute("citesphere_refresh_token", refreshToken);
                    }
                    if (expiresIn != null) {
                        long expiryTime = System.currentTimeMillis() + (expiresIn.longValue() * 1000);
                        session.setAttribute("citesphere_token_expiry", expiryTime);
                    }
                    
                    return accessToken;
                } else {
                    logger.error("[DEBUG] Token exchange failed - Response code: {}, Body: {}", response.code(), responseBody);
                }
            }
        } catch (Exception e) {
            logger.error("Error exchanging code for token", e);
        }
        return null;
    }

    /**
     * Get current base URL for redirect URI
     */
    private String getCurrentBaseUrl() {
        return appBaseUrl != null && !appBaseUrl.isEmpty() ? appBaseUrl : "http://localhost:8080";
    }
    
    /**
     * Check if API response indicates token expiry
     */
    private boolean isTokenExpiredResponse(Map<String, Object> response) {
        if (response == null) {
            return false;
        }
        
        // Check for token_expired flag
        Boolean tokenExpired = (Boolean) response.get("token_expired");
        if (Boolean.TRUE.equals(tokenExpired)) {
            return true;
        }
        
        // Check for error messages indicating token issues
        String errorMessage = (String) response.get("error_message");
        if (errorMessage != null) {
            String lowerError = errorMessage.toLowerCase();
            return lowerError.contains("invalid") && lowerError.contains("token") ||
                   lowerError.contains("expired") && lowerError.contains("token") ||
                   lowerError.contains("unauthorized") ||
                   lowerError.contains("forbidden");
        }
        
        return false;
    }

    @SuppressWarnings("unchecked")
    private String extractField(Map<String, Object> refData, String fieldName) {
        try {
            Map<String, Object> data = (Map<String, Object>) refData.get("data");
            if (data != null && data.containsKey(fieldName)) {
                Object value = data.get(fieldName);
                return value != null ? value.toString() : "";
            }
        } catch (Exception e) {
            logger.warn("Error extracting field {}: {}", fieldName, e.getMessage());
        }
        return "";
    }

    /**
     * Extract creators (authors) from Citesphere reference data
     */
    @SuppressWarnings("unchecked")
    private String extractCreators(Map<String, Object> refData) {
        try {
            Map<String, Object> data = (Map<String, Object>) refData.get("data");
            if (data != null && data.containsKey("creators")) {
                List<Map<String, Object>> creators = (List<Map<String, Object>>) data.get("creators");
                StringBuilder authors = new StringBuilder();
                
                for (Map<String, Object> creator : creators) {
                    if (authors.length() > 0) {
                        authors.append("; ");
                    }
                    String firstName = creator.getOrDefault("firstName", "").toString();
                    String lastName = creator.getOrDefault("lastName", "").toString();
                    
                    if (!lastName.isEmpty()) {
                        authors.append(lastName);
                        if (!firstName.isEmpty()) {
                            authors.append(", ").append(firstName);
                        }
                    } else if (!firstName.isEmpty()) {
                        authors.append(firstName);
                    }
                }
                
                return authors.toString();
            }
        } catch (Exception e) {
            logger.warn("Error extracting creators: {}", e.getMessage());
        }
        return "";
    }

    /**
     * Extract year from Citesphere reference data
     */
    @SuppressWarnings("unchecked")
    private String extractYear(Map<String, Object> refData) {
        try {
            Map<String, Object> data = (Map<String, Object>) refData.get("data");
            if (data != null) {
                String date = extractField(refData, "date");
                if (!date.isEmpty()) {
                    String[] parts = date.split("-");
                    if (parts.length > 0 && parts[0].matches("\\d{4}")) {
                        return parts[0];
                    }
                }
                
                String accessDate = extractField(refData, "accessDate");
                if (!accessDate.isEmpty()) {
                    String[] parts = accessDate.split("-");
                    if (parts.length > 0 && parts[0].matches("\\d{4}")) {
                        return parts[0];
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Error extracting year: {}", e.getMessage());
        }
        return "";
    }
}
