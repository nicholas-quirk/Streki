/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.streki.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.image.Image;

/**
 *
 * @author Nicholas
 */
public class FileManager {

    // java.vendor      Human-readable VM vendor            The Android Project 
    // user.dir or java.io.tmpdir for Android
    
    public ArrayList<String> getSavedDirListing() {
        ArrayList<String> names = null;
        try {
            if((new java.io.File(System.getProperty("user.home"), "Streki")).exists() == false) {
                new java.io.File(System.getProperty("user.home"), "Streki").mkdirs();
                new java.io.File(System.getProperty("user.home") + File.separator + "Streki", "saved").mkdirs();
            }
            File f = new File(System.getProperty("user.home") + File.separator + "Streki", "saved");
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
            f = new File(System.getProperty("user.home") + File.separator + "Streki" + File.separator + "saved", filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

}
