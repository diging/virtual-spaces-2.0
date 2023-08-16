package edu.asu.diging.vspace.core.model.display.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISpaceTextBlock;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceTextBlock;
import edu.asu.diging.vspace.core.model.impl.VSpaceElement;

@Entity
public class SpaceTextBlockDisplay extends VSpaceElement implements ISpaceTextBlockDisplay{
    @Id
    @GeneratedValue(generator = "spacetextblock_display_id_generator")
    @GenericGenerator(name = "spacetextblock_display_id_generator", 
        parameters = @Parameter(name = "prefix", value = "STBD"),
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = SpaceTextBlock.class)
    private ISpaceTextBlock spaceTextBlock;

    private float positionX;
    private float positionY;

    private float height;
    private float width;
    private String textColor;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public ISpaceTextBlock getSpaceTextBlock() {
        return spaceTextBlock;
    }

    @Override
    public void setSpaceTextBlock(ISpaceTextBlock spaceTextBlock) {
        this.spaceTextBlock = spaceTextBlock;
    }

    @Override
    public float getPositionX() {
        return positionX;
    }

    @Override
    public void setPositionX(float positionX) {
        this.positionX=positionX;
    }

    @Override
    public float getPositionY() {
        return positionY;
    }

    @Override
    public void setPositionY(float positionY) {
        this.positionY=positionY;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height=height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width=width;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}
