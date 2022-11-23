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


public abstract class SearchManager implements ISearchManager {

    @Value("${page_size}")
    private int pageSize;

    @Autowired
    private ISequenceManager sequenceManager;

    protected abstract  Page<ISpace> searchSpaces(Pageable requestedPageForSpace ,  String searchTerm);

    protected abstract  Page<IModule> searchModules(Pageable requestedPageForModule ,  String searchTerm);

    protected abstract  Page<ISlide> searchSlides(Pageable requestedPageForSlide ,  String searchTerm);

    protected abstract  Page<ISlide> searchSlideTexts(Pageable requestedPageForSlideText ,  String searchTerm);   

    protected abstract Page<IModule> updateModulePageWithSpaceInfo(Page<IModule> modulePage);

    protected abstract Page<ISlide> updateSlideTextPageWithSpaceInfo(Page<ISlide> slideTextPage);

    protected abstract Page<ISlide> updateSlidePageWithSpaceInfo(Page<ISlide> slidePage);

    
    protected SearchSpaceResults convertToSearchSpaceResults(List<ISpace> spaceList) {
        SearchSpaceResults staffSearchSpaceResults = new SearchSpaceResults();
        staffSearchSpaceResults.setSpaces(spaceList);
        return staffSearchSpaceResults;
    }
    
    protected SearchModuleResults convertToSearchModuleResults(List<IModule> moduleList) {

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

    protected SearchSlideResults convertToSearchSlideResults(List<ISlide> slideList) {
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

    protected SearchSlideTextBlockResults convertToSearchSlideTextBlockResults(List<ISlide> slideTextList, String searchTerm) {
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

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the published spaces corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param spacePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    @Override
    public SearchSpaceResults searchForSpace(String spacePagenum, String searchTerm) {
        Page<ISpace> spacePage = searchSpacesAndPaginate(searchTerm, Integer.parseInt(spacePagenum));
        return convertToSearchSpaceResults(spacePage.getContent());
    }


    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the module corresponding to the page number
     * specified in the input parameter(spacePagenum) whose name or description
     * contains the search string. This also filters modules which are linked to the spaces.
     * 
     * @param modulePagenum current page number sent as request parameter in the
     *                      URL.
     * @param searchTerm    This is the search string which is being searched.
     */
    @Override
    public SearchModuleResults searchForModule(String modulePagenum, String searchTerm) {
        Page<IModule> modulePage = searchModulesAndPaginate(searchTerm, Integer.parseInt(modulePagenum));
        return convertToSearchModuleResults(modulePage.getContent());

    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string. This also filters Slides from modules 
     * which are linked to the spaces.
     * 
     * @param slidePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    @Override
    public SearchSlideResults searchForSlide(String slidePagenum, String searchTerm) {
        Page<ISlide> slidePage = searchSlidesAndPaginate(searchTerm, Integer.parseInt(slidePagenum));
        return convertToSearchSlideResults(slidePage.getContent());
    }


    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the slides corresponding to the page number
     * specified in the input parameter(spacePagenum) whose text block contains the
     * search string. This also filters Slides from modules which are linked to the spaces.
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param searchTerm       This is the search string which is being searched.
     */
    @Override
    public SearchSlideTextBlockResults searchForSlideText(String slideTextPagenum, String searchTerm) {
        Page<ISlide> slideTextPage = searchSlideTextsAndPaginate(searchTerm, Integer.parseInt(slideTextPagenum));
        return  convertToSearchSlideTextBlockResults(slideTextPage.getContent(), searchTerm);

    }

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
    public Page<ISpace> searchSpacesAndPaginate(String searchTerm, int page) {
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForSpace = PageRequest.of(page - 1, pageSize);       
        Page<ISpace> spacePage =  searchSpaces(requestedPageForSpace, searchTerm);

        int totalSpacePage = spacePage.getTotalPages();
        /* if page>total pages,last page is returned */
        if (page > totalSpacePage) {
            totalSpacePage = totalSpacePage == 0 ? 1 : totalSpacePage;
            requestedPageForSpace = PageRequest.of(totalSpacePage - 1, pageSize);   
            spacePage = searchSpaces(requestedPageForSpace,  searchTerm);
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
    public Page<IModule> searchModulesAndPaginate(String searchTerm, int page) {
        /* if page<1, 1st page is returned */
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForModule = PageRequest.of(page - 1, pageSize);
        Page<IModule> modulePage = searchModules(requestedPageForModule, searchTerm);
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
            modulePage = searchModules(requestedPageForModule, searchTerm);
        }
        
        return updateModulePageWithSpaceInfo(modulePage);
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
    public Page<ISlide> searchSlidesAndPaginate(String searchTerm, int page) {
        /* if page<1, 1st page is returned */
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForSlide = PageRequest.of(page - 1, pageSize);
        Page<ISlide> slidePage = searchSlides(requestedPageForSlide, searchTerm);
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
            slidePage = searchSlides(requestedPageForSlide, searchTerm);
        }

        return updateSlidePageWithSpaceInfo(slidePage);      
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
    public Page<ISlide> searchSlideTextsAndPaginate(String searchTerm, int page) {
        /* if page<1, 1st page is returned */
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForSlideText = PageRequest.of(page - 1, pageSize);
        Page<ISlide> slidetextPage = searchSlideTexts(requestedPageForSlideText,
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
            slidetextPage = searchSlideTexts(requestedPageForSlideText,
                    searchTerm);
        }
        return updateSlideTextPageWithSpaceInfo(slidetextPage);
    }




}
