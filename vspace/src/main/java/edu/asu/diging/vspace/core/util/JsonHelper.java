package edu.asu.diging.vspace.core.util;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a utility method to convert java object to Json.
 * 
 * @author abiswa15
 *
 */
@Service
public class JsonHelper {

    private ObjectMapper mapper;

    public ObjectMapper getMapper() {

        if (mapper == null) {
            this.mapper = new ObjectMapper();
        }
        return this.mapper;
    }

    public String convertToJson(Object object) throws JsonProcessingException {

        ObjectMapper mapper = getMapper();
        String jsonResult = mapper.writeValueAsString(object);
        return jsonResult;
    }
}
