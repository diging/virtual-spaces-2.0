package edu.asu.diging.vspace.core.aspects;

import java.time.OffsetDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.auth.IAuthenticationFacade;
import edu.asu.diging.vspace.core.model.IVSpaceElement;

@Aspect
@Component
public class MetadataAspect {
	
	@Autowired
	private IAuthenticationFacade authenticationFacade;

	//@Before("@target(org.springframework.stereotype.Repository)")
	@Before("execution(public * org.springframework.data.repository.Repository+.save(..))")
	private void setObjectMetadata(JoinPoint jp) {
		// we know there is exactly one argument for the save method
		Object arg = jp.getArgs()[0];
		if (arg instanceof IVSpaceElement) {
			OffsetDateTime time = OffsetDateTime.now();
			String user = authenticationFacade.getAuthenticatedUser();
			((IVSpaceElement)arg).setModifiedBy(user);
			((IVSpaceElement)arg).setModificationDate(time);
			
			// if this is a new object, store creation data
			if (((IVSpaceElement) arg).getId() == null) {
				((IVSpaceElement)arg).setCreatedBy(user);
				((IVSpaceElement)arg).setCreationDate(time);
			}
		}
	}
}
