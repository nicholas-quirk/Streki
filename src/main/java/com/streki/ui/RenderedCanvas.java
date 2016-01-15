// Copyright 2015, Nicholas Quirk, All rights reserved.
package com.streki.ui;

import javafx.scene.image.WritableImage;

/**
 *
 * @author Nicholas Quirk
 */
public class RenderedCanvas {
    
    WritableImage writableImage;
    double scaleFactorX;
    double scaleFactorY;
    int canvasSizeWidth;
    int canvasSizeHeight;

    public WritableImage getWritableImage() {
        return writableImage;
    }

    public void setWritableImage(WritableImage writableImage) {
        this.writableImage = writableImage;
    }

    public double getScaleFactorX() {
        return scaleFactorX;
    }

    public void setScaleFactorX(double scaleFactorX) {
        this.scaleFactorX = scaleFactorX;
    }

    public double getScaleFactorY() {
        return scaleFactorY;
    }

    public void setScaleFactorY(double scaleFactorY) {
        this.scaleFactorY = scaleFactorY;
    }

    public int getCanvasSizeWidth() {
        return canvasSizeWidth;
    }

    public void setCanvasSizeWidth(int canvasSizeWidth) {
        this.canvasSizeWidth = canvasSizeWidth;
    }

    public int getCanvasSizeHeight() {
        return canvasSizeHeight;
    }

    public void setCanvasSizeHeight(int canvasSizeHeight) {
        this.canvasSizeHeight = canvasSizeHeight;
    }

}
