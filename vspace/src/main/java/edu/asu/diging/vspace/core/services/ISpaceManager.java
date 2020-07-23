package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface ISpaceManager {

    CreationReturnValue storeSpace(ISpace space, byte[] image, String filename);
    
    CreationReturnValue storeSpace(ISpace space, IVSImage image);

    ISpace getSpace(String id);

    ISpace getFullyLoadedSpace(String id);

    List<ISpace> getAllSpaces();

    List<SpaceLink> getOutgoingLinks(String id);

    List<ISpace> getSpacesWithStatus(SpaceStatus status);

    void deleteSpaceById(String id) throws SpaceDoesNotExistException;
    
    List<SpaceLink> getIncomingLinks(String id);

}
