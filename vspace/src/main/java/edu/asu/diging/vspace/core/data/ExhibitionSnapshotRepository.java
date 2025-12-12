package edu.asu.diging.vspace.core.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;

public interface ExhibitionSnapshotRepository extends PagingAndSortingRepository<ExhibitionSnapshot, String> {

    Page<ExhibitionSnapshot> findAllByOrderByCreationDateDesc(Pageable requestedPage);

    ExhibitionSnapshot findByFolderName(String exhibitionFolderName);

}
