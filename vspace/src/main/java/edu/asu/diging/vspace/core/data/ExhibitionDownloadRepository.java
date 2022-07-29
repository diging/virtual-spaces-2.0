package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;

public interface ExhibitionDownloadRepository extends PagingAndSortingRepository<ExhibitionDownload, String> {

    List<ExhibitionDownload> findAllByOrderByCreationDateDesc();

}
