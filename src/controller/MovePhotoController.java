package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Album;
import model.Photo;
import model.User;
import model.UserDB;
/**
 * this controller is for the move Photo view
 * this view is called movePhoto.fxml
 *
 */
public class MovePhotoController {
	/**
	 * these are the fields for the controller
	 */
	private Photo photo;
	private User user;
	private UserDB users;
	private Album sourceAlbum;
	private ObservableList<Photo> obsList;
	
	/**
	 * these are the fxml classes used in the controller
	 */
	@FXML Button moveButton;
	@FXML Button cancelButton;
	@FXML ChoiceBox choiceBox;
	
	/**
	 * this is the start method that loads the other albums into the choice box
	 * @param stage Stage
	 */
	public void start(Stage stage) {
		ArrayList<Album> otherAlbums = new ArrayList<Album>(user.getAlbumList());
		otherAlbums.remove(sourceAlbum);
		ObservableList<Album> obsList = FXCollections.observableArrayList(otherAlbums);
		choiceBox.getItems().addAll(obsList);
	}
	
	/**
	 * setter for user
	 * @param u User
	 */
	public void setUser(User u) {
		this.user = u;
	}
	
	/**
	 * setter for UserDB
	 * @param db UserDB
	 */
	public void setUserDB(UserDB db) {
		this.users = db;
	}
	
	/**
	 * setter for Photo
	 * @param p Photo
	 */
	public void setPhoto(Photo p) {
		this.photo = p;
	}
	
	/**
	 * setter for album
	 * @param a album
	 */
	public void setAlbum(Album a) {
		this.sourceAlbum = a;
	}
	
	/**
	 * setter for observablelist
	 * @param ob observablelist
	 */
	public void setObsList(ObservableList<Photo> ob) {
		this.obsList = ob;
	}
	
	/**
	 * this method is for when the move button is clicked,
	 * and it will move the photo to the selected album
	 * @param event on click
	 * @throws FileNotFoundException exception for file not found
	 * @throws IOException exception for moving photo
	 */
	@FXML
	private void handleMoveButton(ActionEvent event) throws FileNotFoundException, IOException {
		Parent root;
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		
		if(choiceBox.getSelectionModel().getSelectedItem() != null) {
			Album destAlbum = (Album) choiceBox.getSelectionModel().getSelectedItem();
			destAlbum.addPhoto(photo);
			Set<Photo> resultSet = new HashSet<Photo>(destAlbum.getPhotosList());
			 destAlbum.setPhotosList(new ArrayList<Photo>(resultSet));
			
			sourceAlbum.deletePhoto(sourceAlbum.findIndexOfPhoto(photo));
			obsList.remove(photo);
		}else {
			Alert noSelection = new Alert(AlertType.ERROR);
			noSelection.initOwner(window);
			noSelection.setTitle("No Album Selected");
			noSelection.setHeaderText("Error: Must select a destination album for copy");
			noSelection.showAndWait();
		}
		
		UserDB.writeUsers(users);
		//System.out.println("WROTE OUT - " + users);
		
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
		
	}
	
	/**
	 * this method is for when the cancel button is pressed
	 * @param event on click
	 */
	@FXML
	private void handleCancelButton(ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
}
