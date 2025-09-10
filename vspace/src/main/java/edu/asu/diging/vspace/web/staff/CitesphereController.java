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

@Controller
public class CitesphereController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${citesphere_api_url:https://citesphere.org/api}")
    private String citesphereApiUrl;

    @Value("${citesphere_client_id:}")
    private String citesphereClientId;

    @Value("${citesphere_client_secret:}")
    private String citesphereClientSecret;

    @Value("${app_base_url:}")
    private String appBaseUrl;

    @Autowired
    private IReferenceManager referenceManager;


    /**
     * Initiate OAuth authorization with Citesphere
     */
    @RequestMapping(value = "/staff/citesphere/oauth/authorize", method = RequestMethod.GET)
    public String initiateOAuth(HttpSession session, RedirectAttributes redirectAttributes) {
        if (citesphereClientId == null || citesphereClientId.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Citesphere OAuth is not configured. Please contact your administrator.");
            return "redirect:/staff/dashboard";
        }

        try {
            // Generate state parameter for security
            String state = java.util.UUID.randomUUID().toString();
            session.setAttribute("citesphere_oauth_state", state);

            // Build authorization URL
            String baseUrl = citesphereApiUrl.replace("/api", "");
            String redirectUri = getCurrentBaseUrl() + "/staff/citesphere/oauth/callback";
            
            String authUrl = baseUrl + "/oauth/authorize" +
                    "?response_type=code" +
                    "&client_id=" + java.net.URLEncoder.encode(citesphereClientId, "UTF-8") +
                    "&state=" + java.net.URLEncoder.encode(state, "UTF-8") +
                    "&redirect_uri=" + java.net.URLEncoder.encode(redirectUri, "UTF-8");

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
        
        try {
            // Verify state parameter
            String sessionState = (String) session.getAttribute("citesphere_oauth_state");
            if (sessionState == null || !sessionState.equals(state)) {
                redirectAttributes.addFlashAttribute("error", "Invalid OAuth state. Please try again.");
                return "redirect:/staff/dashboard";
            }
            
            // Exchange code for access token
            String accessToken = exchangeCodeForToken(code);
            if (accessToken != null) {
                // Store token in session
                session.setAttribute("citesphere_access_token", accessToken);
                redirectAttributes.addFlashAttribute("success", "Successfully connected to Citesphere!");
            } else {
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
        try {
            ICitesphereManager citesphereManager = createCitesphereManager(session);
            Map<String, Object> groups = citesphereManager.getGroups();
            return ResponseEntity.ok(groups);
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
        try {
            ICitesphereManager citesphereManager = createCitesphereManager(session);
            Map<String, Object> collections = citesphereManager.getCollections(groupId);
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
        try {
            ICitesphereManager citesphereManager = createCitesphereManager(session);
            Map<String, Object> items = citesphereManager.getCollectionItems(groupId, collectionId, page);
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
        try {
            ICitesphereManager citesphereManager = createCitesphereManager(session);
            Map<String, Object> items = citesphereManager.getGroupItems(groupId);
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
        // Check for OAuth token
        String accessToken = (String) session.getAttribute("citesphere_access_token");
        
        if (accessToken != null) {
            // Use OAuth token
            CitesphereAuthToken authToken = new CitesphereAuthToken(accessToken);
            return new CitesphereManager(citesphereApiUrl, authToken);
        } else {
            // No authentication available
            throw new IllegalStateException("No Citesphere access token available. Please authenticate first.");
        }
    }

    /**
     * Exchange authorization code for access token
     */
    private String exchangeCodeForToken(String code) {
        try {
            OkHttpClient client = new OkHttpClient();
            
            RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("client_id", citesphereClientId)
                .add("client_secret", citesphereClientSecret)
                .add("redirect_uri", getCurrentBaseUrl() + "/staff/citesphere/oauth/callback")
                .build();

            Request request = new Request.Builder()
                .url(citesphereApiUrl.replace("/api", "") + "/oauth/token")
                .post(formBody)
                .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                logger.debug("Token exchange response: {} - {}", response.code(), responseBody);
                
                if (response.isSuccessful() && !responseBody.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> tokenResponse = mapper.readValue(responseBody, Map.class);
                    return (String) tokenResponse.get("access_token");
                } else {
                    logger.error("Token exchange failed: {} - {}", response.code(), responseBody);
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
