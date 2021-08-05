package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

@Repository
@JaversSpringDataAuditable
public interface SpaceRepository extends PagingAndSortingRepository<Space, String> {

    List<Space> findTop5ByOrderByCreationDateDesc();

    List<Space> findAllBySpaceStatus(SpaceStatus spaceStatus);

    List<Space> findAllByImageId(String imageId);

    Page<Space> findDistinctByNameContainingOrDescriptionContaining(Pageable requestedPage, String name,
            String description);
    
    List<Space> findDistinctByNameContainingOrDescriptionContaining(String name, String description);
    
    public Page<Space> findDistinctBySpaceStatusAndNameContainingOrDescriptionContaining(Pageable requestedPage, SpaceStatus spaceStatus, String name,
            String description);
    
}