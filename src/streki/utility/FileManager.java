/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streki.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Nicholas
 */
public class FileManager {

    public static ArrayList<String> getSavedDirListing() {
        ArrayList<String> names = null;
        try {
            File f = new File("./saved");
            names = new ArrayList<String>(Arrays.asList(f.list()));
            System.out.println(names.get(0));
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
            f = new File("./saved", filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

}
