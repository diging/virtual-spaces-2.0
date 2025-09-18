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

    @Value("${app_url}")
    private String appBaseUrl;

    @Autowired
    private IReferenceManager referenceManager;


    // initiate oauth authorization with citesphere
    @RequestMapping(value = "/staff/citesphere/oauth/authorize", method = RequestMethod.GET)
    public String initiateOAuth(HttpSession session, RedirectAttributes redirectAttributes) {
        
        if (citesphereClientId == null || citesphereClientId.isEmpty()) {
            logger.error("OAuth not configured - missing client ID");
            redirectAttributes.addFlashAttribute("error", "Citesphere OAuth is not configured. Please contact your administrator.");
            return "redirect:/staff/dashboard";
        }

        try {
            // generate state parameter for security
            String state = java.util.UUID.randomUUID().toString();
            session.setAttribute("citesphere_oauth_state", state);

            // build authorization url
            String baseUrl = citesphereApiUrl;
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

    // handle oauth callback from citesphere
    @RequestMapping(value = "/staff/citesphere/oauth/callback", method = RequestMethod.GET)
    public String handleOAuthCallback(
            @RequestParam String code,
            @RequestParam String state,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // verify state parameter
            String sessionState = (String) session.getAttribute("citesphere_oauth_state");
            
            if (sessionState == null || !sessionState.equals(state)) {
                logger.error("OAuth state validation failed");
                redirectAttributes.addFlashAttribute("error", "Invalid OAuth state. Please try again.");
                return "redirect:/staff/dashboard";
            }
            
            // exchange code for access token
            String accessToken = exchangeCodeForToken(code, session);
            
            if (accessToken != null) {
                session.setAttribute("citesphere_access_token", accessToken);
                redirectAttributes.addFlashAttribute("success", "Successfully connected to Citesphere!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to obtain access token from Citesphere.");
            }
            
        } catch (Exception e) {
            logger.error("Error handling OAuth callback", e);
            redirectAttributes.addFlashAttribute("error", "OAuth authentication failed: " + e.getMessage());
        } finally {
            // clean up session
            session.removeAttribute("citesphere_oauth_state");
        }
        
        return "redirect:/staff/dashboard";
    }


    // get user groups from citesphere
    @RequestMapping(value = "/staff/citesphere/groups", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getGroups(HttpSession session) {
        
        try {
            ICitesphereManager citesphereManager = createCitesphereManager(session);
            Map<String, Object> groups = citesphereManager.getGroups();
            
            // check for token expiry in response
            if (isTokenExpiredResponse(groups)) {
                session.removeAttribute("citesphere_access_token");
                session.removeAttribute("citesphere_refresh_token");
                session.removeAttribute("citesphere_token_expiry");
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Authentication expired");
                error.put("require_auth", true);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
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

    // get collections for a specific group
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

    // get items for a specific collection
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

    // get all items for a specific group
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

    // import selected references from citesphere to bibliography
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
            
            
            
            if (!createdReferences.isEmpty()) {
                IReference firstRef = createdReferences.get(0);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error importing references from Citesphere", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to import references: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private ICitesphereManager createCitesphereManager(HttpSession session) {
        // check for oauth token
        String accessToken = (String) session.getAttribute("citesphere_access_token");
        String refreshToken = (String) session.getAttribute("citesphere_refresh_token");
        Long expiryTime = (Long) session.getAttribute("citesphere_token_expiry");
        
        if (accessToken != null) {
            // create auth token with all available information
            CitesphereAuthToken authToken;
            if (refreshToken != null && expiryTime != null) {
                authToken = new CitesphereAuthToken(accessToken, refreshToken, expiryTime);
            } else {
                authToken = new CitesphereAuthToken(accessToken);
            }
            
            ICitesphereManager manager = new CitesphereManager(citesphereApiUrl, authToken);
            
            // check if token is valid and refresh if needed
            if (!manager.isTokenValid()) {
                try {
                    CitesphereAuthToken refreshedToken = manager.refreshToken();
                    
                    // update session with new token information
                    session.setAttribute("citesphere_access_token", refreshedToken.getAccessToken());
                    if (refreshedToken.getRefreshToken() != null) {
                        session.setAttribute("citesphere_refresh_token", refreshedToken.getRefreshToken());
                    }
                    if (refreshedToken.getTokenExpiryTime() > 0) {
                        session.setAttribute("citesphere_token_expiry", refreshedToken.getTokenExpiryTime());
                    }
                    
                    return manager;
                    
                } catch (CitesphereTokenException e) {
                    logger.error("Token refresh failed: {}", e.getMessage());
                    // clear invalid tokens from session
                    session.removeAttribute("citesphere_access_token");
                    session.removeAttribute("citesphere_refresh_token");
                    session.removeAttribute("citesphere_token_expiry");
                    throw new IllegalStateException("Citesphere token is invalid and cannot be refreshed. Please re-authenticate.", e);
                }
            }
            
            return manager;
        } else {
            // no authentication available
            logger.error("No access token available in session - authentication required");
            throw new IllegalStateException("No Citesphere access token available. Please authenticate first.");
        }
    }

    // exchange authorization code for access token
    private String exchangeCodeForToken(String code, HttpSession session) {
        
        try {
            OkHttpClient client = new OkHttpClient();
            String redirectUri = getCurrentBaseUrl() + "/staff/citesphere/oauth/callback";
            String tokenUrl = citesphereApiUrl + "oauth/token";
            
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
            
            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (response.isSuccessful() && !responseBody.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> tokenResponse = mapper.readValue(responseBody, Map.class);
                    String accessToken = (String) tokenResponse.get("access_token");
                    String refreshToken = (String) tokenResponse.get("refresh_token");
                    Number expiresIn = (Number) tokenResponse.get("expires_in");
                    
                    // store additional token information in session
                    if (refreshToken != null) {
                        session.setAttribute("citesphere_refresh_token", refreshToken);
                    }
                    if (expiresIn != null) {
                        long expiryTime = System.currentTimeMillis() + (expiresIn.longValue() * 1000);
                        session.setAttribute("citesphere_token_expiry", expiryTime);
                    }
                    
                    return accessToken;
                } else {
                    logger.error("Token exchange failed - Response code: {}, Body: {}", response.code(), responseBody);
                }
            }
        } catch (Exception e) {
            logger.error("Error exchanging code for token", e);
        }
        return null;
    }

    // get current base url for redirect uri
    private String getCurrentBaseUrl() {
        return appBaseUrl != null && !appBaseUrl.isEmpty() ? appBaseUrl : "http://localhost:8080";
    }
    
    // check if api response indicates token expiry
    private boolean isTokenExpiredResponse(Map<String, Object> response) {
        if (response == null) {
            return false;
        }
        
        // check for token_expired flag
        Boolean tokenExpired = (Boolean) response.get("token_expired");
        if (Boolean.TRUE.equals(tokenExpired)) {
            return true;
        }
        
        // check for error messages indicating token issues
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
                String result = value != null ? value.toString() : "";
                return result;
            }
        } catch (Exception e) {
            logger.warn("Error extracting field {}: {}", fieldName, e.getMessage());
        }
        return "";
    }

    // extract creators (authors) from citesphere reference data
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

    // extract year from citesphere reference data
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
