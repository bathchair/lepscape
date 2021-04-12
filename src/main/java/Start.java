import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class Start extends View {
	public ArrayList<Button> buttons;
	
	/**
	 * @author Ishika Govil 
	 */
	
	/**
	 * Creates a simple introductory screen with two buttons, new plot and gallery of saved plots
	 * @param Stage
	 * @param Controller
	 * @param ManageViews
	 */
	public Start(Stage stage, Controller c, ManageViews manageView) {
		super(stage, c, manageView);     
		Canvas canvas = new Canvas(screenWidth, screenHeight);
		border = new BorderPane();
		border.getChildren().add(canvas);
        border.setStyle("-fx-background-color: #94DF86");
        HBox box = new HBox();
        box.setSpacing(15);
        border.setCenter(box);
 
        //Populate button array
		buttons = new ArrayList<Button>();
		buttons.add(addNextButton( "New Garden", "PlotDesign"));
		buttons.add(addNextButton("My Gallery", "Gallery"));
		box.getChildren().addAll(buttons); 
		box.setAlignment(Pos.CENTER);

	}
}