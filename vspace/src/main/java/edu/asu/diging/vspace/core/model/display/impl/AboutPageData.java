package edu.asu.diging.vspace.core.model.display.impl;

import javax.persistence.Lob;

public class AboutPageData {

    private String id;

    private String titles;

    @Lob private String aboutPageTexts;
   
}