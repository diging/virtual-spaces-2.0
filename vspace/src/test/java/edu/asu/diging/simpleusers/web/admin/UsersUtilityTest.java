package edu.asu.diging.simpleusers.web.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.junit.Assert;

public class UsersUtilityTest {
    
    @Mock
    private UsersUtility usersUtility;
    
    HashSet<SimpleGrantedAuthority> roles;
    
    @Before
    public void setUp() {
        roles = new HashSet<SimpleGrantedAuthority>();
        roles.add(new SimpleGrantedAuthority("ROLE_STAFF"));
    }
    
    @Test
    public void test_checkUserRoleExists_roleExists() {
        Assert.assertEquals(usersUtility.checkUserRoleExists(roles, "ROLE_STAFF"), true);
    }
    
    @Test
    public void test_checkUserRoleExists_RoleNotExists() {
        Assert.assertEquals(usersUtility.checkUserRoleExists(roles, "ROLE_USER"), false);
    }

}
