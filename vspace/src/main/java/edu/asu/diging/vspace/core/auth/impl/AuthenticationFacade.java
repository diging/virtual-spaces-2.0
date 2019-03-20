package edu.asu.diging.vspace.core.auth.impl;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.auth.IAuthenticationFacade;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.auth.impl.IAuthenticationFacade#getAuthentication()
	 */
	@Override
	public String getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    return authentication.getName();
		}
		return null;
	}
}
