package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.SpacesCustomOrder;

@Repository
@JaversSpringDataAuditable
public interface SpacesCustomOrderRepository extends PagingAndSortingRepository<SpacesCustomOrder, String> {
    @Query("SELECT max(customOrder) FROM SpacesCustomOrder d WHERE d.space.id = ?1")
    public Integer findMaxCustomOrder(String spaceId);

    public List<SpacesCustomOrder> findBySpace_IdAndCustomOrderGreaterThan(String spaceId,Integer customOrder);
}
