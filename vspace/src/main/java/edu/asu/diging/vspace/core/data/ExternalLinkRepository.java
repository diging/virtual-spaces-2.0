package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;

@Repository
@JaversSpringDataAuditable
public interface ExternalLinkRepository extends PagingAndSortingRepository<ExternalLink, String> {

    public void deleteBySpace(ISpace space);

}
