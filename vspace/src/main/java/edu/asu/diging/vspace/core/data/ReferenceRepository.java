package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.Reference;

@Repository
@JaversSpringDataAuditable
public interface ReferenceRepository extends PagingAndSortingRepository<Reference, String> {

    public List<Reference> findByBiblios_Id(String biblioId);
    
}
