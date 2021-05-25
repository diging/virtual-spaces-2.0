package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.BiblioRepository;
import edu.asu.diging.vspace.core.exception.BibliographyDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.services.IBiblioService;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

@Service
public class BiblioService implements IBiblioService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BiblioRepository biblioRepo;

    @Value("${page_size}")
    private int pageSize;

    private Sort getSortingParameters(String sortedBy, String order) {
        Sort sortingParameters = Sort.by(SortByField.CREATION_DATE.getValue()).descending();
        if(sortedBy!=null && SortByField.getAllValues().contains(sortedBy)) {
            sortingParameters = Sort.by(sortedBy);
        }
        if(order!=null && order.equalsIgnoreCase(Sort.Direction.ASC.toString())) {
            sortingParameters = sortingParameters.ascending();
        } else {
            sortingParameters = sortingParameters.descending();
        }
        return sortingParameters;
    }
    
    @Override
    public List<IBiblioBlock> getBibliographies(int pageNo) {
        return getBibliographies(pageNo, SortByField.CREATION_DATE.getValue(), Sort.Direction.DESC.toString());
    }
    
    /**
     * Method to return the requested images
     * 
     * @param pageNo. if pageNo<1, 1st page is returned, if pageNo>total pages,last
     *                page is returned
     * @return list of images in the requested pageNo and requested order.
     */

    @Override
    public List<IBiblioBlock> getBibliographies(int pageNo, String sortedBy, String order) {
        Sort sortingParameters = getSortingParameters(sortedBy, order);
        pageNo = validatePageNumber(pageNo);
        Pageable sortByRequestedField = PageRequest.of(pageNo - 1, pageSize, sortingParameters);
        Page<BiblioBlock> bibliographies;
        bibliographies = biblioRepo.findAll(sortByRequestedField);
        
        List<IBiblioBlock> results = new ArrayList<>();
        if(bibliographies != null) {
            bibliographies.getContent().forEach(i -> results.add(i));
        }
        return results;
    }
    
    /**
     * Method to return the total image count
     * 
     * @return total count of images in DB
     */

    @Override
    public long getTotalBiblioCount() {
        return biblioRepo.count();
    }
    
    /**
     * Method to return the total pages sufficient to display all images
     * 
     * @return totalPages required to display all images in DB
     */

    
    @Override
    public long getTotalPages() {
        return (biblioRepo.count() % pageSize==0) ? biblioRepo.count() / pageSize : (biblioRepo.count() / pageSize) + 1;
    }
    
    /**
     * Method to return page number after validation
     * 
     * @param pageNo page provided by calling method
     * @return 1 if pageNo less than 1 and lastPage if pageNo greater than
     *         totalPages.
     */

    @Override
    public int validatePageNumber(int pageNo) {
        long totalPages = getTotalPages();
        if(pageNo<1) {
            return 1;
        } else if(pageNo>totalPages) {
            return (totalPages==0) ? 1:(int) totalPages;
        }
        return pageNo;
    }

    /**
     * Method to edit image details
     * 
     * @param imageId   - image unique identifier
     * @param imageForm - ImageForm with updated values for image fields
     * @return throws ImageDoesNotExistException if no image exists with id,
     */
    @Override
    public void editBibliography(String imageId, ImageForm imageForm) throws BibliographyDoesNotExistException {
        IBiblioBlock bibliography = getBiblioById(imageId);
        bibliography.setName(imageForm.getName());
        bibliography.setDescription(imageForm.getDescription());
        biblioRepo.save((BiblioBlock) bibliography);
    }

    /**
     * Method to lookup image by id
     * 
     * @param imageId - image unique identifier
     * @return image with provided image id if it exists, throws
     *         ImageDoesNotExistException if no image exists with id,
     */
    @Override
    public IBiblioBlock getBiblioById(String biblioId) throws BibliographyDoesNotExistException {
        Optional<BiblioBlock> biblioOptional = biblioRepo.findById(biblioId);
        if(biblioOptional.isPresent()) {
            return biblioOptional.get();
        } else {
            throw new BibliographyDoesNotExistException("Bibliography doesn't exist for biblio id" + biblioId);
        }
    }

    @Override
    public List<IBiblioBlock> findByFilenameOrNameContains(String searchTerm) {
        String likeSearchTerm = "%" + searchTerm + "%";
        List<BiblioBlock> results = null;//biblioRepo.findByFilenameLikeOrNameLike(likeSearchTerm, likeSearchTerm);
        List<IBiblioBlock> biblioResults = new ArrayList<>();
        results.forEach(r -> biblioResults.add(r));
        return biblioResults;
    }

}
