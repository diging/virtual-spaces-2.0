package edu.asu.diging.vspace.core.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISearchManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.impl.model.SearchModuleResults;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideResults;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideTextBlockResults;
import edu.asu.diging.vspace.core.services.impl.model.SearchSpaceResults;


public abstract class SearchManager<T> implements ISearchManager<T> {
    
    @Value("${page_size}")
    private int pageSize;
    
    @Autowired
    private ISequenceManager sequenceManager;
    
    protected abstract  Page<ISpace> spaceSearch(Pageable requestedPageForSpace ,  String searchTerm);
    
    protected abstract  Page<T> moduleSearch(Pageable requestedPageForModule ,  String searchTerm);
    
    protected abstract  Page<ISlide> slideSearch(Pageable requestedPageForSlide ,  String searchTerm);

    protected abstract  Page<ISlide> slideTextSearch(Pageable requestedPageForSlideText ,  String searchTerm);   
    
    /**
     * Method to return the requested spaces whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return set of spaces whose name or description contains the search string in
     *         the requested page.
     */
    @Override
    public Page<ISpace> searchInSpaces(String searchTerm, int page) {
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForSpace = PageRequest.of(page - 1, pageSize);       
        Page<ISpace> spacePage =  spaceSearch(requestedPageForSpace, searchTerm);
        
        int totalSpacePage = spacePage.getTotalPages();
        /* if page>total pages,last page is returned */
        if (page > totalSpacePage) {
            totalSpacePage = totalSpacePage == 0 ? 1 : totalSpacePage;
            requestedPageForSpace = PageRequest.of(totalSpacePage - 1, pageSize);   
            spacePage = spaceSearch(requestedPageForSpace,  searchTerm);
        }
        return spacePage;
    }
    
    /**
     * Method to return the requested modules whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return set of modules whose name or description contains the search string
     *         in the requested page.
     */
    @Override
    public Page<T> searchInModules(String searchTerm, int page) {
        /* if page<1, 1st page is returned */
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForModule = PageRequest.of(page - 1, pageSize);
        Page<T> modulePage = moduleSearch(requestedPageForModule, searchTerm);
//        Page<IModule> modulePage = moduleManager.findByNameOrDescription(requestedPageForModule, searchTerm);
        int totalModulePage = modulePage.getTotalPages();
        /*
         * spring will just return an empty dataset if a page that is greater than the
         * total number of pages is returned. So, the page given (except when it's < 1,
         * then set it to 1) and then checking if the total page count is smaller than
         * the given one (if so, make another request). In most cases the page number
         * will be within the range of pages, so we would need only one db call. Only in
         * cases where the page number is wrong, would a second one be needed.if
         * page>total pages,last page is returned.
         */
        if (page > totalModulePage) {
            totalModulePage = totalModulePage == 0 ? 1 : totalModulePage;
            requestedPageForModule = PageRequest.of(totalModulePage - 1, pageSize);
            
            modulePage = moduleSearch(requestedPageForModule, searchTerm);
//            modulePage = moduleManager.findByNameOrDescription(requestedPageForModule, searchTerm);
        }
        return modulePage;
    }

    /**
     * Method to return the requested slides whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return set of slides whose name or description contains the search string in
     *         the requested page.
     */
    @Override
    public Page<ISlide> searchInSlides(String searchTerm, int page) {
        /* if page<1, 1st page is returned */
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForSlide = PageRequest.of(page - 1, pageSize);
        Page<ISlide> slidePage = slideSearch(requestedPageForSlide, searchTerm);
//        Page<ISlide> slidePage = slideManager.findByNameOrDescription(requestedPageForSlide, searchTerm);
        int totalSlidePage = slidePage.getTotalPages();
        /*
         * spring will just return an empty dataset if a page that is greater than the
         * total number of pages is returned. So, the page given (except when it's < 1,
         * then set it to 1) and then checking if the total page count is smaller than
         * the given one (if so, make another request). In most cases the page number
         * will be within the range of pages, so we would need only one db call. Only in
         * cases where the page number is wrong, would a second one be needed.if
         * page>total pages,last page is returned.
         */
        if (page > totalSlidePage) {
            totalSlidePage = totalSlidePage == 0 ? 1 : totalSlidePage;
            requestedPageForSlide = PageRequest.of(totalSlidePage - 1, pageSize);
            slidePage = slideSearch(requestedPageForSlide, searchTerm);
        }
        return slidePage;
    }

    /**
     * Method to return the requested slides whose text blocks contains the search
     * string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return list of slides whose text blocks contains the search string in the
     *         requested page.
     */
    @Override
    public Page<ISlide> searchInSlideTexts(String searchTerm, int page) {
        /* if page<1, 1st page is returned */
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForSlideText = PageRequest.of(page - 1, pageSize);
//        Page<ISlide> slidetextPage = textContentBlockRepo.findWithNameOrDescription(requestedPageForSlideText,
//                searchTerm);
        Page<ISlide> slidetextPage = slideTextSearch(requestedPageForSlideText,
                searchTerm);
        
        int totalSlideTextPage = slidetextPage.getTotalPages();
        /*
         * spring will just return an empty dataset if a page that is greater than the
         * total number of pages is returned. So, the page given (except when it's < 1,
         * then set it to 1) and then checking if the total page count is smaller than
         * the given one (if so, make another request). In most cases the page number
         * will be within the range of pages, so we would need only one db call. Only in
         * cases where the page number is wrong, would a second one be needed.if
         * page>total pages,last page is returned.
         */
        if (page > totalSlideTextPage) {
            totalSlideTextPage = totalSlideTextPage == 0 ? 1 : totalSlideTextPage;
            requestedPageForSlideText = PageRequest.of(totalSlideTextPage - 1, pageSize);
            slidetextPage = slideTextSearch(requestedPageForSlideText,
                    searchTerm);
        }
        return slidetextPage;
    }
    


    
    @Override
    public SearchModuleResults getSearchModuleResults(List<IModule> moduleList) {

        SearchModuleResults searchModuleResults = new SearchModuleResults();
        searchModuleResults.setModules(moduleList);

        Map<String, String> moduleFirstSlideImage = new HashMap<>();
        Map<String, Boolean> isModuleConfiguredMap = new HashMap<>();

        for (IModule module : moduleList) {
            if (module.getStartSequence() == null) {
                isModuleConfiguredMap.put(module.getId(), false);
                moduleFirstSlideImage.put(module.getId(), null);
            } else {
                isModuleConfiguredMap.put(module.getId(), true);
                String startSequenceID = module.getStartSequence().getId();
                List<ISlide> slides = sequenceManager.getSequence(startSequenceID) != null
                        ? sequenceManager.getSequence(startSequenceID).getSlides()
                                : null;

                Slide slide = slides != null && !slides.isEmpty() ? (Slide) slides.get(0) : null;
                String firstSlideImageId = null;

                if (slide != null && slide.getFirstImageBlock() != null) {
                    firstSlideImageId = slide.getFirstImageBlock().getImage().getId();
                }
                moduleFirstSlideImage.put(module.getId(), firstSlideImageId);
            }

        }
        searchModuleResults.setModuleImageIdMap(moduleFirstSlideImage);
        searchModuleResults.setModuleAlertMessages(isModuleConfiguredMap);

        return searchModuleResults;
    }

    
    @Override
    public SearchSlideResults getSearchSlideResults(List<ISlide> slideList) {
        SearchSlideResults searchSlideResults = new SearchSlideResults();
        searchSlideResults.setSlides(slideList);
        
        Map<String, String> slideFirstImage = new HashMap<>();
        
        for (ISlide slide : slideList) {
            if (slide != null && slide.getFirstImageBlock() != null) {
                slideFirstImage.put(slide.getId(), slide.getFirstImageBlock().getImage().getId());
            }
        }
        searchSlideResults.setFirstImageOfSlide(slideFirstImage);
        return searchSlideResults;
    }
    
    
    @Override
    public SearchSlideTextBlockResults getSearchSlideTextBlockResults(List<ISlide> slideTextList, String searchTerm) {
        SearchSlideTextBlockResults staffSearchSlideTextBlockResults = new SearchSlideTextBlockResults();
        staffSearchSlideTextBlockResults.setSlidesWithMatchedTextBlock(slideTextList);
        
        Map<String, String> slideTextFirstImageMap = new HashMap<>();
        Map<String, String> slideTextFirstTextBlockMap = new HashMap<>();
        
        for (ISlide slide : slideTextList) {
            if (slide != null) {
                if (slide.getFirstImageBlock() != null) {
                    slideTextFirstImageMap.put(slide.getId(), slide.getFirstImageBlock().getImage().getId());
                }
                if (slide.getFirstMatchedTextBlock(searchTerm) != null) {
                    slideTextFirstTextBlockMap.put(slide.getId(), slide.getFirstMatchedTextBlock(searchTerm).htmlRenderedText());
                }
            }
        }
        staffSearchSlideTextBlockResults.setSlideToFirstImageMap(slideTextFirstImageMap);
        staffSearchSlideTextBlockResults.setSlideToFirstTextBlockMap(slideTextFirstTextBlockMap);
        
        return staffSearchSlideTextBlockResults;
    }
    
    @Override
    public SearchSpaceResults getSearchSpaceResults(List<ISpace> spaceList) {
        SearchSpaceResults staffSearchSpaceResults = new SearchSpaceResults();
        staffSearchSpaceResults.setSpaces(spaceList);
        return staffSearchSpaceResults;
    }
}
