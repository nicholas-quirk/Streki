// Copyright 2015, Nicholas Quirk, All rights reserved.

package com.streki.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.image.Image;

/**
 *
 * @author Nicholas Quirk
 */
public class FileManager {

    // java.vendor      Human-readable VM vendor            The Android Project 
    // user.dir or java.io.tmpdir for Android
    
    public static final String baseUserDirectory = System.getProperty("user.home") + File.separator + "Streki";
    
    public ArrayList<String> getSavedDirListing() {
        ArrayList<String> names = null;
        try {
            if((new java.io.File(System.getProperty("user.home"), "Streki")).exists() == false) {
                new java.io.File(System.getProperty("user.home"), "Streki").mkdirs();
                new java.io.File(baseUserDirectory, "saved").mkdirs();
            }
            File f = new File(baseUserDirectory, "saved");
            if(f != null && f.list() != null) {
                names = new ArrayList<String>(Arrays.asList(f.list()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }

    public static Image colorPage(String filename) {
        Image f = null;
        try {
            f = new Image(FileManager.class.getResourceAsStream("/coloring_pages/"+filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public File savedFile(String filename) {
        File f = null;
        try {
            f = new File(baseUserDirectory + File.separator + "saved", filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

}
