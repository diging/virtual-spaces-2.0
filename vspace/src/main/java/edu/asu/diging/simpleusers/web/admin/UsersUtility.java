package edu.asu.diging.simpleusers.web.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Utility class that contains methods for ListUsersController
 */
public class UsersUtility {
    
   /**
    * This method checks if the User has a specific Role assigned to them.
    * It takes as parameter a set of User Roles of the type SimpleGrantedAuthority
    * and it validates if the User Role exists with the second parameter which is
    * a String of the type of Role we need to validate.
    * @param  userRoles  a set of SimpleGrantedAuthority containing details of the roles assigned to a User
    * @param  role the role that needs to be checked against userRoles
    * @return      Boolean: True if User is assigned the role, else False.
    */
    public static Boolean checkUserRoleExists(Set<SimpleGrantedAuthority> userRoles, String role) {
        List<String> roleAsString = new ArrayList<String>();
        for (SimpleGrantedAuthority roleKey : userRoles) {
            if(roleKey.toString().equals(role)) return true;  
        }
        return false;
    }
}
