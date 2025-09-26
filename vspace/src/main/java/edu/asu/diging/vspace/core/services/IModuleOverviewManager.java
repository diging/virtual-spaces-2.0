package edu.asu.diging.vspace.core.services;


import edu.asu.diging.vspace.core.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.core.services.impl.model.ModuleOverview;

public interface IModuleOverviewManager {
    
    /**
     * This method returns a ModuleOverview based on a module Id. It fetches all Sequences which belong to the module and 
     * convert this into a SequenceOverview node. This SequenceOverviewNode is added to 
     * ModuleOverview
     * @param id
     * @return ModuleOverview which contains the module and the list of sequences and its slides
     * @throws ModuleNotFoundException 
     */
    public ModuleOverview getModuleOverview(String moduleId) throws ModuleNotFoundException;
    
    /**
     * This method returns a ModuleOverview based on a module Id with localized slide names. It fetches all Sequences which belong to the module and 
     * convert this into a SequenceOverview node with localized slide names. This SequenceOverviewNode is added to 
     * ModuleOverview
     * @param moduleId The ID of the module
     * @param selectedLanguage The language code for the selected language
     * @param defaultLanguageCode The default language code to fall back to
     * @return ModuleOverview which contains the module and the list of sequences and its slides with localized names
     * @throws ModuleNotFoundException 
     */
    public ModuleOverview getModuleOverview(String moduleId, String selectedLanguage, String defaultLanguageCode) throws ModuleNotFoundException;

}
