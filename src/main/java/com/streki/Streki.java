// Copyright 2015, Nicholas Quirk, All rights reserved.

package com.streki;

import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import com.streki.ui.MainUI;

/**
 *
 * @author Nicholas Quirk
 * 
 * Command for Web Start:
 *     "C:\Program Files\Java\jdk1.8.0_45\bin\javapackager.exe" -deploy -outdir "C:\Streki_Web_Start" -outfile Streki -width 400 -height 400 -srcdir "C:\Streki" -srcfiles "Streki.jar" -appclass com.streki.Streki -name "Streki" -title "Streki"
 * 
 */
public class Streki extends Application {

    public static final Boolean debugStreki = false;
    public static final Boolean debugVerboseStreki = false;
    
    private final static Logger LOGGER = Logger.getLogger(Streki.class.getName());
    private static Stage primaryStage;
    
    private MainUI ui;

    @Override
    public void start(Stage primaryStage) {
        if(debugStreki) LOGGER.info("Starting application...");
        Streki.primaryStage = primaryStage;
        this.ui = new MainUI(primaryStage);
        
        /**
        Thread t = new Thread(null, null, "TT", 1000000) {
            @Override
            public void run() {
                System.out.println("HELLO WORLD!");
            }

        };
        t.start();
        **/
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
