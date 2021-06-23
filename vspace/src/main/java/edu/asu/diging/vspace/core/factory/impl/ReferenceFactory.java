package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IReferenceFactory;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;

@Service
public class ReferenceFactory implements IReferenceFactory {
    
    @Override
    public IReference createReference(IBiblioBlock biblio, Reference reference) {
        reference.setBiblio(biblio);
        return reference;        
    }   
}