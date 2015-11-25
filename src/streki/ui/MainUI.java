package streki.ui;

import streki.utility.CanvasBuilder;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import streki.Streki;
import streki.utility.Pen;
//import org.controlsfx.control.StatusBar;


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
    BorderPane border;
    //StatusBar statusBar;
    ScrollPane scrollPane;
    CustomStackPane stackPane;
    Button testButton;
    Canvas canvas;
    List<Canvas> cs = new ArrayList<Canvas>();
    Color strokeColor;
    Pen pen;
    
    public MainUI(final Stage stage) {
        
        if(Streki.debug) LOGGER.info("Initializing Pen...");
        pen = Pen.getInstance();
        pen.setLineWidth(2);
        pen.setStrokeColor(Color.RED);
        
        if(Streki.debug) LOGGER.info("Initializing menu options...");
        createFileMenu(stage);
        createFileMenuChoices(stage);
        
        if(Streki.debug) LOGGER.info("Initializing Pen...");
        border = new BorderPane();
        
        if(Streki.debug) LOGGER.info("Initializing HBox...");
        HBox hBoxMenu = new HBox();
        
        if(Streki.debug) LOGGER.info("Adding top menu...");
        hBoxMenu.getChildren().add(menuBar);
        border.setTop(hBoxMenu);

        if(Streki.debug) LOGGER.info("Creating scroll pane...");
        scrollPane = new ScrollPane();
        
        if(Streki.debug) LOGGER.info("Creating stack pane...");
        stackPane = new CustomStackPane();
        
        if(Streki.debug) LOGGER.info("Initializing canvas...");
        canvas = (new CanvasBuilder())
                .setStage(stage)
                .setWidth(WIDTH)
                .setHeight(HEIGHT)
                .setGlobalAlpha(1)
                .setFillColor(Color.WHITE)
                .setCs(cs)
                .setStackPane(stackPane)
                .createCanvas();
        Event.fireEvent(canvas, 
                new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 
                        MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        
        if(Streki.debug) LOGGER.info("Adding canvas to list...");
        cs.add(canvas);
        
        if(Streki.debug) LOGGER.info("Adding cavnas to stack pane...");
        stackPane.getChildren().add(canvas);
        
        if(Streki.debug) LOGGER.info("Adding stack pane to scroll pane...");
        scrollPane.setContent(stackPane);
        
        if(Streki.debug) LOGGER.info("Adding scroll pane to center border...");
        border.setCenter(scrollPane);
        
        if(Streki.debug) LOGGER.info("Initializing color picker...");
        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.CORAL);
        
        if(Streki.debug) LOGGER.info("Setting color picker onAction...");
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event event) {
                strokeColor = colorPicker.getValue();
                Pen.getInstance().setStrokeColor(colorPicker.getValue());
                if(Streki.debug) LOGGER.info("Changed Pen color to "+Pen.getInstance().getStrokeColor());
            }
        });
        
        if(Streki.debug) LOGGER.info("Initializing pen size slider...");
        final Slider penSizeSlider = new Slider();
        penSizeSlider.setMin(0);
        penSizeSlider.setMax(10);
        penSizeSlider.setValue(1);
        penSizeSlider.setShowTickLabels(true);
        penSizeSlider.setShowTickMarks(true);
        penSizeSlider.setMajorTickUnit(2);
        penSizeSlider.setMinorTickCount(1);
        penSizeSlider.setBlockIncrement(2);
        
        penSizeSlider.valueProperty().addListener((cl) -> {
            if(Streki.debug) LOGGER.info("Changed Pen size to "+Pen.getInstance().getLineWidth());
            Pen.getInstance().setLineWidth(penSizeSlider.getValue());
        });
        
        Label l = new Label("Label");
        GridPane pg = new GridPane();
        pg.setConstraints(l, 2, 3);
        pg.add(colorPicker, 0, 0);
        pg.add(penSizeSlider, 0, 1);
        border.setRight(pg);

        //Scene scene = new Scene(border, 800, 600);
        Scene scene = new Scene(border);
        
        stage.setTitle("Streki");
        stage.setScene(scene);
        
        // Set the window to the entire size of the screen.
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());

        stage.show();
        
    }
    
    private void createFileMenu(final Stage stage) {
        if(Streki.debug) LOGGER.info("Creating file menu...");
        fileMenu = new Menu("File");

        menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        menuBar.getMenus().add(fileMenu);
    }
    
    private void createFileMenuChoices(final Stage stage) {
        if(Streki.debug) LOGGER.info("Creating file menu choices...");
        MenuItem menuItemNew = new MenuItem();
        menuItemNew.setText("New");
        menuItemNew.setOnAction((ae) -> createCanvasPropertiesModal(stage));

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
                        
                    }
                }
            });

        MenuItem menuItemExit = new MenuItem();
        menuItemExit.setText("Exit");
        menuItemExit.setOnAction((ae) -> System.exit(0));
        
        fileMenu.getItems().addAll(menuItemNew, menuItemExport, menuItemExit);
    }
    
    
    private Canvas createCanvasPropertiesModal(Stage stage) {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New Canvas");
        dialog.setHeaderText("New Canvas");

        ButtonType createButtonType = new ButtonType("Create", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField canvasWidth = new TextField();
        canvasWidth.setPromptText("width");
        TextField canvasHeight = new TextField();
        canvasHeight.setPromptText("height");

        grid.add(new Label("width:"), 0, 0);
        grid.add(canvasWidth, 1, 0);
        grid.add(new Label("height:"), 0, 1);
        grid.add(canvasHeight, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return new Pair<>(canvasWidth.getText(), canvasHeight.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        Canvas c = (new CanvasBuilder()).setStage(stage)
                .setWidth(Integer.parseInt(canvasWidth.getText()))
                .setHeight(Integer.parseInt(canvasHeight.getText()))
                .setGlobalAlpha(0.1)
                .createCanvas();
        
        //stackPane.setContent(c);
        stage.setWidth(c.getWidth());
        stage.setHeight(c.getHeight());

        return c;
    }
    
}
