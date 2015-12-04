/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.streki.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.naming.spi.DirectoryManager;

/**
 *
 * @author Nicholas
 */
public class FileManager {

    public static ArrayList<String> getSavedDirListing() {
        ArrayList<String> names = null;
        try {
            //File f = new File("./saved");
            // File.separator 
            
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

    public static File colorPage(String filename) {
        File f = null;
        try {
            f = new File("./resources/images", filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static File savedFile(String filename) {
        File f = null;
        try {
            f = new File(System.getProperty("user.home") + File.separator + "Streki" + File.separator + "saved", filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

}
