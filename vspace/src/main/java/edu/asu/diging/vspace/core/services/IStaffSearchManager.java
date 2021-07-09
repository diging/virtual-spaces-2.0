package edu.asu.diging.vspace.core.services;

import java.util.HashSet;

import edu.asu.diging.vspace.core.model.IVSpaceElement;

public interface IStaffSearchManager {

    HashSet<IVSpaceElement> searchInSpaces(String search,int page);

    HashSet<IVSpaceElement> searchInModules(String search,int page);

    HashSet<IVSpaceElement> searchInSlides(String search,int page);

    HashSet<IVSpaceElement> searchInSlideTexts(String search,int page);

    int validatePageNumber(int pageNo, long totalPages);

    long getTotalSpacePages(String search);
    
    long getTotalModulePages(String search);
    
    long getTotalSlidePages(String search);
    
    long getTotalSlideTextPages(String search);
}
