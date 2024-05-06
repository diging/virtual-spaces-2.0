package edu.asu.diging.vspace.core.auth.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFacadeTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationFacade authenticationFacade;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    

    @Test
    public void test_getAuthenticatedUser_returnsNull() {
     // Create a mock anonymous authentication token
        Authentication anonymousAuth = mock(Authentication.class);
        
        // Set the mock authentication token as the current authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(anonymousAuth);

        // Mock the getAuthentication() method of the authentication facade to return null
        when(authenticationFacade.getAuthenticatedUser()).thenReturn(null);
       
        // Ensure that the getAuthenticatedUser() method returns null
        assertNull(authenticationFacade.getAuthenticatedUser());
    }

    @Test
    public void test_getAuthenticatedUser_success() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        assertEquals("testUser", authenticationFacade.getAuthenticatedUser());
    }

    @Test
    public void test_getAuthenticatedUser_customAuthentication() {
        Authentication authentication = new TestingAuthenticationToken("testUser", "password", "ROLE_USER");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        assertEquals("testUser", authenticationFacade.getAuthenticatedUser());
    }
}
