package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Album;
import model.Photo;
import model.User;
import model.UserDB;
/**
 * this class is the controller for the CreateNewAlbum view
 * this view is called newAlbum
 *
 */
public class NewAlbumController {
	/**
	 * these are the fxml classes used in the controller
	 */
	@FXML Button cancelButton;
	@FXML Button uploadButton;
	@FXML Button logoutButton;
	@FXML Button quitButton;
	@FXML Button createButton;
	@FXML TextField albumNameTextField;
	@FXML TilePane albumTilePane;
	@FXML ScrollPane albumScrollPane;
	
	/**
	 * these are the fields used in the controller
	 */
	private User user;
	ArrayList<Image> images = new ArrayList<Image>();
	ArrayList<String> paths = new ArrayList<String>();
	ArrayList<Date> photoDates = new ArrayList<Date>();
	private UserDB userDB;
	
	/**
	 * this is the start method
	 * @param primaryStage Stage
	 */
	public void start(Stage primaryStage) {
		
	}
	
	/**
	  * this is the setter method for user
	  * @param u User
	  */
	public void setUser(User u) {
		this.user = u;
	}
	
	/**
	 * this method creates a new album and returns us to the albums view
	 * @param event on click
	 * @throws FileNotFoundException exception for if a file is not found
	 * @throws ClassNotFoundException for if a class is not found
	 * @throws IOException exception for creating a new album
	 */
	@FXML
	private void handleCreateButton(ActionEvent event) throws FileNotFoundException, ClassNotFoundException, IOException {
		Parent root;
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		

		if(albumNameTextField.getText().isEmpty()) {
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("No Album Name");
			emptyField.setHeaderText("Error: Must enter album name for creation");
			emptyField.showAndWait();
			return;
		}
		
		// Check if album name already exists
		if(user.albumNameExists(albumNameTextField.getText())) {
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("Album Name Exists");
			emptyField.setHeaderText("Error: Album name entered already exists");
			emptyField.showAndWait();
			return;
		}
		
		// Need to take all images in arraylist, convert to photos and add to album
		String albumName = null;
		
		if(!albumNameTextField.getText().isEmpty()) {
			albumName = albumNameTextField.getText();
		}
		
		// add the album
		user.addAlbum(albumName);
		Album currentAlbum = user.getAlbumList().get(user.getAlbumList().size() - 1);
		
		// Go through all paths, first check if paths exists, if true -> get photo from path and add photo
		// If false -> set photo date, and add photo.
		for(int i = 0; i < paths.size(); i++) {
			if(user.photoExists(paths.get(i))) {
				// True, get the photo from path and add it to album
				Photo temp = user.getPhotoFromPath(paths.get(i));
				if(temp != null) { // Null check on getPhotoFromPath
					currentAlbum.addPhoto(temp);
				}
			}else {
				// Photo doesnt exist
				Photo photo = new Photo(paths.get(i));
				photo.setPhotoDate(photoDates.get(i));
				currentAlbum.addPhoto(photo);
			}
			
		}
		
		// Before we write to userDB, get the current albums photolist and convert to hashset so we dont have duplicates  in album
		Set<Photo> photoSet = new HashSet<Photo>(currentAlbum.getPhotosList());
		currentAlbum.setPhotosList(new ArrayList<Photo>(photoSet));
		
		
		
		// Now need to write out to UserDB to save TODO (check if this saves!) not saving
		UserDB.writeUsers(userDB);
		//System.out.println("WROTE OUT - " + userDB);
		
		// Now go back to albums view -> TODO should we confirm create album first?
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albums.fxml"));
		root = (Parent) loader.load();
		AlbumController albumController = loader.getController();
		// Run a method like setActiveUser, that takes a parameter username and sets it as a field. 
		albumController.setUser(user);
		albumController.setUserDB(userDB);
		Scene albumScene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		albumController.start(stage);
		stage.setScene(albumScene);
		stage.show();
		
	}
	
	/**
	 * this method is for when the cancel button is clicked,
	 * it returns us to the albums view
	 * @param event on click
	 * @throws IOException exception for cancel button
	 * @throws ClassNotFoundException exception for cancel button
	 */
	@FXML
	private void handleCancelButton(ActionEvent event) throws IOException, ClassNotFoundException {
		Parent root;
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		// Back to albums ->
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albums.fxml"));
		root = (Parent) loader.load();
		AlbumController albumController = loader.getController();
		// Run a method like setActiveUser, that takes a parameter username and sets it as a field. 
		Scene albumScene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		albumController.setUser(user);
		albumController.start(stage);
		stage.setScene(albumScene);
		stage.show();
	}
	
	/**
	 * this method is used for uploading photos
	 * @param event on click 
	 * @throws IOException exception for uploading files
	 */
	@FXML
	private void handleUploadButton(ActionEvent event) throws IOException {
		Parent root;
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		
		// Need file chooser -> going to get upload and display in image view working first,
		// Then work on saving image in ImageSerializer.
		FileChooser fileChooser = new FileChooser();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		File photoFile = fileChooser.showOpenDialog(stage);
		String photoPath = photoFile.getAbsolutePath();
		FileInputStream inputStream = new FileInputStream(photoFile);
		
		// Check file type
		// JavaFX Image can be [BMP, GIF, JPEG, PNG]
		
		String fileName = photoFile.getName();
		int index = fileName.lastIndexOf('.');
		String extension = fileName.substring(index + 1);
		
		ArrayList<String> validTypes = new ArrayList<String>();
		validTypes.add("jpg");
		validTypes.add("jpeg");
		validTypes.add("gif");
		validTypes.add("png");
		validTypes.add("bmp");
		
		if(!validTypes.contains(extension)) {
			// If extension is not in valid types
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("Invalid Photo Format");
			emptyField.setHeaderText("Error: Image must be in one of the follwing formats: [BMP, GIF, JPEG, PNG]");
			emptyField.showAndWait();
			return;
		}
		
		if(inputStream.equals(null)) {
			inputStream.close();
			return;
		}
		
		//Image image = new Image(inputStream);
		InputStream is = new FileInputStream(photoPath);
		Image image = new Image(is);
		
		albumTilePane.getChildren().add(generateImageView(photoFile));
		
		images.add(image);
		paths.add(photoPath);
		
		//Also need to add last modified fileTime when uploading as Date
		Path path = photoFile.toPath();
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		//fileTimes.add(attr.lastModifiedTime());
		photoDates.add(new Date(attr.lastModifiedTime().toMillis()));
		 		
		
	}
	
	/**
	 * this method is for generating the image view
	 * @param imageFile File
	 * @return ImageView
	 * @throws IOException exception for generating imageView
	 */
	private ImageView generateImageView(File imageFile) throws IOException {
		// Also adds image to images
		ImageView imageView = null;
		Image image;
		FileInputStream inputStream = new FileInputStream(imageFile);
		image = new Image(inputStream);
		imageView = new ImageView(image);
		imageView.setFitWidth(200);
		imageView.setFitHeight(200);
		inputStream.close();
		
		return imageView;
	}
	
	/**
	 * this method is for logging out of the user
	 * @param event on click 
	 * @throws IOException exception for returning to the login screen
	 * @throws ClassNotFoundException exception for returning to the login screen
	 */
	@FXML
	private void handleLogoutButton(ActionEvent event) throws IOException, ClassNotFoundException {
		// Save state -> back to login Scene
				Parent root;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
				root = (Parent) loader.load();
				LoginController controller = loader.getController();
				Scene adminScene = new Scene(root);
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				controller.start(stage);
				stage.setScene(adminScene);
				stage.show();
	}
	
	/**
	 * this method is for when the quit button is clicked. Quits out the app
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
	 * setter for UserDB
	 * @param users UserDB
	 */
	public void setUserDB(UserDB users) {
		this.userDB = users;
	}
	
	
	
}
