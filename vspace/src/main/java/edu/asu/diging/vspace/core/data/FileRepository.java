package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.model.impl.VSFile;

@Repository
@JaversSpringDataAuditable
public interface FileRepository extends PagingAndSortingRepository<VSFile, String> {

    Page<VSFile> findAll(Pageable requestedPage);
    
    List<VSFile> findByFilenameLikeOrNameLike(String filename, String name);

    List<VSFile> findByFilenameLike(String filename);
}

