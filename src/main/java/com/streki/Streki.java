package com.streki;

import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import com.streki.ui.MainUI;

/**
 *
 * @author Nicholas Quirk
 */
public class Streki extends Application {

    public static final Boolean debugStreki = false;
    
    private final static Logger LOGGER = Logger.getLogger(Streki.class.getName());
    private static Stage primaryStage;
    
    private MainUI ui;

    @Override
    public void start(Stage primaryStage) {
        if(debugStreki) LOGGER.info("Starting application...");
        Streki.primaryStage = primaryStage;
        this.ui = new MainUI(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(debugStreki) LOGGER.info("Launching main...");
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Streki.primaryStage = primaryStage;
    }

}
