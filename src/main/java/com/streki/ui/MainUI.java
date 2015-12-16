// Copyright 2015, Nicholas Quirk, All rights reserved.

package com.streki.ui;

import com.streki.utility.CanvasBuilder;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import com.streki.Streki;
import com.streki.utility.FileManager;
import com.streki.utility.Pen;
import com.streki.utility.PenMode;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;


/**
 *
 * @author Nicholas Quirk
 */
public class MainUI {
    
    private final static Logger LOGGER = Logger.getLogger(MainUI.class.getName());
    
    private static int WIDTH = 800;
    private static int HEIGHT = 800;
    
    // GUI members.
    MenuBar menuBar;
    Menu fileMenu;
    Menu helpMenu;
    BorderPane border;
    ScrollPane scrollPane;
    CustomStackPane stackPane;
    Canvas canvas;
    ColorPicker colorPicker;
    Slider penSizeSlider;
    VBox quickColors;
            
    // State members
    List<Canvas> cs = new ArrayList<Canvas>();
    Color strokeColor;
    Pen pen;
    Stage stage;
    RenderedImage ri;

    public ColorPicker getColorPicker() {
        return this.colorPicker;
    }
    
    public MainUI(final Stage stage) {
        
        this.stage = stage;
        
        if(Streki.debugStreki) LOGGER.info("Initializing Pen...");
        pen = Pen.getInstance();
        pen.setLineWidth(2);
        pen.setStrokeColor(Color.RED);
        
        if(Streki.debugStreki) LOGGER.info("Initializing menu options...");
        createFileMenu();
        createFileMenuChoices(stage);
        
        if(Streki.debugStreki) LOGGER.info("Initializing help options...");
        createHelpMenu();
        createHelpMenuChoices();
        
        if(Streki.debugStreki) LOGGER.info("Initializing BorderPane...");
        border = new BorderPane();
        
        if(Streki.debugStreki) LOGGER.info("Initializing HBox...");
        HBox hBoxMenu = new HBox();
        
        if(Streki.debugStreki) LOGGER.info("Adding top menu...");
        hBoxMenu.getChildren().add(menuBar);
        border.setTop(hBoxMenu);

        if(Streki.debugStreki) LOGGER.info("Initializing ScrollPane...");
        scrollPane = new ScrollPane();
        
        if(Streki.debugStreki) LOGGER.info("Initializing StackPane...");
        stackPane = new CustomStackPane();
        
        if(Streki.debugStreki) LOGGER.info("Adding stack pane to scroll pane...");
        scrollPane.setContent(stackPane);
        
        if(Streki.debugStreki) LOGGER.info("Adding scroll pane to center border...");
        scrollPane.setPadding(new Insets(8, 8, 8, 8));
        border.setCenter(scrollPane);
        
        if(Streki.debugStreki) LOGGER.info("Initializing color picker...");
        createColorPicker();
      
        if(Streki.debugStreki) LOGGER.info("Initializing pen size slider...");
        createPenSlider();

        if(Streki.debugStreki) LOGGER.info("Initializing default color palette...");
        createDefaultColorPalette();

        if(Streki.debugStreki) LOGGER.info("Adding quick colors to scroll pane...");
        ScrollPane quickColorsScrollPane = new ScrollPane(quickColors);
        quickColorsScrollPane.setMaxHeight(600);
        
        if(Streki.debugStreki) LOGGER.info("Adding controls to right panel...");
        VBox controls = new VBox();
        controls.setPadding(new Insets(8, 8, 32, 8));
        Label colorPickerLabel = new Label("Custom Color");
        Label penSizeLabel = new Label("Crayon Size");
        
        Button colorDropperButton = new Button();
        colorDropperButton.setText("Color Selector");
        colorDropperButton.setOnAction(new EventHandler() {
            public void handle(Event event) {
                //Pen.getInstance().penMode = PenMode.PICKER;
                Pen.getInstance().setStrokeColor(Color.TRANSPARENT);
            }
        });
        
        controls.getChildren().addAll(
            colorPickerLabel, 
            colorPicker,
            new Label(" "),
            
            colorDropperButton, 
            new Label(" "),
            
            penSizeLabel, 
            penSizeSlider,
            new Label(" "),
            
            quickColorsScrollPane);
        border.setRight(controls);

        Scene scene = new Scene(border);
        
        stage.setTitle("Streki");
        stage.setScene(scene);
        
        // Set the window to the entire size of the screen.
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        
        stage.getIcons().add(new Image(Streki.class.getResourceAsStream("/icon.png")));
        stage.show();
    }
    
    private void createDefaultColorPalette() {
        
        // http://websafecolors.info/
        quickColors = new VBox();
        quickColors.setSpacing(3);
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 102, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 102, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 102, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 102, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 153, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 153, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 153, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 153, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 204, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 204, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 204, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 204, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 255, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 255, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 255, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(102, 255, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 102, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 102, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 102, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 102, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 153, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 153, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 153, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 153, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 204, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 204, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 204, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 204, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 255, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 255, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 255, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(153, 255, 255, 1), colorPicker));
 
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 102, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 102, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 102, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 102, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 153, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 153, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 153, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 153, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 204, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 204, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 204, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 204, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 255, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 255, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 255, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(204, 255, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 102, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 102, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 102, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 102, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 153, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 153, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 153, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 153, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 204, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 204, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 204, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 204, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 255, 102, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 255, 153, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 255, 204, 1), colorPicker));
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 255, 255, 1), colorPicker));
        
        quickColors.getChildren().add(createQuickColorCanvas(Color.rgb(255, 255, 255, 0), colorPicker));
    }
    
    private void createPenSlider() {
        penSizeSlider = new Slider();
        penSizeSlider.setMin(0);
        penSizeSlider.setMax(10);
        penSizeSlider.setValue(1);
        penSizeSlider.setShowTickLabels(true);
        penSizeSlider.setShowTickMarks(true);
        penSizeSlider.setMajorTickUnit(2);
        penSizeSlider.setMinorTickCount(1);
        penSizeSlider.setBlockIncrement(2);
        
        penSizeSlider.valueProperty().addListener((cl) -> {
            if(Streki.debugStreki) LOGGER.info("Changed Pen size to "+Pen.getInstance().getLineWidth());
            Pen.getInstance().setLineWidth(penSizeSlider.getValue());
        });
    }
    
    private void createColorPicker() {
        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.CORAL);
        
        if(Streki.debugStreki) LOGGER.info("Setting color picker onAction...");
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event event) {
                strokeColor = colorPicker.getValue();
                Pen.getInstance().setStrokeColor(colorPicker.getValue());
                if(Streki.debugStreki) LOGGER.info("Changed Pen color to "+Pen.getInstance().getStrokeColor());
            }
        });
    }
    
    private void createFileMenu() {
        if(Streki.debugStreki) LOGGER.info("Creating file menu...");
        fileMenu = new Menu("File");
        
        menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        menuBar.getMenus().add(fileMenu);
    }
    
    private void createHelpMenu() {
        if(Streki.debugStreki) LOGGER.info("Creating about menu...");
        helpMenu = new Menu("Help");
        
        menuBar.getMenus().add(helpMenu);
    }
    
    private void createHelpMenuChoices() {
        MenuItem aboutMenuItem = new MenuItem();
        aboutMenuItem.setText("About");
        aboutMenuItem.setOnAction((ae) -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("Author: Nicholas Quirk");
            alert.setContentText("Streki is the product of curiousity and a labor of love."
                    + "\n\nResearch has shown that coloring reduces stress in adults."
                    + "\n\nThis is open source software: [enter github url]"
                    + "\n\nIf you would like donated money to a passionate programmer: [paypal url]"
                    + "\n\nIf you would like to feature a coloring page or just contact me: nmquirk@gmail.com");
            alert.showAndWait();
        });
        
        MenuItem howToMenuItem = new MenuItem();
        howToMenuItem.setText("How To");
        howToMenuItem.setOnAction((ae) -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("How To");
            alert.setHeaderText("Using Streki");
            alert.setContentText("Streki is best used with a table PC and stylus, although you can use it with a desktop or laptop and mouse just fine."
                    + "\n\nLoad coloring pictures from the File -> New option."
                    + "\n\nClick on a crayon to select a color or use the color picker to find something else."
                    + "\n\nClick and drag to start coloring."
                    + "\n\nYou can save and load colorings for later."
                    + "\n\nYou can export your artwork, print it and put it on the fridge."
            );
            alert.showAndWait();
        });
        
        helpMenu.getItems().add(aboutMenuItem);
        helpMenu.getItems().add(howToMenuItem);
    }
    
    private void createFileMenuChoices(final Stage stage) {
        if(Streki.debugStreki) LOGGER.info("Creating file menu choices...");
        
        Menu newMenu = new Menu();
        newMenu.setText("New");
        
        MenuItem newMenuItem1 = new MenuItem();
        newMenuItem1.setText("Face");
        newMenuItem1.setOnAction((ae) -> createColoringPage(stage, "coloring-adult-mask.gif", null));
        newMenu.getItems().add(newMenuItem1);
        
        MenuItem menuItemSave = new MenuItem("_Save");
        menuItemSave.setMnemonicParsing(true);
        menuItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        menuItemSave.setOnAction((ae) -> saveRenderedImage());
        
        Menu loadMenu = new Menu();
        loadMenu.setText("Load");
        
        try {
            ArrayList<String> dirListing = (new FileManager()).getSavedDirListing();
            if(dirListing != null) {
                for(String savedName : dirListing) {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(new Long(savedName.substring(0, savedName.indexOf("."))));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd @ hh : mm : ss a");
                    String displayName = sdf.format(c.getTime());
                    MenuItem menuItemLoad = new MenuItem(displayName);
                    menuItemLoad.setOnAction((ae) -> createColoringPage(this.stage, "coloring-adult-mask.gif", savedName));
                    loadMenu.getItems().add(menuItemLoad);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MenuItem menuItemExport = new MenuItem();
        menuItemExport.setText("Export PNG");
        menuItemExport.setOnAction((ae) -> {
                
                FileChooser fileChooser = new FileChooser();
                 
                //Set extension filter
                FileChooser.ExtensionFilter extFilter = 
                        new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
                fileChooser.getExtensionFilters().add(extFilter);
               
                //Show save file dialog
                File file = fileChooser.showSaveDialog(stage);
                
                if(file != null){
                    try {
                        
                        Canvas c = new Canvas(cs.get(0).getWidth(), cs.get(0).getHeight());
                        for(Canvas cx : cs) {
                            WritableImage wi = new WritableImage((int)canvas.getWidth(), (int)canvas.getWidth());
                            
                            SnapshotParameters sp = new SnapshotParameters();
                            sp.setFill(Color.TRANSPARENT);
                            c.getGraphicsContext2D().drawImage(cx.snapshot(sp, wi), 0, 0);
                        }
                        
                        WritableImage writableImage = new WritableImage((int)c.getWidth(), (int)c.getHeight());
                        SnapshotParameters sp = new SnapshotParameters();
                        sp.setFill(Color.TRANSPARENT);
                        c.snapshot(sp, writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                        ImageIO.write(renderedImage, "png", file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        MenuItem menuItemExit = new MenuItem();
        menuItemExit.setText("Exit");
        menuItemExit.setOnAction((ae) -> System.exit(0));
        
        fileMenu.getItems().addAll(newMenu, menuItemSave, loadMenu, menuItemExport, menuItemExit);
    }
    
    public void saveRenderedImage() {
        try {
            Canvas c = new Canvas(cs.get(0).getWidth(), cs.get(0).getHeight());

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
                sp.setFill(Color.TRANSPARENT);

                c.getGraphicsContext2D().drawImage(cx.snapshot(sp, wi), 0, 0);
            }

            WritableImage writableImage = new WritableImage((int) c.getWidth(), (int) c.getHeight());
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);
            c.snapshot(sp, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(renderedImage, "png", (new FileManager()).savedFile(this.canvas.getId() + ".png"));
            
            for (Canvas cx : cs) {
                cx.setScaleX(priorScaleX);
                cx.setScaleY(priorScaleY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createColoringPage(Stage stage, String colorPageName, String savedCanvasName) {
        this.canvas = (new CanvasBuilder())
                .setStage(stage)
                .setWidth(WIDTH)
                .setHeight(HEIGHT)
                .setGlobalAlpha(1)
                .setFillColor(Color.WHITE)
                .setCs(cs)
                .setStackPane(stackPane)
                .setSavedCanvasName(savedCanvasName)
                .setColorPageName(colorPageName)
                .setMainUI(this)
                .createCanvas();
        Event.fireEvent(this.canvas, 
                new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 
                        MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        
        if(Streki.debugStreki) LOGGER.info("Adding canvas to list...");
        if(savedCanvasName == null) {
            this.canvas.setId(Calendar.getInstance().getTimeInMillis()+"");
        } else {
            // Strip .PNG extension...
            this.canvas.setId(savedCanvasName.substring(0, savedCanvasName.lastIndexOf(".")));
        }
        this.cs = new ArrayList<Canvas>();
        this.cs.add(this.canvas);
        
        if(Streki.debugStreki) LOGGER.info("Adding cavnas to stack pane...");
        this.stackPane.getChildren().clear();
        this.stackPane.getChildren().add(this.canvas);
    }
    
    public Canvas createQuickColorCanvas(Color color, ColorPicker colorPicker) {
        Canvas c = new Canvas(128, 32);
        GraphicsContext gc = c.getGraphicsContext2D();

        gc.setFill(color);

        gc.setStroke(Color.TRANSPARENT);
        gc.setLineWidth(2);
        
        gc.moveTo(0, 16);
        gc.lineTo(20, 0);
        gc.lineTo(20, 32);
        gc.lineTo(0, 16);
        gc.stroke();
        gc.fill();
        
        gc.moveTo(20, 0);
        gc.lineTo(128, 0);
        gc.lineTo(128, 32);
        gc.lineTo(20, 32);
        gc.lineTo(20, 0);
        gc.stroke();
        gc.fill();
        
        gc.setStroke(Color.BLACK);
        gc.moveTo(25, 0);
        gc.lineTo(25, 32);
        gc.stroke();
        
        c.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Pen.getInstance().setStrokeColor(color);
                        colorPicker.setValue(color);
                    }
                });
        
       return c;
    }   
}
