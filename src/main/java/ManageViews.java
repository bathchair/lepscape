import java.util.*;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.File;
import java.net.URISyntaxException;

public class ManageViews {
	public double dimPixel; //Used when setting the dimensions of the garden
	public ArrayList<double[]> dimLen; //Used when setting the dimensions of the garden
	public WritableImage img; //sets the image from PlotDesign
	public double screenWidth;
	public double screenHeight;
	Map<String,View> views;
	View currView;
	Controller controller;
	Stage stage;
	public Map<String, ImageView> plantImages;
	public Map<String, ImageView> lepImages;
	public Map<String, Image> buttonImages;
	public Pane sp;
	public WritableImage savedImg;
	/**
	 * @author Ishika Govil
	 */
	
	/**
	 * Constuctor for the ManageViews class, which initializes the HashMap of views and other fields
	 * Sets the first view screen to the Start screen
	 * @param Stage
	 * @param Controller
	 * @author Ishika Govil
	 */
	public ManageViews(Stage stage, Controller c, String fileName, String fileName2, double width, double height) {
		importImages(fileName, fileName2);
		dimLen = new ArrayList<>();
		dimPixel = -1;
		this.screenWidth = width;
		this.screenHeight = height;
		this.controller = c;
	    this.stage = stage;
	    this.sp = new Pane();
		importButtonImages();
		initializeViews();
	    this.currView = this.getView("Start");
	}
	
	
	public void importButtonImages() {
		File[] file;
		this.buttonImages = new HashMap<>();
		try {
			file = (new File(getClass().getResource("/Buttons").toURI())).listFiles();
			if (file != null) {
				for (File child : file) {
					this.buttonImages.put(child.getName().substring(0, child.getName().length()-4), new Image(getClass().getResourceAsStream("/Buttons/" + child.getName()),100,100,false,false));
			    }
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void setPlantImg(Map<String, ImageView> imgs) {
		this.plantImages = imgs;
	}

	public void importImages(String fileName, String fileName2) {
		plantImages = new HashMap<>();
		System.out.println("setting plant images");
		plantImages = CSVtoPlants.readFileForImg(fileName);
		lepImages = new HashMap<>();
		System.out.println("setting lep images");
		lepImages = CSVtoLeps.readFileForImg(fileName2);
	}

	public Map<String, ImageView> getPlantImages() {
		return this.plantImages;
	}
	
	public Map<String, ImageView> getLepImages() {
		return this.lepImages;
	}
	
	/**
	 * Loads all the screens to the HashMap with a key that corresponds to the class
	 * @author Ishika Govil
	 */
	public void initializeViews() {
		views = new HashMap<>();
		views.put("Start", new Start(stage, controller,this));
	    views.put("Gallery", new Gallery(stage,controller,this));  
	    views.put("PlotDesign", new PlotDesign(stage, controller,this));
	    views.put("ConditionScreen", new ConditionScreen(stage,controller,this));
	    views.put("Summary", new Summary(stage,controller,this));
	    views.put("GardenDesign", new GardenDesign(stage,controller,this));
	    views.put("LearnMore", new LearnMore(stage,controller,this));
	    views.put("Lepedia", new Lepedia(stage,controller,this));
	}
	
	/** 
	 * Called in Controller when user wishes to switch views
	 * The next action is determined by the string passed to the function
	 * @param String describing the next action to be shown
	 * @author Ishika Govil 
	 */
	public void switchViews(String next) {
		if(next.equals("Drawing")) {
			((PlotDesign) this.currView).onDrawing();
		}
		else if(next.equals("Shape")) {
	        //Only allows for one shape drawn at once
			if(((PlotDesign) this.currView).shapeClicked == false) {
				((PlotDesign) this.currView).onShape();
			}
			else {				
				((PlotDesign) this.currView).dragAnchor = true;
				((PlotDesign) this.currView).toggleAnchorHandler();
			}
			((PlotDesign) this.currView).validateSave();
			((PlotDesign) this.currView).shapeClicked = true;
		}
		else if (next.equals("Lepedia")) {
			this.currView = this.getView(next);
			Lepedia temp = (Lepedia)this.currView;
			temp.updateLepedia();
		}
		else
			this.currView = this.getView(next);
	}
	
	/**
	 * Makes data for savedImg
	 * @return the matrix of data
	 */
	//for the next 2 methods
	//https://stackoverflow.com/questions/33074774/javafx-image-serialization
	public int[][] makeData() {
		int width = (int)savedImg.getWidth();
		int height = (int) savedImg.getHeight();
		int[][] data = new int[width][height];
		PixelReader r = savedImg.getPixelReader();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                data[i][j] = r.getArgb(i, j);
            }
        }
        return data;
	}
	
	/**
	 * Makes the image for given information of an image
	 * @param width the width of image
	 * @param height the height of image
	 * @param data the data for image
	 */
	public void makeImage(int width, int height, int[][] data) {
		System.out.println(width);
		System.out.println(height);
		WritableImage img = new WritableImage(width, height);
	    PixelWriter w = img.getPixelWriter();
	    for (int i = 0; i < width; i++) {
	    	for (int j = 0; j < height; j++) {
	    		w.setArgb(i, j, data[i][j]);
	    	}
	    }
	    setSavedImage(img);
	}
	
	
	/** 
	 * Returns the BorderPane associated with the current View
	 * @return BorderPane 
	 */
	public BorderPane getBorderPane() {
		return this.currView.border;
	}
	/** 
	 * Returns the screen width associated with the current View
	 * @return double 
	 */
	public double getScreenWidth() {
		return this.screenWidth;
	}
	/** 
	 * Returns the screen height associated with the current View
	 * @return double 
	 */
	public double getScreenHeight() {
		return this.screenHeight;
	}


	/** 
	 * Returns the GraphicsContext associated with the current View
	 * @return GraphicsContext 
	 */
	public GraphicsContext getGC() {
		return this.currView.gc;
	}
	
	/** 
	 * Returns the view associated with the given key
	 * @param String representing key of the desired View
	 * @return View associated with the key
	 */
	public View getView(String key) {
		return views.get(key);
	}
	
	/** 
	 * Writes the WritableImage field img after user saves their PlotDesign
	 * Useful for sharing the WritableImage between View classes
	 * @param WritableImage  
	 */
	public void setImage(WritableImage img) {
		this.img = img;
	}
	
	public void setSavedImage(WritableImage img) {
		System.out.println("called");
		this.savedImg = img;
	}
	
	/** 
	 * Sets the current View to the View described by the key
	 * @param String representing key of the desired View
	 */
	public void setView(String key) {
		this.currView = this.views.get(key);
	}
	
	/** 
	 * Called when mouse enters or exits a button
	 * @param boolean describing if the cursor to be shown is a hand or not 
	 * @param String key representing key for the ImageView
	 * @param ImageView b representing the ImageView being hovered
	 * @author Ishika Govil 
	 */
	public void onChangeCursor(boolean hand, String key, ImageView b) {
		this.currView.changeCursor(hand,key,b);
	}
	
	// restart the plot, clear all lines so user can draw a new garden design
	public void restartPlot() {
		initializeViews();
		this.dimLen = new ArrayList<>(); 
		this.currView = this.views.get("PlotDesign");
	}
	
	public void drawLine(double x1, double y1, double x2, double y2, boolean isPolygon) {
		this.currView.drawLine(x1, y1, x2, y2, isPolygon);
	}
	
	public void validateSave() {
		if(this.currView instanceof PlotDesign)
			((PlotDesign) this.currView).validateSave();
	}
	
	//methods only used by garden design
	public void setY(double y, Node n){
		currView.setY(y, n);
	}
	
	public void setX(double x, Node n){
		currView.setX(x, n);
	}
	
	public boolean getCalledFromStart() {
		return ((Gallery) views.get("Gallery")).calledFromStart;
	}
	
	public void setCalledFromStart(boolean called) {
		((Gallery) views.get("Gallery")).calledFromStart = called;
		((Gallery) views.get("Gallery")).setBackButton();
	}
	
	public void updateLepandCost(double cost, int lepCount) {
		((Summary) views.get("Summary")).updateLepandCost(cost, lepCount);
	}
	

}
