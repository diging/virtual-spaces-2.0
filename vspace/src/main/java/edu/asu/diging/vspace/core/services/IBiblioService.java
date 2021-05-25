package edu.asu.diging.vspace.core.services;


import java.util.List;

import edu.asu.diging.vspace.core.exception.BibliographyDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

public interface IBiblioService {

//    ImageData getImageData(byte[] image);

//    ImageData getImageDimensions(IVSImage image, int width, int height);

    List<IBiblioBlock> getBibliographies(int pageNo);
    
    List<IBiblioBlock> getBibliographies(int pageNo, String sortedBy, String order);

    long getTotalBiblioCount();

    long getTotalPages();

    int validatePageNumber(int pageNo);

    void editBibliography(String biblioId, ImageForm imageForm) throws BibliographyDoesNotExistException;

    IBiblioBlock getBiblioById(String biblioId) throws BibliographyDoesNotExistException;

//    void addCategory(IBiblioBlock image, ImageCategory category);

//    void removeCategory(IBiblioBlock image, ImageCategory category);

    List<IBiblioBlock> findByFilenameOrNameContains(String searchTerm);

}