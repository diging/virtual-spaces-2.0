package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.impl.Module;

@Repository
@JaversSpringDataAuditable
public interface ModuleRepository extends PagingAndSortingRepository<Module, String> {

    List<Module> findTop5ByOrderByCreationDateDesc();
    
    List<Module> findAllByOrderByCreationDateDesc();
    
    @Query("SELECT d FROM Module d WHERE d.name like %?1% OR d.description LIKE %?1%")
    List<IVSpaceElement> findInNameOrDescription(String searchText);

}
