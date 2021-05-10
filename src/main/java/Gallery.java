import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import java.util.*;

//https://stackoverflow.com/questions/44841329/how-to-implement-serializable-for-my-project-to-have-persistence
public class Gallery extends View{
	final int GARDEN_IMAGEVIEW_HEIGHT=100;
	final int HBOX_SPACING = 20;
	final int INFO_IV_SIZE = 50;
	public ArrayList<Button> multiview;
	public Button back;
	public Button sort;
	public TextField search;
	public TilePane tile;
	boolean calledFromStart;
	Stage stage;
	
	public Gallery(Stage stage, Controller controller, ManageViews manageView) {
		super(stage, controller, manageView);
		border = new BorderPane();
		this.stage = stage;
		// back button 
		
//		back.setOnAction(controller.getHandlerforClicked("Back to Start"));
		setBackButton();
		
		ScrollPane root = new ScrollPane();
		tile = new TilePane();
		tile.setStyle("-fx-background-color: lightblue; -fx-border-color: chocolate; -fx-border-width: 5px");
		tile.setPadding(new Insets(5));
        tile.setVgap(4);
        tile.setHgap(4);
        tile.setPrefHeight(stage.getHeight());
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);    // Horizontal scroll bar
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        root.setFitToHeight(true);
        root.setFitToWidth(true);
        root.setContent(tile);  
		border.setCenter(root);
		
		// make title of the page
		HBox hb2 = new HBox();
		hb2.setPadding(new Insets(20));
		Text title = new Text("Gallery");
		title.setTextAlignment(TextAlignment.LEFT);
		title.setFont(Font.font(null, FontWeight.BOLD, 40));
		hb2.getChildren().add(title);
		border.setTop(hb2);
		this.calledFromStart = true;
	}
	
	public void setBackButton(){
		HBox hb1 = new HBox();
		hb1.setAlignment(Pos.BASELINE_LEFT);
		hb1.setPadding(new Insets(20));
		back = new Button("Back");
		back.setPrefSize(100, 30);

		if(calledFromStart)
			hb1.getChildren().add(addNextButton("back","Start"));
		else
			hb1.getChildren().add(addNextButton("back","Summary"));
		
		border.setBottom(hb1);
	}

	//https://stackoverflow.com/questions/22166610/how-to-create-a-popup-windows-in-javafx
	public void loadScreen(WritableImage gardenImage, int index, double cost, double leps, String title) {
		System.out.println("in here");
		BorderPane gardenTile = new BorderPane();
		ImageView iv = new ImageView(gardenImage);
		Label titleLabel = new Label(title);
		titleLabel.setFont(new Font("Andale Mono", 16));
		gardenTile.setBottom(titleLabel);
		gardenTile.setPadding(new Insets(0,5,0,5));
		gardenTile.setCenter(iv);
		gardenTile.setAlignment(titleLabel, Pos.CENTER);
		iv.setPreserveRatio(true);
		iv.setFitHeight(GARDEN_IMAGEVIEW_HEIGHT);
		gardenTile.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                dialog.setTitle(title);
                Image lep = new Image(getClass().getResourceAsStream("/butterfly1.png"));
        		Image dollar = new Image(getClass().getResourceAsStream("/dollar.png"));
        		
        		ImageView lepIv= new ImageView(lep);
        		ImageView budgetIv = new ImageView(dollar);
        		lepIv.setPreserveRatio(true);
        		lepIv.setFitHeight(INFO_IV_SIZE);
        		budgetIv.setPreserveRatio(true);
        		budgetIv.setFitHeight(INFO_IV_SIZE);
        		
        		Label lepLabel = new Label(""+leps);
        		lepLabel.setFont(new Font("Andale Mono", 16));
        		Label costLabel = new Label(""+cost);
        		costLabel.setFont(new Font("Andale Mono", 16));
        		lepLabel.setGraphic(lepIv);
        		costLabel.setGraphic(budgetIv);
        		
        		HBox information = new HBox(HBOX_SPACING);
        		information.setAlignment(Pos.CENTER);
        		information.getChildren().add(lepLabel);
        		information.getChildren().add(costLabel);
     
                BorderPane bp = new BorderPane();
                
                HBox hb = new HBox(HBOX_SPACING);
                Button edit = new Button("Edit");
                Button delete = new Button("Delete");
                edit.setOnAction(controller.getHandlerforEditSaved(index,dialog));
                delete.setOnAction(controller.getHandlerforDeleteSaved(index, dialog));
                hb.getChildren().add(delete);
                hb.getChildren().add(edit);
                bp.setBottom(hb);
                hb.setAlignment(Pos.CENTER);
                bp.setAlignment(hb, Pos.CENTER);
                
                bp.setTop(information);
                bp.setAlignment(information, Pos.CENTER);
                ImageView garden = new ImageView(gardenImage);
                garden.setPreserveRatio(true);
                garden.setFitHeight(5*GARDEN_IMAGEVIEW_HEIGHT);
                bp.setCenter(garden);
                Scene dialogScene = new Scene(bp);
                dialog.setScene(dialogScene);
				dialog.show();
			}
            
		});
		tile.getChildren().add(gardenTile);

	}
	
	public void removeGardenFromPane(int index) {
		tile.getChildren().remove(index);
	}
	
}


