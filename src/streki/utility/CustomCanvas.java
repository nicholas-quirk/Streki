/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streki.utility;

import java.io.Serializable;
import javafx.scene.canvas.Canvas;

/**
 *
 * @author Nicholas
 */
public class CustomCanvas extends Canvas implements Serializable {

    private static final long serialVersionUID = 1L;

    
    CustomCanvas(int width, int height) {
        super(width, height);
    }
    
    public CustomCanvas() {
        super();
    }
    
}
