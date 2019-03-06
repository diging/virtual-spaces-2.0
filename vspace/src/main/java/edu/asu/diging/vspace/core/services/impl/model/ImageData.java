package edu.asu.diging.vspace.core.services.impl.model;

public class ImageData {

    private int height;
    private int width;

    public ImageData(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public ImageData() {
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
