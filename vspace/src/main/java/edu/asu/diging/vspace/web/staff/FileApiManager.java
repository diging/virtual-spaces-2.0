package edu.asu.diging.vspace.web.staff;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

@Service
public class FileApiManager {
    
    CreationReturnValue storeFile(byte[] fileContent, String filename) {
        CreationReturnValue returnValue = new CreationReturnValue();
        return returnValue;
    }

}
