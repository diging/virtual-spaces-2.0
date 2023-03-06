package edu.asu.diging.vspace.core.data.display;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;

@Repository
@JaversSpringDataAuditable
public interface SpaceDisplayRepository extends PagingAndSortingRepository<SpaceDisplay, String> {

    @Modifying
    @Query("delete from SpaceDisplay where space_id = ?1")
    void deleteBySpaceId(String id);
    
}
