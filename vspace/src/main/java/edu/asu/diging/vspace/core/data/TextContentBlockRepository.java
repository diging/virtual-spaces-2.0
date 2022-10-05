package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.TextBlock;

@Repository
@JaversSpringDataAuditable
public interface TextContentBlockRepository extends PagingAndSortingRepository<TextBlock, String> {

    @Query("SELECT DISTINCT c.slide FROM ContentBlock c, TextBlock t WHERE c.id = t.id AND t.text LIKE %?1%")
    public Page<ISlide> findWithNameOrDescription(Pageable requestedPage, String searchText);
    
    @Query("SELECT DISTINCT c.slide FROM ContentBlock c, TextBlock t WHERE c.id = t.id AND t.text LIKE %?1% and c.slide.module.id IN "
            + "(SELECT DISTINCT module.id from Module module JOIN ModuleLink moduleLink ON module.id = moduleLink.module.id JOIN Space space on space.id = moduleLink.space.id WHERE space.spaceStatus = ?2 AND module.moduleStatus = ?3)"
            + "AND c.slide.id IN (Select d.id from Sequence ss join ss.slides d , Module m where m.startSequence.id = ss.id)")
    public Page<ISlide> findWithNameOrDescriptionLinkedToSpace(Pageable requestedPage, String searchText, SpaceStatus spaceStatus, ModuleStatus moduleStatus);
    
}
