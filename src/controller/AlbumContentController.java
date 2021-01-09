package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;

import java.util.Set;

import java.util.StringTokenizer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserDB;
/**
 * this class controls the albumcontent view
 * this view is called albumcontent.fxml
 * this controls the listView of custom cells and updates the labels
 *
 */
public class AlbumContentController {
	/**
	 * private fields for the albumContentController
	 * 
	 */
	private User user;
	private UserDB userDB;
	private ObservableList<Photo> obsList;
	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private Album album;
	
	/**
	 * FXML classes that the controller needs
	 */
	@FXML Label photoNumLabel;
	@FXML Label dateRangeLabel;
	@FXML Label albumNameLabel;
	@FXML Button logoutButton;
	@FXML Button quitButton;
	@FXML Button addPhotoButton;
	@FXML Button searchButton;
	@FXML Button allAlbumsButton;
	@FXML Button clearSearchButton;
	@FXML Button resultsAlbumButton;
	@FXML Button displayPhotoButton;
	@FXML TextField tagFilterTextField;
	@FXML DatePicker datePickerStart;
	@FXML DatePicker datePickerEnd;
	@FXML ListView<Photo> listView;
	
	/**
	 * loads all the photos in the album in to the ListView
	 * and sets the labels to their correct values
	 * @param stage	 the stage
	 * @throws IOException	exception for initializing
	 */
	public void start(Stage stage) throws IOException {
		photos = album.getPhotosList();
		obsList = FXCollections.observableArrayList(photos);
		listView.setItems(obsList);
		listView.setCellFactory(photoListView -> new PhotoListCell());
		albumNameLabel.setText(album.getName());
		if(photos.size() != 0) {
			dateRangeLabel.setText(album.getDateRangeString());
		}else {
			dateRangeLabel.setText("Empty Album");
		}
		
		photoNumLabel.setText(Integer.toString(album.getPhotosList().size()));
		//auto select the first model
		listView.getSelectionModel().select(0);
	}
	
	/**
	 * setter for setting the album
	 * @param a  the passed album
	 */
	public void setAlbum(Album a) {
		this.album = a;
	}
	
	/**
	 * setter for setting the user
	 * @param u  the passed user
	 */
	public void setUser (User u) {
		this.user = u;
	}
	
	/**
	 * setter for setting the userDB
	 * @param users the passed userDB
	 */
	public void setUserDB(UserDB users) {
		this.userDB = users;
	}
	
	/**
	 * method for updating the labels
	 */
	private void updateLabels() {
		photoNumLabel.setText(Integer.toString(album.getPhotosList().size()));
		if(photos.size() != 0) {
			dateRangeLabel.setText(album.getDateRangeString());
		}else {
			dateRangeLabel.setText("Empty Album");
		}
	}
	
	/**
	 * handleAddPhotoButton allows the user to add photos from their machine
	 * @param event  on click
	 * @throws IOException exception for adding photo
	 */
	@FXML
	private void handleAddPhotoButton(ActionEvent event) throws IOException {
		// File chooser to get new photo to add to album, and obsList
		Parent root;
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		File photoFile = fileChooser.showOpenDialog(stage);
		FileInputStream inputStream = new FileInputStream(photoFile);
		String photoPath = photoFile.getAbsolutePath();
		
		// First check if photoPath already exists
		if(user.photoExists(photoPath)) {
			Photo temp = user.getPhotoFromPath(photoPath);
			if(temp != null) {
				// It exists already, make sure its not already in this album
				if(album.isPhotoInAlbum(photoPath)) {
					Alert emptyField = new Alert(AlertType.ERROR);
					emptyField.initOwner(window);
					emptyField.setTitle("Error: Photo Already In Album");
					emptyField.setHeaderText("The photo you are attempting to add is already in the album");
					emptyField.showAndWait();
					return;
				}
				
				// Add the reference to the existing photo, write and return
				album.addPhoto(temp);
				obsList.add(temp);
				updateLabels();
				UserDB.writeUsers(userDB);
				//System.out.println("WROTE OUT - " + userDB);
				return;
			}
		}else {
			// Photo doesnt exist already, continue
		}
		
		
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
		
		Image image = new Image(inputStream);
		
		
		Path path = photoFile.toPath();
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		
		
		Photo photo = new Photo(photoPath);
		photo.setPhotoDate(new Date(attr.lastModifiedTime().toMillis()));
		album.addPhoto(photo);
		
		
		
		obsList.add(photo);
		updateLabels();
		UserDB.writeUsers(userDB);
		//System.out.println("WROTE OUT - " + userDB);
	}
	
	
	/**
	 * handleAllAlbumsButton allows the user to return to the albums view
	 * where they can view all of their albums
	 * @param event  on click
	 * @throws FileNotFoundException  exception for returning to main album view
	 * @throws ClassNotFoundException  exception for returning to main album view
	 * @throws IOException  exception for returning to main album view
	 */
	@FXML
	private void handleAllAlbumsButton(ActionEvent event) throws FileNotFoundException, ClassNotFoundException, IOException {
		// go back to albumView
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albums.fxml"));
		Parent root = (Parent) loader.load();
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
	 * handleLogoutButton allows for the user to logout and return back to the login screen
	 * @param event   on click
	 * @throws FileNotFoundException  exception for returning back to the login screen
	 * @throws ClassNotFoundException  exception for returning back to the login screen
	 * @throws IOException  exception for returning back to the login screen
	 */
	@FXML
	private void handleLogoutButton(ActionEvent event) throws FileNotFoundException, ClassNotFoundException, IOException {
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
	 * handleQuitButton allows for the user to  quit the app
	 * @param event  on click
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
	 * handleDisplayPhotoButton allows for the user to go to the display photo view,
	 * where the image is displayed larger and in a slideshow view
	 * @param event  on click
	 * @throws IOException exception for moving to the display photo view
	 */
	@FXML
	private void handleDisplayPhotoButton(ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		// -> display photo slideshow
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/displayPhoto.fxml"));
		root = (Parent) loader.load();
		DisplayPhotoController controller = loader.getController();
		controller.setUser(user);
		controller.setUserDB(userDB);
		controller.setAlbum(album);
		//this get the selected model
		controller.setPhoto(listView.getSelectionModel().getSelectedItem());
		//This index should be same as currently selected photo in arraylist
		int index = listView.getSelectionModel().getSelectedIndex();
		
		if(obsList.isEmpty()) {
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("Display Error");
			emptyField.setHeaderText("Error: No photos in album to display");
			emptyField.showAndWait();
			return;
		}
		
		if(index == -1) {
			// No currently selected index if -1, default to first
			index = 0;
		}
		controller.setIndex(index);
		Scene adminScene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		controller.start(stage);
		stage.setScene(adminScene);
		stage.show();
	}
	
	/**
	 * this inner class allows for control of a custom listCell inside the 
	 * listView of albumContent
	 */
	public class PhotoListCell extends ListCell<Photo>{
		/**
		 * fxml classes to be used in the controller
		 */
		@FXML Button editTagButton;
		@FXML Button editCaptionButton;
		@FXML Button deleteButton;
		@FXML Button moveButton;
		@FXML Button copyButton;
		@FXML Label dateLabel;
		@FXML Label captionLabel;
		@FXML ImageView imageView;
		@FXML AnchorPane anchorPane;
		@FXML ListView<Tag> tagListView;
		private FXMLLoader loader;
		private ObservableList<Tag> tagObsList;
		
		/**
		 * updates the caption, and tag listView
		 */
		public void updateTags() {
			captionLabel.setText(this.getItem().getCaption());
			//tagLabel.setText(this.getItem().getStringTags());
			// Set tagListView
			ArrayList<Tag> tagList = this.getItem().getTags();
			tagObsList = FXCollections.observableArrayList(tagList);
			tagListView.setItems(tagObsList);
		}
		/**
		 * overrided method that is made to be our custom listCell
		 */
		@Override
	    protected void updateItem(Photo photo, boolean empty) {
	        super.updateItem(photo, empty);

	        if(empty || photo == null) {

	            setText(null);
	            setGraphic(null);

	        } else {
	            if (loader == null) {
	                loader = new FXMLLoader(getClass().getResource("/view/photoPane.fxml"));
	                loader.setController(this);

	                try {
	                    loader.load();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }

	            }
	            try {
					imageView.setImage(photo.getImage());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					InputStream is = null;
					try {
						is = new FileInputStream("photos/notfound.jpg");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Image notFound = new Image(is);
					imageView.setImage(notFound);
				}
				//captionLabel.setText(photo.getCaption());
				//tagLabel.setText(photo.getStringTags());
	            updateTags();
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				dateLabel.setText(dateFormat.format(this.getItem().getPhotoDate()));
				
				//how are we doing date range should we keep track of oldest and youngest photo data?
				editTagButton.setVisible(true);
				deleteButton.setVisible(true);
				editCaptionButton.setVisible(true);

	            setText(null);
	            setGraphic(anchorPane);
	        }

	    }
		/**
		 * handleDeleteButton allows for the user to delete the selected photo
		 * @param event  on click
		 * @throws FileNotFoundException exception for deleting the photo
		 * @throws IOException  exception for deleting the photo
		 */
		@FXML
		private void handleDeleteButton(ActionEvent event) throws FileNotFoundException, IOException {
			Node source = (Node) event.getSource();
			Window window = source.getScene().getWindow();
			
			// Alert to confirm delete
			Alert confirm = new Alert(AlertType.CONFIRMATION);
			confirm.initOwner(window);
			confirm.setTitle("Delete Confirmation");
			confirm.setHeaderText("Are you sure you want to delete this photo?");
			
			Optional<ButtonType> result = confirm.showAndWait();
			
			if(result.get() == ButtonType.OK) {
				photos.remove(this.getItem());
				obsList.remove(this.getItem());
				updateLabels();
				UserDB.writeUsers(userDB);
				//System.out.println("WROTE OUT - " + userDB);
			}else {
				// User cancelled, do nothing
			}
			
		}
		/**
		 * handleEditTagsButton allows for the user to edit the tag, which will bring them to a popup menu
		 * where they can create,edit or delete tags
		 * @param event on click
		 * @throws IOException exception for editting tags
		 */
		@FXML
		private void handleEditTagButton(ActionEvent event) throws IOException {
			// Open edit tag view for photo ->
			// do we need to set current album photo from in edit tag controller so we can go back to 
			// that albums contents when done?
			Parent root;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editTags.fxml"));
			root = (Parent) loader.load();
			EditTagController controller = loader.getController();
			controller.setPhoto(this.getItem());
			controller.setUsers(userDB);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("Edit Tags");
			controller.start(stage);
			stage.setScene(scene);
			stage.showAndWait();
			
			updateTags();
			
			
		}
		
		/**
		 * handleEditCaptionButton allows for the user to edit the caption, which will popup a
		 * view where they can enter the new caption.
		 * @param event on click 
		 * @throws IOException exception for editting caption
		 */
		@FXML
		private void handleEditCaptionButton(ActionEvent event) throws IOException {
			// -> Edit caption? Thorug textinputdialog or custom fxml window?
			Parent root;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editCaption.fxml"));
			root = (Parent) loader.load();
			EditCaptionController controller = loader.getController();
			controller.setPhoto(this.getItem());
			controller.setUserDB(userDB);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle("Edit Caption");
			controller.start(stage);
			stage.setScene(scene);
			stage.showAndWait();
			
			updateTags();
		}		
		
		/**
		 * handleMoveButton allows for the user to move the selected photo to 
		 * a different album using a view
		 * @param event on click
		 * @throws IOException exception for moving photo
		 */
		@FXML
		private void handleMoveButton(ActionEvent event) throws IOException {
			Parent root;
			Node source = (Node) event.getSource();
			Window window = source.getScene().getWindow();

			if(user.getAlbumList().size() <= 1) {
				// error no other albums to copy to
				Alert noAlbums = new Alert(AlertType.ERROR);
				noAlbums.initOwner(window);
				noAlbums.setTitle("No Other Albums");
				noAlbums.setHeaderText("Error: No other albums to copy to, make another album for photo destination.");
				noAlbums.showAndWait();
			}else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/movePhoto.fxml"));
				root = (Parent) loader.load();
				MovePhotoController controller = loader.getController();
				controller.setPhoto(this.getItem());
				controller.setUserDB(userDB);
				controller.setUser(user);
				controller.setAlbum(album);
				controller.setObsList(obsList);
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle("Move Photo");
				controller.start(stage);
				stage.setScene(scene);
				stage.showAndWait();
				
			}
		}
		/**
		 * handleCopyButton allows for the user to copy a photo to a different album, using a view
		 * @param event on click
		 * @throws IOException exception for copying
		 */
		@FXML
		private void handleCopyButton(ActionEvent event) throws IOException {
			Parent root;
			Node source = (Node) event.getSource();
			Window window = source.getScene().getWindow();

			if(user.getAlbumList().size() <= 1) {
				// error no other albums to copy to
				Alert noAlbums = new Alert(AlertType.ERROR);
				noAlbums.initOwner(window);
				noAlbums.setTitle("No Other Albums");
				noAlbums.setHeaderText("Error: No other albums to copy to, make another album for photo destination.");
				noAlbums.showAndWait();
			}else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/copyPhoto.fxml"));
				root = (Parent) loader.load();
				CopyPhotoController controller = loader.getController();
				controller.setPhoto(this.getItem());
				controller.setUserDB(userDB);
				controller.setUser(user);
				controller.setAlbum(album);
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle("Copy Photo");
				controller.start(stage);
				stage.setScene(scene);
				stage.showAndWait();
			}

		}
		
	}
	
	
}
