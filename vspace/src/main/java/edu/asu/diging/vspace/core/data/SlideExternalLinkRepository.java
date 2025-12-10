package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISlideExternalLink;
import edu.asu.diging.vspace.core.model.impl.SlideExternalLink;

@Repository
@JaversSpringDataAuditable
public interface SlideExternalLinkRepository extends PagingAndSortingRepository<SlideExternalLink, String> {

    public List<ISlideExternalLink> findBySlide_Id(String slideId);
}
