package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;

@Repository
@JaversSpringDataAuditable
public interface SpaceLinkRepository extends PagingAndSortingRepository<SpaceLink, String> {

   
    List<SpaceLink> findBySourceSpace(ISpace space);
}
