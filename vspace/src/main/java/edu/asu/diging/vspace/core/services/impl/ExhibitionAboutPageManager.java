package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.asu.diging.vspace.core.data.ExhibitionAboutPageRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager;
/**
 * 
 * @author Avirup Biswas
 *
 */
@Transactional
@Service
public class ExhibitionAboutPageManager implements IExhibitionAboutPageManager{

    @Autowired
    private ExhibitionAboutPageRepository repo;
    
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager#findAll()
     */
    @Override
    public List<ExhibitionAboutPage> findAll() {
        Iterable<ExhibitionAboutPage> aboutpages = repo.findAll();
        List<ExhibitionAboutPage> results = new ArrayList<>();
        aboutpages.forEach(e -> results.add(e));
        return results;
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager#store()
     */
    @Override
    public ExhibitionAboutPage store(ExhibitionAboutPage exhibitionAboutPage) {
        return repo.save(exhibitionAboutPage);
    }
    
    @Override
    public ExhibitionAboutPage getExhibitionAboutPage() {
        List<ExhibitionAboutPage> aboutPageList = findAll();
        return aboutPageList != null && !aboutPageList.isEmpty() ? aboutPageList.get(0):new ExhibitionAboutPage();
    }
    
}
