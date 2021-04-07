
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.*;

public class PlotDesign extends View{
	public ArrayList<Button> drawingShapes;
	public ArrayList<Button> editingTools;
	public TextField search;
	public Button back;
	public Button next;
	public Button clear;
	
	public PlotDesign(Stage stage, Scene scene, Group root, Controller c) {
		super(stage, root, c);
	}
}