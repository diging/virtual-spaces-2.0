package edu.asu.diging.vspace.core.services;


import java.util.List;

import edu.asu.diging.vspace.core.exception.BibliographyDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;

public interface IBiblioService {

    List<IBiblioBlock> getBibliographies(int pageNo);
    
    List<IBiblioBlock> getBibliographies(int pageNo, String sortedBy, String order);

    long getTotalBiblioCount();

    long getTotalPages();

    int validatePageNumber(int pageNo);

    void editBibliography(String biblioId, IBiblioBlock biblioData) throws BibliographyDoesNotExistException;

    IBiblioBlock getBiblioById(String biblioId) throws BibliographyDoesNotExistException;

}