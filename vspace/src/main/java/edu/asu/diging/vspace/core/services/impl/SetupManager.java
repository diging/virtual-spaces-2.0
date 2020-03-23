package edu.asu.diging.vspace.core.services.impl;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import edu.asu.diging.simpleusers.core.exceptions.UserAlreadyExistsException;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.Role;
import edu.asu.diging.simpleusers.core.model.impl.User;
import edu.asu.diging.simpleusers.core.service.IUserManager;
import edu.asu.diging.vspace.core.services.ISetupManager;

@Transactional
@Service
@PropertySource({"${appConfigFile:classpath:}/app.properties"})
public class SetupManager implements ISetupManager {
    
    @Autowired
    private Environment env;

    @Autowired 
    private IUserManager userManager;
    
    private String adminUsername;
    
    @PostConstruct
    public void init() {
        adminUsername = env.getProperty("admin_username");
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.services.impl.ISetupManager#isSetup()
     */
    @Override
    public boolean isSetup() {
        IUser admin = userManager.findByUsername(adminUsername);
        if (admin != null) {
            return true;
        }
        return false;
    }
    
    @Override
    public IUser setupAdmin(String password) throws UserAlreadyExistsException {
        IUser user = new User();
        user.setUsername(adminUsername);
        user.setPassword(password);
        userManager.create(user);
        userManager.approveAccount(adminUsername, adminUsername);
        userManager.addRole(adminUsername, adminUsername, Role.ADMIN);
        return user;
    }
}
