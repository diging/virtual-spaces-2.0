package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.Sequence;

@Repository
@JaversSpringDataAuditable
public interface SequenceRepository extends PagingAndSortingRepository<Sequence, String> {

    @Query("SELECT d FROM Sequence d WHERE d.module.id = ?1")
    public List<Sequence> findSequencesForModule(String moduleId);

}
