package edu.asu.diging.vspace.web.exhibit.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.auth.IAuthenticationFacade;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

/**
 * Decides whether a space or module may be shown on the public exhibition
 * pages. Unpublished content is only visible to logged in users. Spaces and
 * modules without a status (created before statuses existed) are treated as
 * published.
 */
@Component
public class ExhibitionAccessChecker {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    public boolean canView(ISpace space) {
        if (space == null) {
            return false;
        }
        return space.getSpaceStatus() != SpaceStatus.UNPUBLISHED || isLoggedIn();
    }

    public boolean canView(IModule module) {
        if (module == null) {
            return false;
        }
        return module.getModuleStatus() != ModuleStatus.UNPUBLISHED || isLoggedIn();
    }

    public boolean isLoggedIn() {
        return authenticationFacade.getAuthenticatedUser() != null;
    }
}
