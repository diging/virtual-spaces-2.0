package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.impl.Sequence;

@Repository
@JaversSpringDataAuditable
public interface SequenceRepository extends PagingAndSortingRepository<Sequence, String> {

    @Query("SELECT d FROM Sequence d WHERE d.module.id = ?1")
    public List<Sequence> findSequencesForModule(String moduleId);
    
    @Query("SELECT d FROM Sequence d WHERE d.module.id = ?1 and d.id = ?2")
    public Sequence findSequenceForModuleAndSequence(String moduleId, String sequenceId);
    
    @Query(value="SELECT d.startSequence_id FROM Module d where d.startSequence_id IS NOT NULL", nativeQuery = true)
    public List<ISequence> getAllSequencesForModules();
}
