package edu.asu.diging.vspace.web.staff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.ImageCategory;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.services.IBiblioService;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ListBibliographiesController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IBiblioService biblioService;

    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping("/staff/biblios/list")
    public String listSpacesWithoutNum(Model model,
            @RequestParam(value = "sort", required = false) String sortedBy,
            @RequestParam(value = "order", required = false) String order) {
        return String.format("redirect:/staff/biblios/list/1?sort=%s&order=%s",sortedBy,order);
    }

    @RequestMapping("/staff/biblios/list/{page}")
    public String listSpaces(@PathVariable(required = false) String page,
            @RequestParam(value = "sort", required = false) String sortedBy,
            @RequestParam(value = "order", required = false) String order, Model model, RedirectAttributes attributes) {
        int pageNo;
        page = StringUtils.isEmpty(page) ? "1" : page;
        try {
            pageNo = biblioService.validatePageNumber(Integer.parseInt(page));
        } catch (NumberFormatException numberFormatException){
            pageNo = 1;
        }
        List<IBiblioBlock> biblios;
        model.addAttribute("totalPages", biblioService.getTotalPages());
        model.addAttribute("currentPageNumber", pageNo);
        model.addAttribute("totalBiblioCount", biblioService.getTotalBiblioCount());
        biblios = biblioService.getBibliographies(pageNo, sortedBy, order);
        Map<String, List<ISpace>> biblioToSpaces = new HashMap<String, List<ISpace>>();
        for(IBiblioBlock biblio : biblios) {
            List<ISpace> spaces = spaceManager.getSpacesWithImageId(biblio.getId());
            if(spaces!=null && spaces.size()>0) {
                biblioToSpaces.put(biblio.getId(), spaces);
            }
        }
        model.addAttribute("biblioToSpaces",biblioToSpaces);
        model.addAttribute("biblios", biblios);
        model.addAttribute("sortProperty",
                (sortedBy==null || sortedBy.equals("")) ? SortByField.CREATION_DATE.getValue():sortedBy);
        model.addAttribute("order",
                (order==null || order.equals("")) ? Sort.Direction.DESC.toString().toLowerCase():order);
        return "staff/biblios/bibliolist";
    }
}
