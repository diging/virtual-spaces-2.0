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
    
    List<ISpace> findByName(String name);

    /**
     * Method to return the requested spaces
     * 
     * @param pageNo. if pageNo<1, 1st page is returned, if pageNo>total pages,last
     *                page is returned
     * @return list of images in the requested pageNo and requested order.
     */
    List<ISpace> getSpaces(int pageNo, String sortedBy, String order);

    /**
     * Method to return the total pages sufficient to display all spaces
     * 
     * @return totalPages required to display all spaces in DB
     */
    long getTotalPages();

    /**
     * Method to return page number after validation
     * 
     * @param pageNo page provided by calling method
     * @return 1 if pageNo less than 1 and lastPage if pageNo greater than
     *         totalPages.
     */
    int validatePageNumber(int pageNo);

    List<ISpace> getSpaces(int pageNo);
}
