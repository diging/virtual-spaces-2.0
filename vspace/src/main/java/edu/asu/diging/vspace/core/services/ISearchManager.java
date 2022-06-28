package edu.asu.diging.vspace.core.services;

import org.springframework.data.domain.Page;

import edu.asu.diging.vspace.core.model.ISpace;

public interface ISearchManager {
    
    public Page<ISpace> searchInSpaces(String searchTerm, int page);
    

}
