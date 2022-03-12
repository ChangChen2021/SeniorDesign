package application;
 
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region; 
import javafx.stage.Stage;
import view.DrawAxis;
  
		/**
		 * A sample that demonstrates polygon construction.
		 */
public class AxisTest extends Application {			
			
		    // Will be a simple red-filled triangle
			private int centerX=400;
			private int centerY=300;
			private int axisLength=100;
		    public Parent createContent() { 
 
		        Pane root = new Pane();
		        root.setPrefSize(800, 600);
		        root.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		        root.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); 
		        
		        DrawAxis axis=new DrawAxis(400, 300, 300);

		        root.getChildren().addAll(
		        		axis.getxAxis(),
		        		axis.getyAxis(),
		        		axis.getxAxisTip(),
		        		axis.getyAxisTip());
		 
		        return root;
		    }
		 
		    @Override
		    public void start(Stage primaryStage) throws Exception {
		    	primaryStage.setTitle(" Coordinate System");
		        primaryStage.setScene(new Scene(createContent()));
		        primaryStage.show();
		    }
		 
		    /**
		     * Java main for when running without JavaFX launcher
		     */
		    public static void main(String[] args) {
		        launch(args);
		    } 
}
