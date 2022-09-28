package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.ExhibitionSpaceOrderMode;
import edu.asu.diging.vspace.core.model.ISpace;

public interface IExhibitionSpaceOrderUtility {

    List<ISpace> sortSpacesAlphabetically(List<ISpace> publishedSpaces);

    List<ISpace> sortSpacesOnCreationDate(List<ISpace> publishedSpaces);

    List<ISpace> sortSpacesByCustomOrder(List<ISpace> publishedSpaces);

    List<ISpace> sortSpaces(List<ISpace> publishedSpaces, ExhibitionSpaceOrderMode mode);

}