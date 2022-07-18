package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import edu.asu.diging.vspace.core.data.ReferenceRepository;
import edu.asu.diging.vspace.core.exception.ReferenceDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.Reference;

public class ReferenceManagerTest {

    @Mock
    private ReferenceRepository refRepo;

    @InjectMocks
    private ReferenceManager refManagerToTest = new ReferenceManager();

    // setting common used variables and Objects
    private List<Reference> refList;
    private IReference ref1, ref2;
    private Reference refForm;

    private String REF_ID1, REF_ID2, REF_ID_NOT_PRESENT;
    private final String REF_TITLE1 = "TestTitle1";
    private final String REF_AUTHOR1 = "TestAuthor1";
    private final String REF_YEAR1 = "2000";
    private final String REF_JOURNAL1 = "TestJournal1";
    private final String REF_URL1 = "TestUrl1.com";
    private final String REF_VOLUME1 = "TestVolume1";
    private final String REF_ISSUE1 = "TestIssue1";
    private final String REF_PAGES1 = "TestPages1";
    private final String REF_EDITORS1 = "TestEditors1";
    private final String REF_TYPE1 = "TestType1";
    private final String REF_NOTE1 = "TestNote1";

    private final String REF_TITLE2 = "TestTitle2";
    private final String REF_AUTHOR2 = "TestAuthor2";
    private final String REF_YEAR2 = "2000";
    private final String REF_JOURNAL2 = "TestJournal2";
    private final String REF_URL2 = "TestUrl2.com";
    private final String REF_VOLUME2 = "TestVolume2";
    private final String REF_ISSUE2 = "TestIssue2";
    private final String REF_PAGES2 = "TestPages2";
    private final String REF_EDITORS2 = "TestEditors2";
    private final String REF_TYPE2 = "TestType2";
    private final String REF_NOTE2 = "TestNote2";

    private final String REF_NEW_TITLE = "NewTestTitle1";
    private final String REF_NEW_AUTHOR = "NewTestAuthor1";
    private final String REF_NEW_YEAR = "2001";
    private final String REF_NEW_JOURNAL = "NewTestJournal1";
    private final String REF_NEW_URL = "NewTestUrl1.com";
    private final String REF_NEW_VOLUME = "NewTestVolume1";
    private final String REF_NEW_ISSUE = "NewTestIssue1";
    private final String REF_NEW_PAGES = "NewTestPages1";
    private final String REF_NEW_EDITORS = "NewTestEditors1";
    private final String REF_NEW_TYPE = "NewTestType1";
    private final String REF_NEW_NOTE = "NewTestNote1";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        REF_ID1 = "REF000000002";
        REF_ID2 = "REF000000001";
        REF_ID_NOT_PRESENT = "REF000000300";

        Reference refObj = new Reference();
        refObj.setId(REF_ID1);

        Reference refObjOther = new Reference();
        refObjOther.setId(REF_ID2);

        ref1 = new Reference();
        ref1.setId(REF_ID1);
        ref1.setTitle(REF_TITLE1);
        ref1.setAuthor(REF_AUTHOR1);
        ref1.setYear(REF_YEAR1);
        ref1.setJournal(REF_JOURNAL1);
        ref1.setUrl(REF_URL1);
        ref1.setVolume(REF_VOLUME1);
        ref1.setIssue(REF_ISSUE1);
        ref1.setPages(REF_PAGES1);
        ref1.setEditors(REF_EDITORS1);
        ref1.setType(REF_TYPE1);
        ref1.setNote(REF_NOTE1);

        ref2 = new Reference();
        ref2.setId(REF_ID2);
        ref2.setTitle(REF_TITLE2);
        ref2.setAuthor(REF_AUTHOR2);
        ref2.setYear(REF_YEAR2);
        ref2.setJournal(REF_JOURNAL2);
        ref2.setUrl(REF_URL2);
        ref2.setVolume(REF_VOLUME2);
        ref2.setIssue(REF_ISSUE2);
        ref2.setPages(REF_PAGES2);
        ref2.setEditors(REF_EDITORS2);
        ref2.setType(REF_TYPE2);
        ref2.setNote(REF_NOTE2);

        refList = new ArrayList<>();
        refList.add((Reference) ref2);
        refList.add((Reference) ref1);

        refForm = new Reference();
        refForm.setTitle(REF_NEW_TITLE);
        refForm.setAuthor(REF_NEW_AUTHOR);
        refForm.setYear(REF_NEW_YEAR);
        refForm.setJournal(REF_NEW_JOURNAL);
        refForm.setUrl(REF_NEW_URL);
        refForm.setVolume(REF_NEW_VOLUME);
        refForm.setIssue(REF_NEW_ISSUE);
        refForm.setPages(REF_NEW_PAGES);
        refForm.setEditors(REF_NEW_EDITORS);
        refForm.setType(REF_NEW_TYPE);
        refForm.setNote(REF_NEW_NOTE);

        ReflectionTestUtils.setField(refManagerToTest, "pageSize", 10);
    }

    @Test
    public void test_deleteReferenceById_refIdPresent() throws ReferenceDoesNotExistException {
        Reference ref = new Reference();
        ref.setId(REF_ID1);
        Optional<Reference> refObj = Optional.of(ref);
        Mockito.when(refRepo.findById(ref.getId())).thenReturn(refObj);
        refManagerToTest.deleteReferenceById(REF_ID1);
        Mockito.verify(refRepo).deleteById(REF_ID1);
    }

    @Test
    public void test_deleteReferenceById_refIdNotPresent() throws ReferenceDoesNotExistException {
        Mockito.when(refRepo.findById(REF_ID_NOT_PRESENT)).thenReturn(Optional.empty());
        refManagerToTest.deleteReferenceById(REF_ID_NOT_PRESENT);
        Mockito.verify(refRepo).deleteById(REF_ID_NOT_PRESENT);
    }

    @Test
    public void test_deleteReferenceId_refIdIsNull() throws ReferenceDoesNotExistException {
        Mockito.when(refRepo.findById(null)).thenReturn(Optional.empty());
        refManagerToTest.deleteReferenceById(null);
        Mockito.verify(refRepo, Mockito.never()).deleteById(REF_ID_NOT_PRESENT);
    }

    @Test
    public void test_getTotalPages_success() {
        when(refRepo.count()).thenReturn(12L);
        assertEquals(2, refManagerToTest.getTotalPages());
    }

    @Test
    public void test_getTotalPages_whenZeroReferences() {
        when(refRepo.count()).thenReturn(0L);
        assertEquals(0, refManagerToTest.getTotalPages());
    }

    @Test
    public void test_getReferences_success() {
        Pageable sortByRequestedField = PageRequest.of(0, 10,
                Sort.by(SortByField.CREATION_DATE.getValue()).descending());
        when(refRepo.count()).thenReturn(1L);
        when(refRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<Reference>(refList));
        List<IReference> requestedReferences = refManagerToTest.getReferences(1);
        assertEquals(2, requestedReferences.size());
        assertEquals(REF_ID2, requestedReferences.get(0).getId());
        verify(refRepo).findAll(sortByRequestedField);
    }

    @Test
    public void test_getReferences_negativePage() {
        Pageable sortByRequestedField = PageRequest.of(0, 10,
                Sort.by(SortByField.CREATION_DATE.getValue()).descending());
        when(refRepo.count()).thenReturn(1L);
        when(refRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<Reference>(refList));
        List<IReference> requestedReferences = refManagerToTest.getReferences(-2);
        assertEquals(REF_ID2, requestedReferences.get(0).getId());
        verify(refRepo).findAll(sortByRequestedField);
    }

    @Test
    public void test_getReferences_pageGreaterThanTotalPages() {
        ReflectionTestUtils.setField(refManagerToTest, "pageSize", 1);
        Pageable sortByRequestedField = PageRequest.of(4, 1,
                Sort.by(SortByField.CREATION_DATE.getValue()).descending());
        when(refRepo.count()).thenReturn(5L);
        when(refRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<Reference>(refList));
        List<IReference> requestedReferences = refManagerToTest.getReferences(7);
        assertEquals(REF_ID2, requestedReferences.get(0).getId());
        verify(refRepo).findAll(sortByRequestedField);
    }

    @Test
    public void test_getReferences_sorted_success() {
        Pageable sortByRequestedField = PageRequest.of(0, 10,
                Sort.by(SortByField.REFERENCE_TITLE.getValue()).descending());
        when(refRepo.count()).thenReturn(1L);
        when(refRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<Reference>(refList));
        List<IReference> requestedReferences = refManagerToTest.getReferences(1, SortByField.REFERENCE_TITLE.getValue(),
                Sort.Direction.DESC.toString());
        assertEquals(true, checkSortByTitleDesc(requestedReferences));
        assertEquals(2, requestedReferences.size());
        assertEquals(REF_ID2, requestedReferences.get(0).getId());
    }

    @Test
    public void test_getReferences_sorted_negativePage() {
        Pageable sortByRequestedField = PageRequest.of(0, 10,
                Sort.by(SortByField.REFERENCE_TITLE.getValue()).descending());
        when(refRepo.count()).thenReturn(1L);
        when(refRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<Reference>(refList));
        List<IReference> requestedReferences = refManagerToTest.getReferences(-2,
                SortByField.REFERENCE_TITLE.getValue(), Sort.Direction.DESC.toString());
        assertEquals(true, checkSortByTitleDesc(requestedReferences));
        assertEquals(REF_ID2, requestedReferences.get(0).getId());
    }

    @Test
    public void test_getReferences_sorted_pageGreaterThanTotalPages() {
        ReflectionTestUtils.setField(refManagerToTest, "pageSize", 1);
        Pageable sortByRequestedField = PageRequest.of(4, 1,
                Sort.by(SortByField.REFERENCE_TITLE.getValue()).descending());
        when(refRepo.count()).thenReturn(5L);
        when(refRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<Reference>(refList));
        List<IReference> requestedReferences = refManagerToTest.getReferences(7, SortByField.REFERENCE_TITLE.getValue(),
                Sort.Direction.DESC.toString());
        assertEquals(true, checkSortByTitleDesc(requestedReferences));
        assertEquals(REF_ID2, requestedReferences.get(0).getId());
    }

    @Test
    public void test_getReferences_sorted_noResult() {
        Pageable sortByRequestedField = PageRequest.of(0, 10,
                Sort.by(SortByField.CREATION_DATE.getValue()).descending());
        when(refRepo.count()).thenReturn(5L);
        when(refRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<Reference>(new ArrayList<>()));
        List<IReference> requestedReferences = refManagerToTest.getReferences(1, SortByField.CREATION_DATE.getValue(),
                Sort.Direction.DESC.toString());
        assertEquals(true, requestedReferences.isEmpty());
    }

    @Test
    public void test_getTotalReferenceCount_success() {
        when(refRepo.count()).thenReturn(5L);
        assertEquals(5L, refManagerToTest.getTotalReferenceCount());
    }

    @Test
    public void test_getTotalReferenceCount_whenZeroReferences() {
        when(refRepo.count()).thenReturn(0L);
        assertEquals(0L, refManagerToTest.getTotalReferenceCount());
    }

    @Test
    public void test_validatePageNumber_success() {
        when(refRepo.count()).thenReturn(40L);
        assertEquals(2, refManagerToTest.validatePageNumber(2));
    }

    @Test
    public void test_validatePageNumber_whenPageIsNegative() {
        when(refRepo.count()).thenReturn(1L);
        assertEquals(1, refManagerToTest.validatePageNumber(-1));
    }

    @Test
    public void test_validatePageNumber_pageGreaterThanTotalPages() {
        ReflectionTestUtils.setField(refManagerToTest, "pageSize", 1);
        when(refRepo.count()).thenReturn(5L);
        assertEquals(5, refManagerToTest.validatePageNumber(20));
    }

    @Test
    public void test_editReference_success() throws ReferenceDoesNotExistException {
        Mockito.when(refRepo.findById(REF_ID1)).thenReturn(Optional.of(refList.get(1)));
        refManagerToTest.updateReference(refForm);
        verify(refRepo).save(refForm);
    }

    @Test
    public void test_editReference_whenNoReferenceExists() {
        Mockito.when(refRepo.findById(REF_ID1)).thenReturn(Optional.empty());
        refManagerToTest.updateReference(refForm);
        Mockito.verify(refRepo).save(refForm);
    }

    @Test
    public void test_getReferenceById_success() throws ReferenceDoesNotExistException {
        Mockito.when(refRepo.findById(REF_ID1)).thenReturn(Optional.of(refList.get(0)));
        assertEquals(refList.get(0).getId(), refManagerToTest.getReferenceById(REF_ID1).getId());
    }

    @Test
    public void test_getReferenceById_whenNoReferenceExists() {
        Mockito.when(refRepo.findById(REF_ID1)).thenReturn(Optional.empty());
        assertNull(refManagerToTest.getReferenceById(REF_ID1));
    }

    @Test
    public void test_getReference_success() {
        Mockito.when(refRepo.findById(REF_ID1)).thenReturn(Optional.of(refList.get(0)));
        assertEquals(refList.get(0).getId(), refManagerToTest.getReference(REF_ID1).getId());

    }
    
    @Test
    public void test_getReferencesForBiblio_success() {
        List<IReference> refList = new ArrayList<>();
        Mockito.when(refRepo.findByBiblios_Id("BiblioId")).thenReturn(refList);
        assertEquals(refList, refManagerToTest.getReferencesForBiblio("BiblioId"));

    }

    @Test
    public void test_saveReference_success() {
        Reference ref = new Reference();
        ref.setId(REF_ID1);
        ref.setTitle(REF_NEW_TITLE);
        ref.setAuthor(REF_AUTHOR1);
        Optional<Reference> refObj = Optional.of(ref);
        IBiblioBlock biblio = new BiblioBlock();
        biblio.setBiblioTitle(REF_AUTHOR1);
        biblio.setId(REF_ID1);
        biblio.setReferences(refList);
        List<BiblioBlock> biblioList = new ArrayList<>();
        biblioList.add((BiblioBlock)biblio);
        
        Mockito.when(refRepo.save((Reference) ref)).thenReturn(ref);
        Mockito.when(refRepo.findById(ref.getId())).thenReturn(refObj);
        IReference returnResult = refManagerToTest.saveReference(biblio, ref);
        assertEquals(returnResult.getTitle(), REF_NEW_TITLE);
        assertEquals(returnResult.getAuthor(), REF_AUTHOR1);
        assertEquals(returnResult.getId(), REF_ID1);
        verify(refRepo).save((Reference) ref);
    }

    private Boolean checkSortByTitleDesc(List<IReference> requestedReferences) {
        for (int i = 0; i < requestedReferences.size() - 1; ++i) {
            if (requestedReferences.get(i).getTitle().compareTo(requestedReferences.get(i + 1).getTitle()) < 0) {
                return false;
            }
        }
        return true;
    }

}
