package edu.asu.diging.vspace.core.data;

import java.util.List;
import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.SpacesCustomOrder;

@Repository
@JaversSpringDataAuditable
public interface SpacesCustomOrderRepository extends PagingAndSortingRepository<SpacesCustomOrder, String> {

}
