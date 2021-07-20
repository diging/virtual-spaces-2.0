package edu.asu.diging.vspace.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    private static ObjectMapper mapper;
    
    static
    {
        mapper = new ObjectMapper();
    }
    
    public static String convertToJson(Object object) throws JsonProcessingException
    {
        String jsonResult="";
        jsonResult=mapper.writeValueAsString(object);
        return jsonResult;
    }
    
    public static ObjectMapper getMapper() {
        return mapper;
    }
}
