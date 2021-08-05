package edu.asu.diging.vspace.web.general.search;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.ModuleWithSpace;
import edu.asu.diging.vspace.core.model.impl.PublicSearch;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IPublicSearchManager;

@Controller
public class PublicSearchModuleController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPublicSearchManager publicSearchManager;
    
    @Autowired
    private IModuleLinkManager moduleLinkManager;

    @RequestMapping(value = "/exhibit/search/module")
    public ResponseEntity<PublicSearch> searchInVspace(
            @RequestParam(value = "modulePagenum", required = false, defaultValue = "1") String modulePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<Module> moduleList = paginationForModule(modulePagenum, searchTerm);
        PublicSearch publicSearch = new PublicSearch();
        publicSearch.setModuleList(moduleList);
        
        Map<String, String> moduleFirstSlideImage = new HashMap<>();
        
        for (Module module : moduleList) {
            
            Slide slide = module.getSlides() != null && !module.getSlides().isEmpty()
                    ? (Slide) module.getSlides().get(0) : null;
            
            String firstSlideImageId = null;
            
            if (slide != null && slide.getFirstImageBlock() != null) {
                firstSlideImageId = slide.getFirstImageBlock().getImage().getId();
            }
            moduleFirstSlideImage.put(module.getId(), firstSlideImageId);
            publicSearch.setModuleFirstSlideFirstImage(moduleFirstSlideImage);
        }
        return new ResponseEntity<PublicSearch>(publicSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) in module table and return the module corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param modulePagenum current page number sent as request parameter in the
     *                      URL.
     * @param searchTerm    This is the search string which is being searched.
     */
    private List<Module> paginationForModule(String modulePagenum, String searchTerm) {
        Page<Module> modulePage = publicSearchManager.searchInModules(searchTerm, Integer.parseInt(modulePagenum));
        List<Module> moduleList = new ArrayList<>();
        
        for(Module module : modulePage.getContent()) {
            ModuleLink moduleLink = moduleLinkManager.findFirstByModule(module);
            if(moduleLink!=null) {
                ModuleWithSpace modWithSpace = new ModuleWithSpace();
                try {
                    BeanUtils.copyProperties(modWithSpace, module);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not create moduleWithSpace.", e);
                }
                modWithSpace.setSpaceId(moduleLink.getSpace().getId());
                moduleList.add(modWithSpace);
            }
        }
        
        return moduleList;
    }

}
