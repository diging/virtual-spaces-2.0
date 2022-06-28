package edu.asu.diging.vspace.web.general.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

public class SearchController {
    
    @Autowired
    private IStaffSearchManager staffSearchManager;
    
     protected void  updateModelWithSpaceSearchResult(Model model, Page<ISpace> spacePage, String spacePagenum ) {
         model.addAttribute("spaceCurrentPageNumber", Integer.parseInt(spacePagenum));
         model.addAttribute("spaceTotalPages", spacePage.getTotalPages());
         model.addAttribute("spaceSearchResults", spacePage.getContent());
         model.addAttribute("spaceCount", spacePage.getTotalElements());
     }
     
     /**
      * This method is used to search the search string specified in the input
      * parameter(searchTerm) and return the module corresponding to the page number
      * specified in the input parameter(spacePagenum) whose name or description
      * contains the search string. The modulePagenum is starting from 1.
      * 
      * @param modulePagenum current page number sent as request parameter in the
      *                      URL.
      * @param model         This the object of Model attribute in spring MVC.
      * @param searchTerm    This is the search string which is being searched.
      */
     protected Page<IModule> paginationForModule(String modulePagenum, Model model, String searchTerm) {
         Page<IModule> modulePage = staffSearchManager.searchInModules(searchTerm, Integer.parseInt(modulePagenum));
         model.addAttribute("moduleCurrentPageNumber", Integer.parseInt(modulePagenum));
         model.addAttribute("moduleTotalPages", modulePage.getTotalPages());
         model.addAttribute("moduleCount", modulePage.getTotalElements());
         return modulePage;
     }
     
     
     /**
      * This method is used to search the search string specified in the input
      * parameter(searchTerm) and return the slides corresponding to the page number
      * specified in the input parameter(spacePagenum) whose name or description
      * contains the search string. The slidePagenum is starting from 1.
      * 
      * @param slidePagenum current page number sent as request parameter in the URL.
      * @param model        This the object of Model attribute in spring MVC.
      * @param searchTerm   This is the search string which is being searched.
     * @return 
      */
     protected Page<ISlide> paginationForSlide(String slidePagenum, Model model, String searchTerm) {
         Page<ISlide> slidePage = staffSearchManager.searchInSlides(searchTerm, Integer.parseInt(slidePagenum));
         model.addAttribute("slideCurrentPageNumber", Integer.parseInt(slidePagenum));
         model.addAttribute("slideTotalPages", slidePage.getTotalPages());
         model.addAttribute("slideCount", slidePage.getTotalElements());
         return slidePage;
     }
     
     /**
      * This method is used to search the search string specified in the input
      * parameter(searchTerm) and return the slides corresponding to the page number
      * specified in the input parameter(spacePagenum) whose text block contains the
      * search string. The slideTextPagenum is starting from 1.
      * 
      * @param slideTextPagenum current page number sent as request parameter in the
      *                         URL.
      * @param model            This the object of Model attribute in spring MVC.
      * 
      * @param searchTerm       This is the search string which is being searched.
     * @return 
      */
     protected Page<ISlide> paginationForSlideText(String slideTextPagenum, Model model, String searchTerm) {
         Page<ISlide> slideTextPage = staffSearchManager.searchInSlideTexts(searchTerm,
                 Integer.parseInt(slideTextPagenum));
         model.addAttribute("slideTextCurrentPageNumber", Integer.parseInt(slideTextPagenum));
         model.addAttribute("slideTextTotalPages", slideTextPage.getTotalPages());
         model.addAttribute("slideTextCount", slideTextPage.getTotalElements());
         
         return slideTextPage;
     }
}
