package edu.asu.diging.vspace.core.services;
import java.util.HashSet;

import edu.asu.diging.vspace.core.model.IVSpaceElement;

public interface IPublicSearchManager {

    HashSet<IVSpaceElement> getAllSearchedElements(String searchString);
}