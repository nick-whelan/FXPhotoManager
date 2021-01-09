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
 * this class is the controller for the copying photos view
 * this view is called copyPhoto.fxml
 *
 */
public class CopyPhotoController {
	/**
	 * fields for the controller
	 */
	private Photo photo;
	private User user;
	private UserDB users;
	private Album sourceAlbum;
	/**
	 * fxml classes that the controller uses
	 */
	@FXML Button copyButton;
	@FXML Button cancelButton;
	@FXML ChoiceBox<Album> choiceBox;
	
	/**
	 * loads the dest albums and the listviews for them
	 * @param stage mainstage
	 */
	public void start(Stage stage) {
		ArrayList<Album> otherAlbums = new ArrayList<Album>(user.getAlbumList());
		otherAlbums.remove(sourceAlbum);
		ObservableList<Album> obsList = FXCollections.observableArrayList(otherAlbums);
		choiceBox.getItems().addAll(obsList);
	}
	
	/**
	 * sets the user
	 * @param u user
	 */
	public void setUser(User u) {
		this.user = u;
	}
	
	/**
	 * sets the userDB
	 * @param db userDB
	 */
	public void setUserDB(UserDB db) {
		this.users = db;
	}
	
	/**
	 * sets the photo
	 * @param p photo
	 */
	public void setPhoto(Photo p) {
		this.photo = p;
	}
	
	/**
	 * sets the album
	 * @param a album
	 */
	public void setAlbum(Album a) {
		this.sourceAlbum = a;
	}
	
	/**
	 * handleCopyButton allows us to copy a photo to a diff album
	 * @param event on click
	 * @throws FileNotFoundException exception for copying photo
	 * @throws IOException exception for copying photo
	 */
	@FXML
	private void handleCopyButton(ActionEvent event) throws FileNotFoundException, IOException {
		
		Parent root;
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		
		if(choiceBox.getSelectionModel().getSelectedItem() != null) {
			Album destAlbum = (Album) choiceBox.getSelectionModel().getSelectedItem();
			destAlbum.addPhoto(photo);
			Set<Photo> resultSet = new HashSet<Photo>(destAlbum.getPhotosList());
			 destAlbum.setPhotosList(new ArrayList<Photo>(resultSet));
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
	 * handleCancelButton allows the user to back out of copying
	 * @param event on click
	 */
	@FXML
	private void handleCancelButton(ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
}
