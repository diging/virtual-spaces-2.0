package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;

@Repository
@JaversSpringDataAuditable
public interface ContentBlockRepository extends PagingAndSortingRepository<ContentBlock, String> {

    @Query("SELECT max(contentOrder) FROM ContentBlock d WHERE d.slide.id = ?1")
    public Integer findMaxContentOrder(String slideId);
    
    public List<ContentBlock> findBySlide_IdAndContentOrderGreaterThan(String slideId,Integer contentOrder);
    
    @Modifying
    @Transactional
    @Query(value="UPDATE ContentBlock c SET c.contentOrder = ?2 WHERE c.id=?1")
    public int adjustContentOrder(String blockId,Integer contentOrder);
}
