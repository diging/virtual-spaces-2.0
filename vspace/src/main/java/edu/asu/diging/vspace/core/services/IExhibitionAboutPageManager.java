package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
/**
 * IExhibitionAboutPageManager allows to store and manage ExhibitionAboutPage.
 * @author Avirup Biswas
 *
 */
public interface IExhibitionAboutPageManager {
    List<ExhibitionAboutPage> findAll();
    ExhibitionAboutPage store(ExhibitionAboutPage exhibitionAboutPage);
}
