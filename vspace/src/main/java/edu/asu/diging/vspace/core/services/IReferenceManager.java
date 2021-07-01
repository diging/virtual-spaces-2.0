package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;

public interface IReferenceManager {

    IReference getReference(String referenceId);
    
    IReference createReference(IBiblioBlock biblio, Reference reference);

    void updateReference(Reference reference);

    void deleteReferenceById(String referenceId, String BiblioId);
    
    void deleteRefences(List<IReference> references, String BiblioId);

    List<IReference> getReferencesForBiblio(String biblioId);

}
