package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface ISpaceManager {

    CreationReturnValue storeSpace(ISpace space, byte[] image, String filename);
    
    CreationReturnValue storeSpace(ISpace space, IVSImage image);

    ISpace getSpace(String id);

    ISpace getFullyLoadedSpace(String id);

    List<ISpace> getAllSpaces();
    
    void deleteSpaceById(String id) throws SpaceDoesNotExistException;
}