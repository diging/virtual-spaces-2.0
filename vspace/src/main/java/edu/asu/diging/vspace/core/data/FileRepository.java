package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.VSFile;

@Repository
@JaversSpringDataAuditable
public interface FileRepository extends PagingAndSortingRepository<VSFile, String> {

    List<VSFile> findByFilenameLikeOrNameLike(String filename, String name);

    List<VSFile> findByFilenameLike(String filename);
}

