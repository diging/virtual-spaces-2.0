package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.SpaceLink;

@Repository
@JaversSpringDataAuditable
public interface SpaceLinkRepository extends PagingAndSortingRepository<SpaceLink, String> {
    
    @Modifying
    @Query("delete from SpaceLink where source_space_id = ?1")
    void deleteBySourceSpaceId(String sourceId);
    
}
