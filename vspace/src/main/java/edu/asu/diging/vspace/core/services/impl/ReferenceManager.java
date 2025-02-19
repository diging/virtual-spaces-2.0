package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ReferenceRepository;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.services.IReferenceManager;

@Service
public class ReferenceManager implements IReferenceManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ReferenceRepository referenceRepo;

    @Override
    public IReference createReference(IBiblioBlock biblio, String title, String author,String year,String journal, String url, String volume,String issue, String pages,String editor, String type, String note, String visibility) {
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
    public void updateReference(Reference reference) {
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
}
