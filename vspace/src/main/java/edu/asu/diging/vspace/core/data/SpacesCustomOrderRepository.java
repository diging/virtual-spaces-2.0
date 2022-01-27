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
    @Query("SELECT max(customOrder) FROM SpacesCustomOrder d WHERE d.space.id = ?1")
    public Integer findMaxCustomOrder(String spaceId);
    
    @Query("SELECT max(customOrder) FROM SpacesCustomOrder")
    public Integer findMaxCustomOrder();
    
    public SpacesCustomOrder findByCustomOrderName(String customOrderName);

    public List<SpacesCustomOrder> findBySpace_IdAndCustomOrderGreaterThan(String spaceId,Integer customOrder);
    
    public Optional<SpacesCustomOrder> findBySpace_Id(String spaceId);
}
