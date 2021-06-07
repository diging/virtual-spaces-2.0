package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.vspace.core.data.BiblioRepository;
import edu.asu.diging.vspace.core.exception.BibliographyDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;

public class BiblioServiceTest {

    @Mock
    private BiblioRepository biblioRepo;
    
    @InjectMocks
    private BiblioService serviceToTest;

    private BiblioBlock biblioForm;
    private List<BiblioBlock> biblios;
    private IBiblioBlock biblio1;
    private IBiblioBlock biblio2;
    
    private final String BIBLIO_ID1 = "id1";
    private final String BIBLIO_TITLE1 = "TestTitle1";
    private final String BIBLIO_AUTHOR1 = "TestAuthor1";
    private final String BIBLIO_YEAR1 = "2000";
    private final String BIBLIO_JOURNAL1 = "TestJournal1";
    private final String BIBLIO_URL1 = "TestUrl1.com";
    private final String BIBLIO_VOLUME1 = "TestVolume1";
    private final String BIBLIO_ISSUE1 = "TestIssue1";
    private final String BIBLIO_PAGES1 = "TestPages1";
    private final String BIBLIO_EDITORS1 = "TestEditors1";
    private final String BIBLIO_TYPE1 = "TestType1";
    private final String BIBLIO_NOTE1 = "TestNote1";
    
    private final String BIBLIO_ID2 = "id2";
    private final String BIBLIO_TITLE2 = "TestTitle2";
    private final String BIBLIO_AUTHOR2 = "TestAuthor2";
    private final String BIBLIO_YEAR2 = "2000";
    private final String BIBLIO_JOURNAL2 = "TestJournal2";
    private final String BIBLIO_URL2 = "TestUrl2.com";
    private final String BIBLIO_VOLUME2 = "TestVolume2";
    private final String BIBLIO_ISSUE2 = "TestIssue2";
    private final String BIBLIO_PAGES2 = "TestPages2";
    private final String BIBLIO_EDITORS2 = "TestEditors2";
    private final String BIBLIO_TYPE2 = "TestType2";
    private final String BIBLIO_NOTE2 = "TestNote2";
    
    private final String BIBLIO_NEW_TITLE = "NewTestTitle1";
    private final String BIBLIO_NEW_AUTHOR = "NewTestAuthor1";
    private final String BIBLIO_NEW_YEAR = "2001";
    private final String BIBLIO_NEW_JOURNAL = "NewTestJournal1";
    private final String BIBLIO_NEW_URL = "NewTestUrl1.com";
    private final String BIBLIO_NEW_VOLUME = "NewTestVolume1";
    private final String BIBLIO_NEW_ISSUE = "NewTestIssue1";
    private final String BIBLIO_NEW_PAGES = "NewTestPages1";
    private final String BIBLIO_NEW_EDITORS = "NewTestEditors1";
    private final String BIBLIO_NEW_TYPE = "NewTestType1";
    private final String BIBLIO_NEW_NOTE = "NewTestNote1";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        biblio1 = new BiblioBlock();
        biblio1.setId(BIBLIO_ID1);
        biblio1.setTitle(BIBLIO_TITLE1);
        biblio1.setAuthor(BIBLIO_AUTHOR1);
        biblio1.setYear(BIBLIO_YEAR1);
        biblio1.setJournal(BIBLIO_JOURNAL1);
        biblio1.setUrl(BIBLIO_URL1);
        biblio1.setVolume(BIBLIO_VOLUME1);
        biblio1.setIssue(BIBLIO_ISSUE1);
        biblio1.setPages(BIBLIO_PAGES1);
        biblio1.setEditors(BIBLIO_EDITORS1);
        biblio1.setType(BIBLIO_TYPE1);
        biblio1.setNote(BIBLIO_NOTE1);
        
        biblio2 = new BiblioBlock();
        biblio2.setId(BIBLIO_ID2);
        biblio2.setTitle(BIBLIO_TITLE2);
        biblio2.setAuthor(BIBLIO_AUTHOR2);
        biblio2.setYear(BIBLIO_YEAR2);
        biblio2.setJournal(BIBLIO_JOURNAL2);
        biblio2.setUrl(BIBLIO_URL2);
        biblio2.setVolume(BIBLIO_VOLUME2);
        biblio2.setIssue(BIBLIO_ISSUE2);
        biblio2.setPages(BIBLIO_PAGES2);
        biblio2.setEditors(BIBLIO_EDITORS2);
        biblio2.setType(BIBLIO_TYPE2);
        biblio2.setNote(BIBLIO_NOTE2);

        biblios = new ArrayList<>();
        biblios.add((BiblioBlock)biblio2);
        biblios.add((BiblioBlock)biblio1);
        
        biblioForm = new BiblioBlock();
        biblioForm.setTitle(BIBLIO_NEW_TITLE);
        biblioForm.setAuthor(BIBLIO_NEW_AUTHOR);
        biblioForm.setYear(BIBLIO_NEW_YEAR);
        biblioForm.setJournal(BIBLIO_NEW_JOURNAL);
        biblioForm.setUrl(BIBLIO_NEW_URL);
        biblioForm.setVolume(BIBLIO_NEW_VOLUME);
        biblioForm.setIssue(BIBLIO_NEW_ISSUE);
        biblioForm.setPages(BIBLIO_NEW_PAGES);
        biblioForm.setEditors(BIBLIO_NEW_EDITORS);
        biblioForm.setType(BIBLIO_NEW_TYPE);
        biblioForm.setNote(BIBLIO_NEW_NOTE);

        ReflectionTestUtils.setField(serviceToTest, "pageSize", 10);
    }

    @Test
    public void test_getTotalPages_success() {
        when(biblioRepo.count()).thenReturn(12L);
        assertEquals(2, serviceToTest.getTotalPages());
    }
    
    @Test
    public void test_getTotalPages_whenZeroImages() {
        when(biblioRepo.count()).thenReturn(0L);
        assertEquals(0, serviceToTest.getTotalPages());
    }
   
    @Test
    public void test_getBibliographies_success() { 
        Pageable sortByRequestedField = PageRequest.of(0, 10, Sort.by(SortByField.CREATION_DATE.getValue()).descending());
        when(biblioRepo.count()).thenReturn(1L);
        when(biblioRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<BiblioBlock>(biblios));
        List<IBiblioBlock> requestedBibliographies = serviceToTest.getBibliographies(1);
        assertEquals(2, requestedBibliographies.size());
        assertEquals(BIBLIO_ID2, requestedBibliographies.get(0).getId());
        verify(biblioRepo).findAll(sortByRequestedField);
    }
  
    @Test
    public void test_getBibliographies_negativePage() { 
        Pageable sortByRequestedField = PageRequest.of(0, 10, Sort.by(SortByField.CREATION_DATE.getValue()).descending());
        when(biblioRepo.count()).thenReturn(1L);
        when(biblioRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<BiblioBlock>(biblios));
        List<IBiblioBlock> requestedBibliographies = serviceToTest.getBibliographies(-2);
        assertEquals(BIBLIO_ID2, requestedBibliographies.get(0).getId());
        verify(biblioRepo).findAll(sortByRequestedField);
    }
    
    @Test
    public void test_getImages_pageGreaterThanTotalPages() { 
        ReflectionTestUtils.setField(serviceToTest, "pageSize", 1);
        Pageable sortByRequestedField = PageRequest.of(4, 1, Sort.by(SortByField.CREATION_DATE.getValue()).descending());
        when(biblioRepo.count()).thenReturn(5L);
        when(biblioRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<BiblioBlock>(biblios));
        List<IBiblioBlock> requestedBibliographies = serviceToTest.getBibliographies(7);
        assertEquals(BIBLIO_ID2, requestedBibliographies.get(0).getId());
        verify(biblioRepo).findAll(sortByRequestedField);
    }
    
    @Test
    public void test_getBibliographies_sorted_success() {
        Pageable sortByRequestedField = PageRequest.of(0, 10, Sort.by(SortByField.BIBLIO_TITLE.getValue()).descending());
        when(biblioRepo.count()).thenReturn(1L);
        when(biblioRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<BiblioBlock>(biblios));
        List<IBiblioBlock> requestedBibliographies = serviceToTest.getBibliographies(1, SortByField.BIBLIO_TITLE.getValue(), Sort.Direction.DESC.toString());
        assertEquals(true, checkSortByTitleDesc(requestedBibliographies));
        assertEquals(2, requestedBibliographies.size());
        assertEquals(BIBLIO_ID2, requestedBibliographies.get(0).getId());
    }
    
    @Test
    public void test_getBibliographies_sorted_negativePage() { 
        Pageable sortByRequestedField = PageRequest.of(0, 10, Sort.by(SortByField.BIBLIO_TITLE.getValue()).descending());
        when(biblioRepo.count()).thenReturn(1L);
        when(biblioRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<BiblioBlock>(biblios));
        List<IBiblioBlock> requestedBibliographies = serviceToTest.getBibliographies(-2, SortByField.BIBLIO_TITLE.getValue(), Sort.Direction.DESC.toString());
        assertEquals(true, checkSortByTitleDesc(requestedBibliographies));
        assertEquals(BIBLIO_ID2, requestedBibliographies.get(0).getId());
    }
    
    @Test
    public void test_getBibliographies_sorted_pageGreaterThanTotalPages() { 
        ReflectionTestUtils.setField(serviceToTest, "pageSize", 1);
        Pageable sortByRequestedField = PageRequest.of(4, 1, Sort.by(SortByField.BIBLIO_TITLE.getValue()).descending());
        when(biblioRepo.count()).thenReturn(5L);
        when(biblioRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<BiblioBlock>(biblios));
        List<IBiblioBlock> requestedBibliographies = serviceToTest.getBibliographies(7, SortByField.BIBLIO_TITLE.getValue(), Sort.Direction.DESC.toString());
        assertEquals(true, checkSortByTitleDesc(requestedBibliographies));
        assertEquals(BIBLIO_ID2, requestedBibliographies.get(0).getId());
    }
    
    @Test
    public void test_getBibliographies_sorted_noResult() { 
        ReflectionTestUtils.setField(serviceToTest, "pageSize", 1);
        Pageable sortByRequestedField = PageRequest.of(4, 1, Sort.by(SortByField.CREATION_DATE.getValue()).descending());
        when(biblioRepo.count()).thenReturn(5L);
        when(biblioRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<BiblioBlock>(new ArrayList<>()));
        List<IBiblioBlock> requestedBibliographies = serviceToTest.getBibliographies(1, SortByField.CREATION_DATE.getValue(), Sort.Direction.DESC.toString());
        assertEquals(true, requestedBibliographies.isEmpty());
    }
    
    @Test
    public void test_getTotalBiblioCount_success() {
        when(biblioRepo.count()).thenReturn(5L);
        assertEquals(5L, serviceToTest.getTotalBiblioCount());
    }
    
    @Test
    public void test_getTotalBiblioCount_whenZeroBiblios() {
        when(biblioRepo.count()).thenReturn(0L);
        assertEquals(0L, serviceToTest.getTotalBiblioCount());
    }
    
    @Test
    public void test_validatePageNumber_success() {
        when(biblioRepo.count()).thenReturn(40L);
        assertEquals(2, serviceToTest.validatePageNumber(2));
    }
    
    @Test
    public void test_validatePageNumber_whenPageIsNegative() {
        when(biblioRepo.count()).thenReturn(1L);
        assertEquals(1, serviceToTest.validatePageNumber(-1));
    }
    
    @Test
    public void test_validatePageNumber_pageGreaterThanTotalPages() {
        ReflectionTestUtils.setField(serviceToTest, "pageSize", 1);
        when(biblioRepo.count()).thenReturn(5L);
        assertEquals(5, serviceToTest.validatePageNumber(20));
    }
    
    @Test
    public void test_editBibliography_success() throws BibliographyDoesNotExistException {
        Mockito.when(biblioRepo.findById(BIBLIO_ID1)).thenReturn(Optional.of(biblios.get(1)));
        serviceToTest.editBibliography(BIBLIO_ID1, biblioForm);
        Assert.assertEquals(biblioForm.getTitle(), biblio1.getTitle());
        Assert.assertEquals(biblioForm.getAuthor(), biblio1.getAuthor());
        Assert.assertEquals(biblioForm.getAuthor(), biblio1.getAuthor());
        Assert.assertEquals(biblioForm.getYear(), biblio1.getYear());
        Assert.assertEquals(biblioForm.getJournal(), biblio1.getJournal());
        Assert.assertEquals(biblioForm.getUrl(), biblio1.getUrl());
        Assert.assertEquals(biblioForm.getVolume(), biblio1.getVolume());
        Assert.assertEquals(biblioForm.getIssue(), biblio1.getIssue());
        Assert.assertEquals(biblioForm.getPages(), biblio1.getPages());
        Assert.assertEquals(biblioForm.getEditors(), biblio1.getEditors());
        Assert.assertEquals(biblioForm.getType(), biblio1.getType());
        Assert.assertEquals(biblioForm.getNote(), biblio1.getNote());
    }
    
    @Test(expected = BibliographyDoesNotExistException.class)
    public void test_editBibliography_whenNoBibliographyExists() throws BibliographyDoesNotExistException{
        Mockito.when(biblioRepo.findById(BIBLIO_ID1)).thenReturn(Optional.empty());
        serviceToTest.editBibliography(BIBLIO_ID1, biblioForm);
    }
    
    @Test
    public void test_getBiblioById_success() throws BibliographyDoesNotExistException {
        Mockito.when(biblioRepo.findById(BIBLIO_ID1)).thenReturn(Optional.of(biblios.get(0)));
        assertEquals(biblios.get(0).getId(), serviceToTest.getBiblioById(BIBLIO_ID1).getId());
    }
    
    @Test(expected = BibliographyDoesNotExistException.class)
    public void test_getBiblioById_whenNoImageExist() throws BibliographyDoesNotExistException {
        Mockito.when(biblioRepo.findById(BIBLIO_ID1)).thenReturn(Optional.empty());
        serviceToTest.getBiblioById(BIBLIO_ID1);
    }

    private Boolean checkSortByTitleDesc(List<IBiblioBlock> requestedBibliographies) {
        for(int i=0; i<requestedBibliographies.size() - 1; ++i) {
            if (requestedBibliographies.get(i).getTitle().compareTo(requestedBibliographies.get(i+1).getTitle()) < 0) {
                return false;
            }
        }
        return true;
    }
}
