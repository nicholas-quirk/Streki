// Copyright 2015, Nicholas Quirk, All rights reserved.

package com.streki.ui;

import java.util.logging.Logger;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import com.streki.Streki;

/**
 *
 * @author Nicholas Quirk
 * 
 * http://stackoverflow.com/questions/12375276/panning-on-a-canvas-in-javafx
 */
public class CustomStackPane extends StackPane {

    private final static Logger LOGGER = Logger.getLogger(CustomStackPane.class.getName());

    private double pressedX;
    private double pressedY;

    public CustomStackPane() {

        LOGGER.info("Creating new CustomStackPane...");

        setOnMousePressed((event) -> {
            if(event.isSynthesized() == false) {
                // Synthesized mouse events are touch.
                // Let's ignore them for now.
                pressedX = event.getX();
                pressedY = event.getY();
                if(Streki.debugVerboseStreki) LOGGER.info("CustomStackPane -> setOnMousePressed x: " + pressedX + "y: " + pressedY);
                event.consume();
            } else {
                event.consume();
            }
        });

        // For moving the picture
        setOnMouseDragged((event) -> {
            if (event.getButton() == MouseButton.SECONDARY 
                    && event.isSynthesized() == false) {
                setTranslateX(getTranslateX() + event.getX() - pressedX);
                setTranslateY(getTranslateY() + event.getY() - pressedY);
                if(Streki.debugVerboseStreki) LOGGER.info("CustomStackPane -> setOnMouseDragged x: " +
                        (getTranslateX() + event.getX() - pressedX)+"y: " +
                        (getTranslateY() + event.getY() - pressedY));
                event.consume();
            } else {
                event.consume();
            }
        });
    }
    
}
