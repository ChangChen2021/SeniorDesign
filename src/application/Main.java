package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.MyView;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * 
 * @author Chang Chen
 *
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		MyView myView= new MyView();
		try {
			//set up the primary stage size
			Scene scene = new Scene(myView.root,1200,600);
			//set up the primary stage title
			primaryStage.setTitle("Demo");
			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
