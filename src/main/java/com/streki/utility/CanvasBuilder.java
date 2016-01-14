// Copyright 2015, Nicholas Quirk, All rights reserved.

package com.streki.utility;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import com.streki.Streki;
import com.streki.ui.MainUI;
import com.streki.ui.RenderedCanvas;
import java.util.ArrayList;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.imageio.ImageIO;

/**
 *
 * @author Nicholas
 */
public class CanvasBuilder {

    private final static Logger LOGGER = Logger.getLogger(CanvasBuilder.class.getName());

    private Canvas canvas;
    private Stage stage;
    private int width;
    private int height;
    private double globalAlpha;
    List<Canvas> cs;
    StackPane stackPane;
    private Color fillColor;
    private double xScale;
    private double yScale;
    private String colorPageName;
    private String savedCanvasName;
    private MainUI mainUI;
    List<RenderedCanvas> renderedCaseStack = new ArrayList<RenderedCanvas>();

    private void initDraw(GraphicsContext gc) {
        if (Streki.debugStreki) {
            LOGGER.info("CanvasBuilder -> initDraw");
        }

        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setStroke(Pen.getInstance().getStrokeColor());
        gc.setLineWidth(Pen.getInstance().getLineWidth());

        if (this.fillColor != null) {
            gc.setFill(this.fillColor);
            gc.fillRect(0, 0, this.width, this.height);
        }

        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        gc.setGlobalAlpha(globalAlpha);

        if (this.savedCanvasName != null) {

            try {
                BufferedImage bufferedImage = ImageIO.read((new FileManager()).savedFile(this.savedCanvasName));
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                gc.drawImage(image, 0, 0, canvasWidth, canvasHeight);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (this.colorPageName != null) {
            try {
                Image image = FileManager.colorPage(this.colorPageName);
                gc.drawImage(image, 0, 0, canvasWidth, canvasHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Canvas createCanvas() {
        this.canvas = new Canvas(width, height);
        GraphicsContext graphicsContext = this.canvas.getGraphicsContext2D();

        initDraw(graphicsContext);
        
        this.canvas.setOnTouchPressed((event) -> {
            Pen.getInstance().penMode = PenMode.NONE;
            event.consume();
        });
        
        this.canvas.setOnTouchMoved((event) -> {
            event.consume();
        });
        
        this.canvas.setOnTouchStationary((event) -> {
            event.consume();
        });
        
        this.canvas.setOnTouchReleased((event) -> {
            Pen.getInstance().penMode = PenMode.COLOR;
            event.consume();
        });
        
        this.canvas.setOnScroll((event) -> {

            if(Pen.getInstance().penMode == PenMode.COLOR) {
                double r = (event.getDeltaY() < 0) ? 1.1 : 0.9;
                double zx = canvas.getScaleX() * r;
                double zy = canvas.getScaleY() * r;

                xScale = zx;
                yScale = zy;

                canvas.setScaleX(zx);
                canvas.setScaleY(zy);

                Event.fireEvent(canvas,
                        new MouseEvent(MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0,
                                MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
            }
        });

        this.canvas.setOnMouseExited((event) -> {
            Streki.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
        });

        this.canvas.setOnMouseEntered((event) -> {
            try {
                if (Pen.getInstance().getLineWidth() * xScale > 9) {
                    // When we zoom in we want the marker tip to increase.
                    Canvas canvas = new Canvas((Pen.getInstance().getLineWidth() * xScale), (Pen.getInstance().getLineWidth() * yScale));
                    canvas.getGraphicsContext2D().setFill(((Color) Pen.getInstance().getStrokeColor()).darker());

                    // Create a rectangle and write is to disk.
                    canvas.getGraphicsContext2D().fillRect(0, 0, Pen.getInstance().getLineWidth() * xScale, Pen.getInstance().getLineWidth() * yScale);
                    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                    SnapshotParameters sp = new SnapshotParameters();
                    sp.setFill(Color.TRANSPARENT);
                    canvas.snapshot(sp, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    javax.imageio.ImageIO.write(renderedImage, "png", new File(FileManager.baseUserDirectory, "cursor.png"));
                    Image image = new Image("file:///" + (new File(FileManager.baseUserDirectory, "cursor.png")).getAbsolutePath());

                    Streki.getPrimaryStage().getScene().setCursor(new ImageCursor(image,
                            image.getWidth() / 2,
                            image.getHeight() / 2));
                } else {
                    Streki.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        this.canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                        
            // Save current instance of canvas
            saveCanvas();

            //Pen.getInstance().setHorizontalPos(event.getX());
            //Pen.getInstance().setVerticalPos(event.getY());
            if(Pen.getInstance().penMode == PenMode.COLOR) {
                graphicsContext.setStroke(Pen.getInstance().getStrokeColor());
                graphicsContext.setLineWidth(Pen.getInstance().getLineWidth());

                graphicsContext.beginPath();
                graphicsContext.moveTo(event.getX(), event.getY());
            }
            
            if(Pen.getInstance().penMode == PenMode.PICKER) {
                /**
                WritableImage writableImage = mainUI.getScene().snapshot(null);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", new File("StrekiTemp.png"));
                } catch(Exception e) {
                    
                } 
                PixelReader pr = writableImage.getPixelReader();
                Color pickerColor = pr.getColor((int)event.getX(), (int)event.getY());
               Pen.getInstance().setStrokeColor(pickerColor);
               **/
            }
        });

        this.canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (event) -> {
                if ((event.isSynthesized() == false) && event.getButton() == MouseButton.PRIMARY && Pen.getInstance().penMode == PenMode.COLOR) {
                    graphicsContext.lineTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                    event.consume();
                }
            }
        );
        
        this.canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (event) -> {
                //if(Pen.getInstance().penMode == PenMode.PICKER) {
                //    Pen.getInstance().penMode = PenMode.COLOR;
                //}
                // TODO: Keep file handle.
                Image image = null;
                try {
                    image = FileManager.colorPage(colorPageName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Draws the coloring page on top
                graphicsContext.drawImage(image, 0, 0, graphicsContext.getCanvas().getWidth(),
                        graphicsContext.getCanvas().getHeight());
            });
        
        
        this.canvas.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {

                if((event.getCode() == KeyCode.Z) && event.isControlDown())
                {
                    undoCanvas();
                    //Event.fireEvent(canvas,t);
                    //new MouseEvent(MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0,
                    //        MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                    event.consume();
                }
              });
        
        return this.canvas;
    }
    
    public MainUI getMainUI() {
        return mainUI;
    }

    public CanvasBuilder setMainUI(MainUI mainUI) {
        this.mainUI = mainUI;
        return this;
    }

    public String getSavedCanvasName() {
        return savedCanvasName;
    }

    public CanvasBuilder setSavedCanvasName(String savedCanvasName) {
        this.savedCanvasName = savedCanvasName;
        return this;
    }

    public String getColorPageName() {
        return colorPageName;
    }

    public CanvasBuilder setColorPageName(String colorPageName) {
        this.colorPageName = colorPageName;
        return this;
    }
    
    public Color getFillColor() {
        return fillColor;
    }

    public CanvasBuilder setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public List<Canvas> getCs() {
        return cs;
    }

    public CanvasBuilder setCs(List<Canvas> cs) {
        this.cs = cs;
        return this;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public CanvasBuilder setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
        return this;
    }

    public double getGlobalAlpha() {
        return globalAlpha;
    }

    public CanvasBuilder setGlobalAlpha(double globalAlpha) {
        this.globalAlpha = globalAlpha;
        return this;
    }

    public Stage getStage() {
        return stage;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public CanvasBuilder setCanvas(Canvas canvas) {
        this.canvas = canvas;
        return this;
    }

    public CanvasBuilder setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public CanvasBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public CanvasBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public Canvas getCanvas() {
        return this.canvas;
    }
    
    private void undoCanvas() {
        if(this.renderedCaseStack.size() > 0) {
            RenderedCanvas rc = this.renderedCaseStack.remove(this.renderedCaseStack.size()-1);
            this.canvas.getGraphicsContext2D().drawImage(rc.getWritableImage(), 0, 0, rc.getCanvasSizeWidth(), rc.getCanvasSizeHeight());

            // DUPLICATE CODE!!!
            GraphicsContext graphicsContext = this.canvas.getGraphicsContext2D();
            // TODO: Keep file handle.

            Image image = null;
            try {
                image = FileManager.colorPage(colorPageName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Draws the coloring page on top
            graphicsContext.drawImage(image, 0, 0, graphicsContext.getCanvas().getWidth(),
                    graphicsContext.getCanvas().getHeight());               
        }
    }
    
    private void saveCanvas() {
        try {
            if(cs != null && cs.size() > 0) {
                Canvas c = new Canvas(cs.get(0).getWidth(), cs.get(0).getHeight());

                c.getGraphicsContext2D().setLineCap(StrokeLineCap.ROUND);
                c.getGraphicsContext2D().setLineJoin(StrokeLineJoin.ROUND);
                c.getGraphicsContext2D().setGlobalAlpha(globalAlpha);
                
                double priorScaleX = 1;
                double priorScaleY = 1;
                
                for (Canvas cx : cs) {

                    priorScaleX = cx.getScaleX();
                    priorScaleY = cx.getScaleY();

                    // We may be zoomed in when saving...
                    cx.setScaleX(1);
                    cx.setScaleY(1);

                    Event.fireEvent(cx,
                            new MouseEvent(MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0,
                                    MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));

                    WritableImage wi = new WritableImage((int) canvas.getWidth(), (int) canvas.getWidth());
                    SnapshotParameters sp = new SnapshotParameters();
                    c.getGraphicsContext2D().drawImage(cx.snapshot(sp, wi), 0, 0);
                }

                WritableImage writableImage = new WritableImage((int) c.getWidth(), (int) c.getHeight());
                SnapshotParameters sp = new SnapshotParameters();
                c.snapshot(sp, writableImage);

                RenderedCanvas rc = new RenderedCanvas();
                rc.setWritableImage(writableImage);
                rc.setScaleFactorX(priorScaleX);
                rc.setScaleFactorY(priorScaleY);
                rc.setCanvasSizeWidth((int) c.getWidth());
                rc.setCanvasSizeHeight((int) c.getHeight());

                this.renderedCaseStack.add(rc);

                for (Canvas cx : cs) {
                    cx.setScaleX(priorScaleX);
                    cx.setScaleY(priorScaleY);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
