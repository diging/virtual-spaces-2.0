package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.Sequence;

@Repository
@JaversSpringDataAuditable
public interface SequenceRepository extends PagingAndSortingRepository<Sequence, String> {
    
    @Query("SELECT d FROM Sequence d WHERE d.module.id = ?1")
    public List<Sequence> findSequencesForModule(String moduleId);
    
    @Modifying
    @Query("delete from Sequence where id = ?1")
    void deleteSlideIdFromSequence(String id);
    
    @Modifying
    @Query("delete from Sequence where id in (:linkIds)")
    void deleteSlideIdFromSequence1(@Param("linkIds") List<String> linkId);
    
	
}
