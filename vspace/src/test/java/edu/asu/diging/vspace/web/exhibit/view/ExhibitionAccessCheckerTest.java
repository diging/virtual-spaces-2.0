package edu.asu.diging.vspace.web.exhibit.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.auth.IAuthenticationFacade;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

public class ExhibitionAccessCheckerTest {

    @Mock
    private IAuthenticationFacade authenticationFacade;

    @InjectMocks
    private ExhibitionAccessChecker accessChecker;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_canView_anonymous() {
        when(authenticationFacade.getAuthenticatedUser()).thenReturn(null);

        assertTrue(accessChecker.canView(space(SpaceStatus.PUBLISHED)));
        assertTrue(accessChecker.canView(space(null)));
        assertFalse(accessChecker.canView(space(SpaceStatus.UNPUBLISHED)));
        assertFalse(accessChecker.canView((Space) null));

        assertTrue(accessChecker.canView(module(ModuleStatus.PUBLISHED)));
        assertTrue(accessChecker.canView(module(null)));
        assertFalse(accessChecker.canView(module(ModuleStatus.UNPUBLISHED)));
        assertFalse(accessChecker.canView((Module) null));
    }

    @Test
    public void test_canView_loggedIn() {
        when(authenticationFacade.getAuthenticatedUser()).thenReturn("staff");

        assertTrue(accessChecker.canView(space(SpaceStatus.UNPUBLISHED)));
        assertTrue(accessChecker.canView(module(ModuleStatus.UNPUBLISHED)));
        assertFalse(accessChecker.canView((Space) null));
        assertFalse(accessChecker.canView((Module) null));
    }

    private Space space(SpaceStatus status) {
        Space space = new Space();
        space.setSpaceStatus(status);
        return space;
    }

    private Module module(ModuleStatus status) {
        Module module = new Module();
        module.setModuleStatus(status);
        return module;
    }
}
