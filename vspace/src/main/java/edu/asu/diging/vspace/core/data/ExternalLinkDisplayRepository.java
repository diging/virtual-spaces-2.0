package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;

@Repository
@JaversSpringDataAuditable
public interface ExternalLinkDisplayRepository extends PagingAndSortingRepository<ExternalLinkDisplay, String> {

    @Query("SELECT d FROM ExternalLinkDisplay d WHERE d.externalLink.space.id = ?1")
    public List<ExternalLinkDisplay> findExternalLinkDisplaysForSpace(String spaceId);

    public void deleteByExternalLink(IExternalLink link);
}
