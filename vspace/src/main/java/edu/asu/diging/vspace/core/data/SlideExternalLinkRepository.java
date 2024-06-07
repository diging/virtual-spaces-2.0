package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkSlide;

@Repository
@JaversSpringDataAuditable
public interface SlideExternalLinkRepository extends PagingAndSortingRepository<ExternalLinkSlide, String>{

	@Query("SELECT d FROM ExternalLinkSlide d WHERE d.slide.id = ?1")
    public List<IExternalLinkSlide> findExternalLinkSlides(String slideId);

    public void deleteByExternalLink(IExternalLinkSlide link);
}
