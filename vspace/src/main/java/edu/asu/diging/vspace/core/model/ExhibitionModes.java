package edu.asu.diging.vspace.core.model;

import java.util.ArrayList;
import java.util.List;

public enum ExhibitionModes {

    ACTIVE(""),
    MAINTENANCE("This exhibition is currently under maintenance. Please check back later."),
    OFFLINE("This exhibition is currently offline. Please check back later.");

    private final String value;

    private ExhibitionModes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static List<ExhibitionModes> getAllModes (){
        List<ExhibitionModes> modesList = new ArrayList<>();
            modesList.add(ExhibitionModes.ACTIVE);
            modesList.add(ExhibitionModes.MAINTENANCE);
            modesList.add(ExhibitionModes.OFFLINE);
        return modesList;
   }
}
