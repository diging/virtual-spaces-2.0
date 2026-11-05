package edu.asu.diging.vspace.core.model.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VSpaceElementTest {

    @Test
    public void test_getElementType_contentBlocks() {
        assertEquals("TextBlock", new TextBlock().getElementType());
        assertEquals("ImageBlock", new ImageBlock().getElementType());
        assertEquals("VideoBlock", new VideoBlock().getElementType());
        assertEquals("BiblioBlock", new BiblioBlock().getElementType());
        assertEquals("SpaceBlock", new SpaceBlock().getElementType());
        assertEquals("ChoiceBlock", new ChoiceBlock().getElementType());
    }

    @Test
    public void test_getElementType_slides() {
        assertEquals("Slide", new Slide().getElementType());
        assertEquals("BranchingPoint", new BranchingPoint().getElementType());
    }
}
