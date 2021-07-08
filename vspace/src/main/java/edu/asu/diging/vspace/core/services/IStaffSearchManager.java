package edu.asu.diging.vspace.core.services;

import java.util.HashSet;

import edu.asu.diging.vspace.core.model.IVSpaceElement;

public interface IStaffSearchManager {

    HashSet<IVSpaceElement> searchSpaces(String search,int page,long totalSpacePage,String tab,StringBuilder strSpacePageNo);

    HashSet<IVSpaceElement> searchModules(String search,int page,long totalModulePage,String tab,StringBuilder strModulePageNo);

    HashSet<IVSpaceElement> searchSlides(String search,int page,long totalSlidePage,String tab,StringBuilder strSlidePageNo);

    HashSet<IVSpaceElement> searchSlideTexts(String search,int page,long totalSlideTextPage,String tab,StringBuilder strSlideTextPageNo);

    int validatePageNumber(int pageNo, long totalPages);

    long getTotalPages(long count);

    long getCountOfSearchedModule(String search);

    long getCountOfSearchedSpace(String search);

    long getCountOfSearchedSlide(String search);

    long getCountOfSearchedSlideText(String search);

}
