package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;

public interface IReferenceFactory {

    IReference createReference(IBiblioBlock biblio, Reference reference);

}
