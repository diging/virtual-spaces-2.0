package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.display.ISlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SlideExternalLinkDisplay;

@Repository
@JaversSpringDataAuditable
public interface SlideExternalLinkDisplayRepository extends PagingAndSortingRepository<SlideExternalLinkDisplay, String> {
    
    @Query("SELECT d FROM SlideExternalLinkDisplay d WHERE d.externalLink.slide.id = ?1")
    public List<ISlideExternalLinkDisplay> findSlideExternalLinkDisplaysForSlide(String slideId);

    public void deleteByExternalLink(IExternalLinkSlide link);

}
