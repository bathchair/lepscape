import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.*;


public class Summary extends View {
	public Controller ic;
	Pane main;
	Canvas canvas;

/**
 * set up a stage and border pane to hold other panes
 * @param stage
 * @param c
 * @param manageView
 */
	public Summary(Stage stage, Controller c, ManageViews manageView) {
		// set up the stage with different area
		super(stage, c, manageView);
		border = new BorderPane();
		border.setBottom(addBottomHBox());
		border.setLeft(addNavigationVBox());
		border.setCenter(addCenterPane());
		
		/*
	       // load butterfly animation
	       ImageView iv1 = new ImageView();
	       Image butterfly = new Image(getClass().getResourceAsStream("/butterfly.png"));
	      // Image flapping = new Image(getClass().getResourceAsStream("/flapping.png"));
	       iv1.setImage(butterfly);
	       iv1.setPreserveRatio(true);
	       iv1.setFitHeight(30);
	       
	       Duration duration = Duration.minutes(2);
	       TranslateTransition translation = new TranslateTransition(duration, iv1);
	       translation.setByX(100);
	       translation.setAutoReverse(true);
	       sp1.getChildren().add(iv1);
	       translation.play();
	       */
		
       //main = addCanvas();
		border.setCenter(main);  
    }
	
/**
 * add bottom pane to hold the buttons for create new garden and download
 * @return the bottom pane created 
 */
	public HBox addBottomHBox() {
		HBox box = new HBox();
        box.setStyle("-fx-background-color: steelblue");
        box.setSpacing(15);
        box.setPadding(new Insets(15, 12, 15, 12));
        box.setAlignment(Pos.CENTER_RIGHT);
        box.getChildren().addAll(addBottomButtons());
        return box;
	}
	
/**
 * create an array list of buttons of download and create new garden
 * @return an array list of buttons
 */
	public ArrayList<ImageView> addBottomButtons() {
		// make buttons for Lepedia, Download and Create New Garden
        ArrayList <ImageView> bottomButtons = new ArrayList <>();
        ImageView download = new ImageView(this.manageView.buttonImages.get("download"));
        download.setOnMouseClicked(e -> {
        	FileChooser file = new FileChooser();
        	file.setTitle("Download File");
        	File file1 = file.showSaveDialog(stage);
        });
        bottomButtons.add(download);
        bottomButtons.add(addNextButton("next", "Restart"));
        return bottomButtons;
	}
	
/**
 * create a vertical box pane to hold the navigation buttons
 * @return the vertical pane
 */
	public VBox addNavigationVBox() {
		VBox sideVBox = new VBox();
        sideVBox.setStyle("-fx-background-color: lavender");
        sideVBox.setSpacing(15);
        sideVBox.setPadding(new Insets(20));
       // sideVBox.getChildren().addAll(addNavigationButtons()); //uncomment
        sideVBox.setAlignment(Pos.TOP_RIGHT);
        return sideVBox;
	}

/**
 * create an array list of navigation buttons
 * @return an array list of buttons
 */
	public ArrayList<ImageView> addNavigationButtons() {
		ArrayList <ImageView> buttons = new ArrayList<>();
        buttons.add(addNextButton("back", "GardenDesign"));
        buttons.add(addNextButton("clear", "Lepedia"));
        buttons.add(addNextButton("download","LearnMore"));
        ImageView saveGarden = new ImageView(this.manageView.buttonImages.get("next"));
        saveGarden.setOnMouseClicked(controller.getHandlerforSummarySave());
        buttons.add(saveGarden);
        buttons.add(addNextButton("next","Gallery"));
        return buttons;
	}

/**
 * create a center pane to hold the garden design 
 * @return the center pane
 */
	public StackPane addCenterPane() {
		StackPane centerPane = new StackPane();
		centerPane.setStyle("-fx-border-color: chocolate; -fx-border-width: 5px; -fx-background-color: lightblue");
		return centerPane;
	}
	
/**
 * Makes the canvas so the previously set garden outline can be displayed
 * Canvas then places inside a pane
 * @return the created pane
 */
	public void addCanvas() {
		Pane gardenDesign = new Pane();
		gardenDesign.setStyle("-fx-border-color:GREY; -fx-border-width:5px");
		canvas = new Canvas();
		canvas.setStyle("-fx-border-color:GREY; -fx-border-width:5px");
		gc = canvas.getGraphicsContext2D();
//		gardenDesign.getChildren().add(canvas);
	
		canvas.widthProperty().bind(gardenDesign.widthProperty());
		canvas.heightProperty().bind(gardenDesign.heightProperty());
	
		canvas.widthProperty().addListener(e -> controller.drawToCanvas(canvas));
		canvas.heightProperty().addListener(e -> controller.drawToCanvas(canvas));
		
//		canvas = new Canvas();
//		canvas.setStyle("-fx-border-color:GREY; -fx-border-width:5px");
//		gc = canvas.getGraphicsContext2D();
//		gardenDesign.getChildren().add(canvas);
//	
//		canvas.widthProperty().bind(gardenDesign.widthProperty());
//		canvas.heightProperty().bind(gardenDesign.heightProperty());
//	
//		canvas.widthProperty().addListener(e -> controller.drawToCanvas(canvas));
//		canvas.heightProperty().addListener(e -> controller.drawToCanvas(canvas));
		
		ImageView iv = new ImageView(manageView.savedImg);
		iv.setPreserveRatio(true);
		iv.fitWidthProperty().bind(gardenDesign.widthProperty());
		iv.fitHeightProperty().bind(gardenDesign.widthProperty());
		gardenDesign.getChildren().add(iv);
		border.setCenter(gardenDesign);
	}
	
/**
 * create a tilepane to hold information about the garden with updated cost and leps count
 * @return
 */
	public void updateLepandCost(double cost, int leps) {
		VBox rightPane = new VBox();
	    rightPane.setPadding(new Insets(10));
	    rightPane.setStyle("-fx-background-color: lavender");
	    Text title = new Text("Summary");
	    title.setFont(Font.font(null, FontWeight.BOLD, 30));
	    
		HBox box1 = new HBox();
	    Image lepCount = new Image(getClass().getResourceAsStream("/butterfly1.png"));
	    ImageView lepIV = new ImageView(lepCount);
		lepIV.setPreserveRatio(true);
		lepIV.setFitHeight(20);
		Label lep = new Label(""+ leps);
		lep.setGraphic(lepIV);
		box1.getChildren().addAll(lepIV, lep);
		
		HBox box = new HBox();
		Image dollar = new Image(getClass().getResourceAsStream("/dollar.png"));
		ImageView costIV = new ImageView(dollar);
		Label totalCost = new Label("" + cost);
		totalCost.setGraphic(costIV);
		costIV.setPreserveRatio(true);
		costIV.setFitHeight(20);
		box.getChildren().addAll(costIV, totalCost);
		
		rightPane.getChildren().addAll(title, box1, box);
		border.setRight(rightPane);
	}
}