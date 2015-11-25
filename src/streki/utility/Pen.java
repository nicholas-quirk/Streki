package streki.utility;

import java.util.logging.Logger;
import javafx.scene.paint.Paint;
import streki.Streki;

/**
 *
 * @author Nicholas Quirk
 */
public class Pen {
    
     private final static Logger LOGGER = Logger.getLogger(Pen.class.getName());

    private static Pen instance = null;

    private Paint strokeColor;
    private double lineWidth;

    protected Pen() {
        // Exists only to defeat instantiation.
    }

    public static Pen getInstance() {
        
        if(Streki.debug) LOGGER.info("Returning new Pen instance...");
        
        if (instance == null) {
            instance = new Pen();
        }
        
        return instance;
    }

    public Paint getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Paint strokeColor) {
        this.strokeColor = strokeColor;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

}
