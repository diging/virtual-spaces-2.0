package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;

@Repository
@JaversSpringDataAuditable
public interface SpaceLinkRepository extends PagingAndSortingRepository<SpaceLink, String> {

    List<SpaceLink> findBySourceSpace(ISpace space);
    
    @Modifying
    @Query("delete from SpaceLink where source_space_id = ?1")
    void deleteBySourceSpaceId(String id);
    
    void deleteBySourceSpaceIdIsNull();
    
    @Modifying
    @Query("delete from SpaceLink where target_space_id = ?1")
    void deleteByTargetSpaceId(String id);
    
    @Query("select d from SpaceLink d where d.sourceSpace.id = ?1")
    List<SpaceLink> getLinkedSpaces(String id);
        
    List<SpaceLink> findByTargetSpace(ISpace space);

    List<ISpaceLink> findBySourceSpaceIsNull();
}
