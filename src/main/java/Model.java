import java.util.*;

public class Model {
	public int budget;
	public Garden gardenMap;
	public Map<String, PlantSpecies> plantDirectory;
	public Map<String, Lep> lepDirectory;
	public double lengthPerPixel;
	
	public Model() {
		this.gardenMap = new Garden();
		this.plantDirectory = new HashMap<>();
		this.lepDirectory = new HashMap<>();
	}
	// Methods for the user to draw the garden and put in desired conditions
	// create the optimal garden based on leps and conditions provided
	public void createDefault() {}
	// get the boundaries of the garden
	public ArrayList<double[]> getBoundaries() {
		return this.gardenMap.getOutline();
	}
	public Garden getGarden() {
		return this.gardenMap;
	}
	// create a new condition for the garden
	public void createNewConditions() {}
	// update the outline whenever the user puts down another point to connect
	public void updateOutlineSection(double x, double y) {
		this.gardenMap.updateOutline(x, y);
	}
	// update the outline to the condition that user wants
	public void updateConditions() {}
	// find dimension of the garden
	public void findDimensions() {}
	
	// Methods to use in order to place down plants
	//choose 2 plants to compare the number of leps they support
	public void comparePlant() {}
	// get plant's sphere
	public void getPlantInfo() {}
	// check if plant is okay to be placed
	public void validatePlacement() {}
	// place down plants
	public void placePlant() {}
	// update the cost every time a plant is placed
	public void costUpdate() {}
	
	/*@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}*/
	public double getX() {
		return 0;
	}
	public double getY() {
		return 0;
	}
	public double getLengthPerPixel() {
		return this.lengthPerPixel;
	}
	public void setLengthPerPixel(double pix) {
		this.lengthPerPixel = pix;
	}
}