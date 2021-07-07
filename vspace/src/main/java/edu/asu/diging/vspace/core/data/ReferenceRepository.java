package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.Reference;

@Repository
@JaversSpringDataAuditable
public interface ReferenceRepository extends PagingAndSortingRepository<Reference, String> {

//    @Query("SELECT d FROM Reference d WHERE d.biblio.id = ?1")
//    public List<Reference> findReferencesForBiblio(String biblioId);
//    
    public List<Reference> findByBiblioId(String biblioId);
//    
}
