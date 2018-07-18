package edu.asu.diging.vspace.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.Space;

@Repository
public interface SpaceRepository extends PagingAndSortingRepository<Space, String> {

}
