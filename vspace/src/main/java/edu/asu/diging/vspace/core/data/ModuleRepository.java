package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;

@Repository
@JaversSpringDataAuditable
public interface ModuleRepository extends PagingAndSortingRepository<Module, String> {

    List<Module> findTop5ByOrderByCreationDateDesc();

    public Page<Module> findDistinctByNameContainingOrDescriptionContaining(Pageable requestedPage, String name,
            String description);
    
    @Query("SELECT DISTINCT m, s.id FROM Module m, ModuleLink lm, Space s WHERE s.spaceStatus = 0 .id = t.id AND m.name LIKE %?1% OR m.description LIKE %?2%")
    public Page<Slide> findWithNameOrDescription(Pageable requestedPage, String searchText);

    List<Module> findAllByOrderByCreationDateDesc();
}
