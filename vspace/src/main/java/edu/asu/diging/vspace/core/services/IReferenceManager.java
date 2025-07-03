package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;

public interface IReferenceManager {

    IReference getReference(String referenceId);
    
    IReference createReference(String biblioId, String title, String author,String year,String journal, String url, String volume,String issue, String pages,String editor, String type, String note, String visibility);

    void updateReference(IReference reference);

    void deleteReferenceById(String referenceId, String BiblioId);
    
    void deleteReferences(List<IReference> references, String BiblioId);

    List<IReference> getReferencesForBiblio(String biblioId);

}
