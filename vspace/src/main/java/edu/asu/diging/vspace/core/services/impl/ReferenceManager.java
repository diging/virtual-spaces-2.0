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

import edu.asu.diging.vspace.core.data.ReferenceRepository;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IReferenceManager;

@Service
public class ReferenceManager implements IReferenceManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ReferenceRepository referenceRepo;
    
    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @Value("${page_size}")
    private int pageSize;

    @Override
    public IReference createReference(String biblioId, String title, String author,String year,String journal, String url, String volume,String issue, String pages,String editor, String type, String note, String visibility) {

    	IReference reference = new Reference();
        reference.setAuthor(author);
        reference.setTitle(title);
        reference.setYear(year);
        reference.setJournal(journal);
        reference.setUrl(url);
        reference.setVolume(volume);
        reference.setIssue(issue);
        reference.setPages(pages);
        reference.setEditors(editor);
        reference.setType(type);
        reference.setNote(note);
        if(visibility == "Private") {
        	reference.setVisibility(false);
        }
        else {
        	reference.setVisibility(true);
        }

        BiblioBlock biblio = contentBlockManager.getBiblioBlock(biblioId);
        reference.getBiblios().add(biblio);
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
    public void deleteReferenceById(String referenceId, String biblioId) {
        if (referenceId == null) {
            logger.warn("Reference Id cannot be null.");
            return;
        }
        referenceRepo.delete((Reference)getReference(referenceId));
    }

    @Override
    public void deleteReferences(List<IReference> references, String biblioId) {
        for(IReference ref : references) {
            deleteReferenceById(ref.getId(), biblioId);
        }  
    }

    @Override
    public List<IReference> getReferencesForBiblio(String biblioId) {
        return new ArrayList<>(referenceRepo.findByBiblios_Id(biblioId));
    }
    
    @Override
    public List<IReference> getAllReferences(int pageNo, String sortedBy, String order) {
        Sort sortingParameters = getSortingParameters(sortedBy, order);
        if (pageNo < 1) {
            pageNo = 1;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sortingParameters);
        Page<Reference> references = referenceRepo.findAll(pageable);
        if (references.getContent().size() == 0 && references.getTotalPages() > 0) {
            pageable = PageRequest.of(references.getTotalPages() - 1, pageSize, sortingParameters);
            references = referenceRepo.findAll(pageable);
        }
        List<IReference> results = new ArrayList<>();
        if (references != null) {
            references.getContent().forEach(ref -> results.add(ref));
        }
        return results;
    }
    
    @Override
    public long getTotalReferenceCount() {
        return referenceRepo.count();
    }
    
    @Override
    public long getTotalPages() {
        long count = referenceRepo.count();
        return (count % pageSize == 0) ? count / pageSize : (count / pageSize) + 1;
    }
    
    private Sort getSortingParameters(String sortedBy, String order) {
        Sort sortingParameters = Sort.by(SortByField.CREATION_DATE.getValue()).descending();
        if (sortedBy != null && SortByField.getAllValues().contains(sortedBy)) {
            sortingParameters = Sort.by(sortedBy);
        }
        if (order != null && order.equalsIgnoreCase(Sort.Direction.ASC.toString())) {
            sortingParameters = sortingParameters.ascending();
        } else {
            sortingParameters = sortingParameters.descending();
        }
        return sortingParameters;
    }
}
