package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.SpacesCustomOrder;

@Repository
@JaversSpringDataAuditable
public interface SpacesCustomOrderRepository extends PagingAndSortingRepository<SpacesCustomOrder, String> {
    
    List<SpacesCustomOrder> findByCustomOrderName(String name);

}
