package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;
import edu.asu.diging.vspace.web.CustomResponses;

public interface ISpaceManager {

    CreationReturnValue storeSpace(ISpace space, byte[] image, String filename);

    ISpace getSpace(String id);

    ISpace getFullyLoadedSpace(String id);

    List<ISpace> getAllSpaces();
    
    CustomResponses deleteSpaceById(String id);
}