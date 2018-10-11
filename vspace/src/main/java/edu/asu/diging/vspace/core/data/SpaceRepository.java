package edu.asu.diging.vspace.core.data;

import java.util.List;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import edu.asu.diging.vspace.core.model.impl.Space;

@Repository
@JaversSpringDataAuditable
public interface SpaceRepository extends PagingAndSortingRepository<Space, String> {

  List<Space> findTop5ByOrderByCreationDateDesc();
}
