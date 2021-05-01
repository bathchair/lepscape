import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.*;

public abstract class View{
	public Map<String, Image> plantImages;
	
	private static final int lineWidth = 3;
	private static final double fillThreshold = 0.95;
	
	public int screenWidth = 1290;
	public int screenHeight = 800;
	public double gardenWidth = 0.5*screenWidth;
	public double gardenHeight = 0.5*screenHeight;
	public double gardenTopLeftX = 0.3*screenWidth;
	public double gardenTopLeftY = 0.2*screenHeight;
	public int buttonWidth = 100;
	public int buttonHeight = 30;
	BorderPane border;
	Stage stage;
	Controller controller;
	ManageViews manageView;
	GraphicsContext gc;
	ArrayList<Line> polygonLines;
	ArrayList<Line> freeLines;
	//Image from: https://custom-cursor.com/en/collection/life-style/hand-painted-poppy-flower
	ImageCursor flowerCursor = new ImageCursor(new Image(getClass().getResourceAsStream("/flowerCursor.png"), 30,40,false,false));
	//Image from: https://custom-cursor.com/en/collection/animals/blue-and-purple-butterfly
	ImageCursor handCursor = new ImageCursor(new Image(getClass().getResourceAsStream("/lepCursor.png"), 40,40,false,false));
	/**
	 * View class that is the super class for all View screens
	 * @param Stage stage
	 * @param Controller c
	 * @param ManageViews manageView
	 */
	public View(Stage stage, Controller c, ManageViews manageView) { 
		this.manageView = manageView;
		this.stage = stage;
		this.polygonLines = new ArrayList<>();
		this.freeLines = new ArrayList<>();
        this.controller = c;
		this.stage.setTitle("Lepscape");
		
	}	
	/**
	 * Sets the translateX value of a node n
	 * @param double x
	 * @param Node n
	 */
	public void setX(double x, Node n) {
		n.setTranslateX(x);
	}
	/**
	 * Gets the BorderPane
	 * @return BorderPane
	 */
	public BorderPane getBorderPane() {
		return border;
	}
	/**
	 * Returns the screenWidth
	 * @return int 
	 */
	public int getScreenWidth() {
		return screenWidth;
	}
	/**
	 * Returns the screenHeight
	 * @return int 
	 */
	public int getScreenHeight() {
		return screenHeight;
	}
	
	/**
	 * Sets the translateX value of a node n
	 * @param double x
	 * @param Node n
	 */
	public void setY(double y, Node n) {
		n.setTranslateY(y);
	}

	/**
	 * Updates the cursor to the different ImageCursors
	 * @param boolean hand
	 */
	public void changeCursor(boolean hand) { //Changes cursor to either a hand if true is passed, or pointer if false
		//https://blog.idrsolutions.com/2014/05/tutorial-change-default-cursor-javafx/ 
		if(hand)
			stage.getScene().setCursor(this.handCursor);
		else
			stage.getScene().setCursor(this.flowerCursor);
	} 

	/**
	 * Adds a button with the correct size and actions
	 * @param String text representing text to be displayed on the Button
	 * @param String next representing the action the button performs when clicked
	 * @return Button
	 */
	public Button addNextButton(String text, String next) {
		Button b = new Button(text);
		b.setPrefSize(buttonWidth, buttonHeight);
		setOnMouse(b);
		b.setOnAction(controller.getHandlerforClicked(next));
		return b;
	}	

	/**
	 * Sets the MouseEntered and MouseExited handlers for Button
	 * @param Button b
	 */
	public void setOnMouse(Button b) {
		b.setOnMouseEntered(controller.getHandlerforMouseEntered());
		b.setOnMouseExited(controller.getHandlerforMouseExited());
	}
	/**
	 * Adds a line between (x1,y1) and (x2,y2)
	 * @param double x1
	 * @param double y1
	 * @param double x2
	 * @param double y2
	 * @param boolean isPolygon
	 */
	public void drawLine(double x1, double y1, double x2, double y2, boolean isPolygon) {
		Line line = new Line(x1, y1, x2, y2);
		line.setStrokeWidth(2);
		border.getChildren().add(line);
		if(isPolygon) 
			polygonLines.add(line);
		else
			freeLines.add(line);
	}
	

	/**
	 * Removes all the lines drawn using the drawLine() method
	 */
	public void removeLines() {
			border.getChildren().removeAll(polygonLines);
			polygonLines = new ArrayList<>();
			border.getChildren().removeAll(freeLines);
			freeLines = new ArrayList<>();
	}
	
	public static double drawOnCanvas(Canvas canvas, ArrayList<Vector2> points, ArrayList<Vector2> extrema, ArrayList<Conditions> conditions) {
		double minX = extrema.get(3).getX();
		double maxX = extrema.get(1).getX();
		double minY = extrema.get(0).getY();
		double maxY = extrema.get(2).getY();
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setLineWidth(lineWidth);
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		double scale = findScale(minX, maxX, minY, maxY, canvas.getWidth(), canvas.getHeight());
		
//		System.out.println("minX: " + minX);
//		System.out.println("maxX: " + maxX);
//		System.out.println("minY: " + minY);
//		System.out.println("maxY: " + maxY);
//		System.out.println("scale: " + scale);
//		System.out.println("cw: " + canvas.getWidth());
//		System.out.println("ch: " + canvas.getHeight());
		
		drawOutlines(gc, points, scale, minX, minY);
		
		Iterator<Conditions> condIter = conditions.iterator();
		
		System.out.println("Drawing conditions");
		while(condIter.hasNext()) {
			Conditions cond = condIter.next();
			int startX = (int) ((cond.getX() - minX) * scale);
			int startY = (int) ((cond.getY() - minY) * scale);
			floodFill(canvas, cond, startX, startY, (int) canvas.getWidth(), (int) canvas.getHeight());
			gc.save();
			System.out.println("drawing cond at " + startX + " " + startY);
		}
		return scale;
	}
	
	public static double findScale(double minX, double maxX, double minY, double maxY, double targetWidth, double targetHeight) {
		double sourceWidth = maxX - minX;
		double sourceHeight = maxY - minY;

		double xScale = targetWidth / sourceWidth;
		double yScale = targetHeight / sourceHeight;

		return Math.min(xScale, yScale);
	}
	
	private static void drawOutlines(GraphicsContext gc, ArrayList<Vector2> points, double scale, double minX, double minY) {
		Iterator<Vector2> pointIter = points.iterator();

		boolean isNewLine = true;
		
		while(pointIter.hasNext()) {

			Vector2 point = pointIter.next();
			double x = (point.getX() - minX) * scale;
			double y = (point.getY() - minY) * scale;
			
//			System.out.println("x: " + x + " y: " + y);

			if(x < 0 && y < 0) {
				isNewLine = true;
			} else if (isNewLine) {
				gc.beginPath();
				gc.moveTo(x, y);
				isNewLine = false;
			} else {
				gc.lineTo(x, y);
				gc.stroke();
			}
			
		}
		gc.closePath();
	}

	private static void floodFill(Canvas canvas, Conditions conds, int startX, int startY, int width, int height) {
		// Inspired by the flood fill example https://stackoverflow.com/questions/23983465/is-there-a-fill-function-for-arbitrary-shapes-in-javafx
		Stack<Point2D> fillStack = new Stack<>();

		WritableImage snapshot = canvas.snapshot(null, null);
		PixelReader pr = snapshot.getPixelReader();
		PixelWriter imgPW = snapshot.getPixelWriter();

		GraphicsContext gc = canvas.getGraphicsContext2D();
		PixelWriter pw = gc.getPixelWriter();

		
		Color fillColor = conds.toColor();
		fillStack.push(new Point2D(startX, startY));
		
		while(!fillStack.empty()) {
			Point2D curr = fillStack.pop();
			int x = (int) curr.getX();
			int y = (int) curr.getY();

			if(pr.getColor(x, y).grayscale().getBrightness() < fillThreshold) continue;
			
			pw.setColor(x, y, fillColor);
			imgPW.setColor(x, y, Color.BLACK);

			if(x > 0) {
				fillStack.push(new Point2D(x-1, y));
			}
			
			if(x < width - 1) {
				fillStack.push(new Point2D(x+1, y));
			}

			if(y > 0) {
				fillStack.push(new Point2D(x, y-1));
			}

			if(y < height - 1) {
				fillStack.push(new Point2D(x, y+1));
			}
				
		}
	}
	
	//Used only in gardenDesign. In here because need to called by controller
	public void removePlant(Node n) {}
	public void makeInfoPane(String name, String info) {}
	public void updateBudgetandLep(int cost, int lepCount) {}


}
