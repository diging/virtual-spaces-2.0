package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.model.impl.VSVideo;

@Repository
@JaversSpringDataAuditable
public interface VideoRepository  extends PagingAndSortingRepository<VSVideo, String> {

    List<VSVideo> findByFilenameLikeOrNameLike(String filename, String name);
    
    List<VSVideo> findByFilenameLike(String filename);
}

