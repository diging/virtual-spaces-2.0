package edu.asu.diging.vspace.core.util;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * This is a utility method to convert java object to Json.
 * @author abiswa15
 *
 */
@Service
public class JsonHelper {

    public String convertToJson(ObjectMapper mapper,Object object) throws JsonProcessingException {
        String jsonResult = mapper.writeValueAsString(object);
        return jsonResult;
    }
}
