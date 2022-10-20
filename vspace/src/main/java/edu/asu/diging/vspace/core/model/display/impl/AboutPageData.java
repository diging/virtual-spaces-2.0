package edu.asu.diging.vspace.core.model.display.impl;

import javax.persistence.Lob;

public class AboutPageData {

    private String id;

    private String title;

    @Lob private String aboutPageText;
   
}