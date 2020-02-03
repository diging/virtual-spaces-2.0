package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;

@Repository
@JaversSpringDataAuditable
public interface SpaceLinkRepository extends PagingAndSortingRepository<SpaceLink, String> {

    List<SpaceLink> findBySourceSpace(ISpace space);
    
    @Modifying
    @Query("delete from SpaceLink where source_space_id in (:sourceSpaceIds)")
    void deleteBySourceSpaceId(@Param("sourceSpaceIds") List<String> id);
    
    @Query("select d from SpaceLink d where d.sourceSpace.id = ?1")
    List<SpaceLink> getLinkedSpaceIds(String id);
}
