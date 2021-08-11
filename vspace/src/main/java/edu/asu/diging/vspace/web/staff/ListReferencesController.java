package edu.asu.diging.vspace.web.staff;

import java.util.List;

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

import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.services.IReferenceManager;
import edu.asu.diging.vspace.references.ReferenceMetadataRegistry;

@Controller
public class ListReferencesController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IReferenceManager referenceManager;

    @RequestMapping("/staff/references/list")
    public String listSpacesWithoutNum(Model model,
            @RequestParam(value = "sort", required = false) String sortedBy,
            @RequestParam(value = "order", required = false) String order) {
        return String.format("redirect:/staff/references/list/1?sort=%s&order=%s",sortedBy,order);
    }

    @RequestMapping("/staff/references/list/{page}")
    public String listSpaces(@PathVariable(required = false) String page,
            @RequestParam(value = "sort", required = false) String sortedBy,
            @RequestParam(value = "order", required = false) String order, Model model, RedirectAttributes attributes) {
        int pageNo;
        page = StringUtils.isEmpty(page) ? "1" : page;
        try {
            pageNo = referenceManager.validatePageNumber(Integer.parseInt(page));
        } catch (NumberFormatException numberFormatException){
            pageNo = 1;
        }
        List<IReference> references;
        references = referenceManager.getReferences(pageNo, sortedBy, order);
        
        model.addAttribute("totalPages", referenceManager.getTotalPages());
        model.addAttribute("currentPageNumber", pageNo);
        model.addAttribute("totalReferenceCount", referenceManager.getTotalReferenceCount());
        model.addAttribute("references", references);
        model.addAttribute("sortProperty",
                (sortedBy==null || sortedBy.equals("")) ? SortByField.CREATION_DATE.getValue():sortedBy);
        model.addAttribute("order",
                (order==null || order.equals("")) ? Sort.Direction.DESC.toString().toLowerCase():order);
        return "staff/references/referencelist";
    }
}
