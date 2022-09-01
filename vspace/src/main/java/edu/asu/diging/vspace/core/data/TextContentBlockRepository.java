package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.model.impl.TextBlock;

@Repository
@JaversSpringDataAuditable
public interface TextContentBlockRepository extends PagingAndSortingRepository<TextBlock, String> {

    @Query("SELECT DISTINCT c.slide FROM ContentBlock c, TextBlock t WHERE c.id = t.id AND t.text LIKE %?1%")
    public Page<ISlide> findWithNameOrDescription(Pageable requestedPage, String searchText);
    
    @Query("SELECT DISTINCT c.slide FROM ContentBlock c, TextBlock t WHERE c.id = t.id AND t.text LIKE %?1% and c.slide.module.id IN (SELECT DISTINCT module.id from Module module JOIN ModuleLink moduleLink ON module.id = moduleLink.module.id AND module.moduleStatus = ?2)")
    public Page<ISlide> findWithNameOrDescriptionLinkedToSpace(Pageable requestedPage, String searchText, ModuleStatus moduleStatus);
    
}
