package edu.asu.diging.vspace.core.services;

import edu.asu.diging.simpleusers.core.exceptions.UserAlreadyExistsException;
import edu.asu.diging.simpleusers.core.model.IUser;

public interface ISetupManager {

    /**
     * This method checks if a user called "admin" exists. If not, we assume this
     * application has not yet been setup.
     * 
     * @return
     */
    boolean isSetup();

    IUser setupAdmin(String password) throws UserAlreadyExistsException;

}