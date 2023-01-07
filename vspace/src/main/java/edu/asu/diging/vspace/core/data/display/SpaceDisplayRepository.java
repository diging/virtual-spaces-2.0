package edu.asu.diging.vspace.core.data.display;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;
import edu.asu.diging.vspace.core.model.impl.Space;

@Repository
@JaversSpringDataAuditable
public interface SpaceDisplayRepository extends PagingAndSortingRepository<SpaceDisplay, String> {

    @Modifying
    @Query("delete from SpaceDisplay where space_id = ?1")
    void deleteBySpaceId(String id);
    
    
    
    @Query("SELECT space.id FROM  SpaceDisplay where id = ?1")
    Space getSpaceId(String id);

    public List<SpaceDisplay> getBySpace(ISpace space);
}
