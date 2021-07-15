package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ReferenceDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;

public interface IReferenceManager {

    IReference getReference(String referenceId);
    
    IReference saveReference(IBiblioBlock biblio, Reference reference);

    void updateReference(IReference reference);

    void deleteReferenceById(String referenceId) throws ReferenceDoesNotExistException;
    
    List<IReference> getReferencesForBiblio(String biblioId);
    
    List<IReference> getReferences(int pageNo);
    
    List<IReference> getReferences(int pageNo, String sortedBy, String order);

    long getTotalReferenceCount();

    long getTotalPages();

    int validatePageNumber(int pageNo);

    void updateReference(String referenceId, IReference referenceData);

    IReference getReferenceById(String referenceId);

}
