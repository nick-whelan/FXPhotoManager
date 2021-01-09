package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Album;
import model.Photo;
import model.User;
import model.UserDB;
/**
 * this is the controller for the login view
 * this view is called login.fxml
 *
 */
public class LoginController {
	/**
	 * these are the fxml classes used in the controller
	 */
	@FXML Button loginButton;
	@FXML TextField loginTextField;
	@FXML Button quitButton;
	
	// Create userDB
	/**
	 * these are the fields used in the controller
	 */
	UserDB userDB;

	/**
	 * method for when the login button is clicked
	 * @param event on click
	 * @throws IOException for loading the controller
	 * @throws ClassNotFoundException for loading the controller
	 */
	@FXML
	private void handleLoginButton(ActionEvent event) throws IOException, ClassNotFoundException {
		
		Parent root;
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		
		userDB = UserDB.loadUsers();
		
		// Check if login field is not empty
		if(!loginTextField.getText().isEmpty()) {
			// Check if user is admin, goto admin scene
			if(loginTextField.getText().strip().equalsIgnoreCase("admin")) {
				
				// Goto admin scene
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
				root = (Parent) loader.load();
				AdminController controller = loader.getController();
				controller.setUserDB(userDB);
				Scene adminScene = new Scene(root);
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				controller.start(stage);
				stage.setScene(adminScene);
				stage.show();
				return;
			}
			
			// Check if user is in list, otherwise user dne error
			// userDB.containsUser(loginTextField.getText())
			//System.out.println(userDB.getUserList().isEmpty());
			if(userDB == null) {
				//System.out.println("NULL");
				userDB = UserDB.loadUsers();
				//System.out.println("RELOAD?");
				if(userDB == null) {
					//System.out.println("STILL NULL");
				}
			}
			if(userDB.containsUser(loginTextField.getText()) && !loginTextField.getText().equalsIgnoreCase("admin")) {
				// User is in list, goto albums home scene -> (Need to populate data in albumsctrler.start()
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albums.fxml"));
				root = (Parent) loader.load();
				AlbumController albumController = loader.getController();
				// Run a method like setActiveUser, that takes a parameter username and sets it as a field. 
				albumController.setUser(userDB.getUserByName(loginTextField.getText().toLowerCase()));
				albumController.setUserDB(userDB);
				Scene albumScene = new Scene(root);
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				albumController.start(stage);
				stage.setScene(albumScene);
				stage.show();
				
			}else {
				// If user does not exist -> Error
				Alert emptyField = new Alert(AlertType.ERROR);
				emptyField.initOwner(window);
				emptyField.setTitle("User does not exist");
				emptyField.setHeaderText("Error: Username not found");
				emptyField.showAndWait();
			}
			
		}else {
			// If login field is empty -> warning
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("Empty Username Field");
			emptyField.setHeaderText("Error: Must enter username");
			emptyField.showAndWait();
		}
		
	}
	
	/**
	 * this method is for the quit button, closes the app when clicked
	 * @param event on click
	 */
	@FXML
	private void handleQuitButton(ActionEvent event) {
		Stage stage = (Stage) quitButton.getScene().getWindow();
		//close the stage
		stage.close();
		//close the app
		Platform.exit();
	}

	/**
	 * this is the start method and it loads the users and the stock user
	 * @param primaryStage Stage object
	 * @throws FileNotFoundException Thrown if path is not found.
	 * @throws IOException For loading images
	 * @throws ClassNotFoundException Thrown if .dat cannot be loaded
	 */
	public void start(Stage primaryStage) throws FileNotFoundException, IOException, ClassNotFoundException {
		//Create userDB
		//userDB = new UserDB();
		//System.out.println("START");
		// NOTE add checks for no DB, create if needed
		// un comment below then recomment to remake .dat
		//userDB.writeUsers(users);
		try {
			userDB = UserDB.loadUsers();
			/*
			if(userDB == null) {
				System.out.println("WROTE - 1 - NULL RESET");
				userDB = new UserDB();
				addStockPhotos();
			}
			*/
			//System.out.println("LOADED");
			//System.out.println(userDB);
		} catch (FileNotFoundException e) {
			//System.out.println("WROTE - 2");
			userDB = new UserDB();
			
			addStockPhotos();
			
			UserDB.writeUsers(userDB); 
			//System.out.println("WROTE OUT - " + userDB);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * adds the stock photos to the stock user
	 * @throws IOException exception for loading images
	 */
	public void addStockPhotos() throws IOException {
		userDB.addUser("stock");
		User user = userDB.getUserByName("stock");
		user.addAlbum("stock");
		Album stockAlbum = user.getAlbumFromList("stock");
		
		Photo stock1 = new Photo("data/stock1.jpeg");
		stockAlbum.addPhoto(stock1);
		File file1 = new File("data/stock1.jpeg");
		Path path1 = file1.toPath();
		BasicFileAttributes attr = Files.readAttributes(path1, BasicFileAttributes.class);
		stock1.setPhotoDate(new Date(attr.lastModifiedTime().toMillis()));
		
		Photo stock2 = new Photo("data/stock2.jpeg");
		stockAlbum.addPhoto(stock2);
		File file2 = new File("data/stock2.jpeg");
		Path path2 = file2.toPath();
		BasicFileAttributes attr2 = Files.readAttributes(path2, BasicFileAttributes.class);
		stock2.setPhotoDate(new Date(attr2.lastModifiedTime().toMillis()));
		
		Photo stock3 = new Photo("data/stock3.jpeg");
		stockAlbum.addPhoto(stock3);
		File file3 = new File("data/stock3.jpeg");
		Path path3 = file3.toPath();
		BasicFileAttributes attr3 = Files.readAttributes(path3, BasicFileAttributes.class);
		stock3.setPhotoDate(new Date(attr3.lastModifiedTime().toMillis()));
		
		Photo stock4 = new Photo("data/stock4.jpeg");
		stockAlbum.addPhoto(stock4);
		File file4 = new File("data/stock4.jpeg");
		Path path4 = file4.toPath();
		BasicFileAttributes attr4 = Files.readAttributes(path4, BasicFileAttributes.class);
		stock4.setPhotoDate(new Date(attr4.lastModifiedTime().toMillis()));
		
		Photo stock5 = new Photo("data/stock5.jpeg");
		stockAlbum.addPhoto(stock5);
		File file5 = new File("data/stock5.jpeg");
		Path path5 = file5.toPath();
		BasicFileAttributes attr5 = Files.readAttributes(path5, BasicFileAttributes.class);
		stock5.setPhotoDate(new Date(attr5.lastModifiedTime().toMillis()));
		
		Photo stock6 = new Photo("data/stock6.jpeg");
		stockAlbum.addPhoto(stock6);
		File file6 = new File("data/stock6.jpeg");
		Path path6 = file6.toPath();
		BasicFileAttributes attr6 = Files.readAttributes(path6, BasicFileAttributes.class);
		stock6.setPhotoDate(new Date(attr6.lastModifiedTime().toMillis()));

		
		
	}
	
}