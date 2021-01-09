package Photos;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.UserDB;

/**
 * Driver class for photos application.
 *
 */
public class Photos extends Application{

	/**
	 * Start method for application
	 * @param primaryStage the main stage of application.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		LoginController loginController = loader.getController();
		loginController.start(primaryStage);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photo Album");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/**
	 * Main method
	 * @param args Arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
