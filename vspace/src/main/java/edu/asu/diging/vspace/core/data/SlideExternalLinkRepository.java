package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkSlide;

@Repository
@JaversSpringDataAuditable
public interface SlideExternalLinkRepository extends PagingAndSortingRepository<ExternalLinkSlide, String> {

    public List<IExternalLinkSlide> findBySlide_Id(String slideId);
}
