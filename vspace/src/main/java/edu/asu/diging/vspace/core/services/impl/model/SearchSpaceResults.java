package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISpace;

public class SearchSpaceResults {
    
    private List<ISpace> spaces;
    private int currentPage;
    private int totalPages;

    public List<ISpace> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<ISpace> spaces) {
        this.spaces = spaces;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    

}
