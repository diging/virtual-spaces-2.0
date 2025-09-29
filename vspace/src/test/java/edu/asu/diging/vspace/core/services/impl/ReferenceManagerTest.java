package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ReferenceRepository;
import edu.asu.diging.vspace.core.exception.ReferenceDoesNotExistException;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;

public class ReferenceManagerTest {

    @Mock
    private ReferenceRepository refRepo;

    @InjectMocks
    private ReferenceManager refManagerToTest = new ReferenceManager();

    // setting common used variables and Objects
    private String refId, refIdNotPresent, refIdOther, biblioId;
    private List<IReference> refList = new ArrayList<>();
    private List<IReference> refListOther = new ArrayList<>();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        refId = "REF000000002";
        refIdOther = "REF000000001";
        refIdNotPresent = "REF000000300";
        
        biblioId = "CON000000002";
        
        Reference refObj = new Reference();
        refObj.setId(refId);
        
        Reference refObjOther = new Reference();
        refObjOther.setId(refIdOther);
        
        refList.add(refObj);
        refListOther.add(refObj);
        refListOther.add(refObjOther);
    }

    @Test
    public void test_deleteReferenceById_refIdPresent() throws ReferenceDoesNotExistException {
        Reference ref = new Reference();
        ref.setId(refId);
        Optional<Reference> refObj = Optional.of(ref);
        Mockito.when(refRepo.findById(ref.getId())).thenReturn(refObj);
        refManagerToTest.deleteReferenceById(refId, biblioId);
        Mockito.verify(refRepo).delete(ref);
    }

    @Test
    public void test_deleteReferenceById_refIdNotPresent() throws SlideDoesNotExistException {
        Mockito.when(refRepo.findById(refIdNotPresent)).thenReturn(Optional.empty());
        refManagerToTest.deleteReferenceById(refIdNotPresent, biblioId);
        Mockito.verify(refRepo, Mockito.never()).deleteById(refIdNotPresent);
    }
    
    @Test
    public void test_deleteReferenceId_refIdIsNull() {
        Mockito.when(refRepo.findById(null)).thenReturn(Optional.empty());
        refManagerToTest.deleteReferenceById(null, biblioId);
        Mockito.verify(refRepo, Mockito.never()).deleteById(refIdNotPresent);
    }

}
