package edu.asu.diging.vspace.references;

public class CitationStyleDefault implements ICitationStyle{
    
    private static CitationStyleDefault citationStyleDefault = null;
    
    
    String citationStyleName = "defaultZoteroCitation";
    
    private CitationStyleDefault() {}
    
    public static CitationStyleDefault getInstance() {
        if(citationStyleDefault == null) {
            citationStyleDefault = new CitationStyleDefault();
        }
        return citationStyleDefault;
    }
    
}
