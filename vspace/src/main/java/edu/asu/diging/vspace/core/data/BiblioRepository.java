package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.BiblioBlock;

@Repository
@JaversSpringDataAuditable
public interface BiblioRepository extends PagingAndSortingRepository<BiblioBlock, String> {

//    List<VSImage> findByFilenameLikeOrNameLike(String filename, String name);

//    List<VSImage> findByFilenameLike(String filename);

//    Page<VSImage> findByCategories(Pageable pageable, ImageCategory category);

//    List<IVSImage> findByCategories(ImageCategory category);
    
//    long countByCategories(ImageCategory category);
}
