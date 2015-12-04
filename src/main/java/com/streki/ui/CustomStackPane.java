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
            pressedX = event.getX();
            pressedY = event.getY();
            if(Streki.debug) LOGGER.info("CustomStackPane -> setOnMousePressed x: " + pressedX + "y: " + pressedY);
        });

        setOnMouseDragged((event) -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                setTranslateX(getTranslateX() + event.getX() - pressedX);
                setTranslateY(getTranslateY() + event.getY() - pressedY);
                event.consume();
                if(Streki.debug) LOGGER.info("CustomStackPane -> setOnMouseDragged x: " +
                        (getTranslateX() + event.getX() - pressedX)+"y: " +
                        (getTranslateY() + event.getY() - pressedY));
            }
        });
    }
}
