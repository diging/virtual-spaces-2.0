package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ReferenceRepository;
import edu.asu.diging.vspace.core.exception.ReferenceDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.services.IReferenceManager;

@Service
public class ReferenceManager implements IReferenceManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ReferenceRepository referenceRepo;
    
    @Value("${page_size}")
    private int pageSize;


    @Override
    public IReference saveReference(IBiblioBlock biblio, Reference reference) {
        reference.getBiblios().add((BiblioBlock) biblio);
        return referenceRepo.save((Reference) reference);
    }

    @Override
    public IReference getReference(String referenceId) {
        Optional<Reference> reference = referenceRepo.findById(referenceId);
        if (reference.isPresent()) {
            return reference.get();
        }
        return null;
    }

    @Override
    public void updateReference(IReference reference) {
        referenceRepo.save((Reference) reference);
    }

    @Override
    public void deleteReferenceById(String referenceId) throws ReferenceDoesNotExistException {
        if (referenceId == null) {
            logger.warn("Reference Id cannot be null.");
            return;
        }

        try {
            referenceRepo.deleteById(referenceId);
        } catch (EmptyResultDataAccessException e) {
            throw new ReferenceDoesNotExistException(e);
        }
    }

    @Override
    public List<IReference> getReferencesForBiblio(String biblioId) {
        return referenceRepo.findByBiblios_Id(biblioId);
    }
 
    @Override
    public List<IReference> getReferences(int pageNo) {
        return getReferences(pageNo, SortByField.CREATION_DATE.getValue(), Sort.Direction.DESC.toString());
    }

    @Override
    public List<IReference> getReferences(int pageNo, String sortedBy, String order) {
        Sort sortingParameters = getSortingParameters(sortedBy, order);
        pageNo = validatePageNumber(pageNo);
        Pageable sortByRequestedField = PageRequest.of(pageNo - 1, pageSize, sortingParameters);
        Page<Reference> references = referenceRepo.findAll(sortByRequestedField);
        
        List<IReference> results = new ArrayList<>();
        if(references != null) {
            references.getContent().forEach(i -> results.add(i));
        }
        return results;
    }

    @Override
    public long getTotalReferenceCount() {
        return referenceRepo.count();
    }

    @Override
    public long getTotalPages() {
        return (referenceRepo.count() % pageSize==0) ? referenceRepo.count() / pageSize : (referenceRepo.count() / pageSize) + 1;
    }

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

    @Override
    public IReference getReferenceById(String referenceId) {
        Optional<Reference> referenceOptional = referenceRepo.findById(referenceId);
        if(referenceOptional.isPresent()) {
            return referenceOptional.get();
        } 
        return null;
    }
    
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
    
}
