package edu.asu.diging.vspace.core.services;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

public interface ISpaceManager {

    CreationReturnValue storeSpace(ISpace space, byte[] image, String filename);

    CreationReturnValue storeSpace(ISpace space, IVSImage image);

    ISpace getSpace(String id);

    ISpace getFullyLoadedSpace(String id);

    List<ISpace> getAllSpaces();

    List<SpaceLink> getOutgoingLinks(String id);

    List<ISpace> getSpacesWithStatus(SpaceStatus status);

    List<ISpace> getSpacesWithImageId(String imageId);
 
    void deleteSpaceById(String id) throws SpaceDoesNotExistException;

    List<SpaceLink> getIncomingLinks(String id);
    
    Iterable<Space> addIncomingLinkInfoToSpaces(Iterable<Space> spaces);
    
    Page<ISpace> findByNameOrDescription(Pageable requestedPage,String searchText);

    void updateNameAndDescription(ISpace space, SpaceForm spaceForm);

    void addSpaceDetails(ISpace space, LocalizedTextForm names);
    
}
