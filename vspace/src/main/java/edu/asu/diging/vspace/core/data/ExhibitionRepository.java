package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;

@Repository
@JaversSpringDataAuditable
public interface ExhibitionRepository extends PagingAndSortingRepository<IExhibition, String> {

    public List<IExhibition> findAllByOrderByIdAsc();

}
