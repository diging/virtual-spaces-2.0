package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ReferenceDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;

public interface IReferenceManager {

    IReference getReference(String referenceId);
    
    IReference createReference(IBiblioBlock biblio, Reference reference);

    void updateReference(Reference reference);

    void deleteReferenceById(String referenceId, String BiblioId);
    
    void deleteReferences(List<IReference> references, String BiblioId);

    List<IReference> getReferencesForBiblio(String biblioId);
    
    List<IReference> getReferences(int pageNo);
    
    List<IReference> getReferences(int pageNo, String sortedBy, String order);

    long getTotalReferenceCount();

    long getTotalPages();

    int validatePageNumber(int pageNo);

    void editReference(String referenceId, IReference referenceData) throws ReferenceDoesNotExistException;

    IReference getReferenceById(String referenceId) throws ReferenceDoesNotExistException;

}
