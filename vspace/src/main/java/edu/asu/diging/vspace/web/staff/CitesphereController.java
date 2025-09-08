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

    @Value("${citesphere_username:}")
    private String citesphereUsername;

    @Value("${citesphere_password:}")
    private String citespherePassword;

    @Autowired
    private IReferenceManager referenceManager;

    /**
     * Get user groups from Citesphere
     */
    @RequestMapping(value = "/staff/citesphere/groups", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getGroups() {
        try {
            ICitesphereManager citesphereManager = createCitesphereManager();
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
    public ResponseEntity<Map<String, Object>> getCollections(@PathVariable String groupId) {
        try {
            ICitesphereManager citesphereManager = createCitesphereManager();
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
            @RequestParam(value = "page", defaultValue = "0") int page) {
        try {
            ICitesphereManager citesphereManager = createCitesphereManager();
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
    public ResponseEntity<Map<String, Object>> getGroupItems(@PathVariable String groupId) {
        try {
            ICitesphereManager citesphereManager = createCitesphereManager();
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

    private ICitesphereManager createCitesphereManager() {
        CitesphereAuthToken authToken = new CitesphereAuthToken(citesphereUsername, citespherePassword);
        return new CitesphereManager(citesphereApiUrl, authToken);
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
