package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.User;
import model.UserDB;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import model.Album;
import model.Photo;
import model.Tag;
/**
 * this is the controller for editting tags
 *
 */
public class EditTagController {
	/**
	 * these are the fxml classes used in the controller
	 */
	@FXML Button newTagButton;
	@FXML Button editTagButton;
	@FXML Button deleteTagButton;
	@FXML Button closeButton;
	@FXML Button saveButton;
	@FXML ListView<Tag> listView;
	
	/**
	 * these are the fields used in the controller
	 */
	private ObservableList<Tag> obsList;
	private UserDB users;
	private Photo photo;
	
	/**
	 * setter for userDB
	 * @param u userDB
	 */
	public void setUsers (UserDB u) {
		this.users = u;
	}
	
	/**
	 * setter for photo
	 * @param photo Photo
	 */
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
	/**
	 * update method that updates the listView and selects the first element
	 */
	public void updateTags() {
		ArrayList<Tag> test = photo.getTags();
		if(!test.isEmpty()) {//if arrayList not Empty we still get null ptrs
			obsList = FXCollections.observableArrayList(photo.getTags());
			listView.setItems(obsList);
			listView.getSelectionModel().select(0);
		}
	}

	/**
	 * the handleNewTagButton causes a popup where the user can create a new tag 
	 * @param event on click
	 * @throws IOException exception for loading the view
	 */
	@FXML
	private void handleNewTagButton(ActionEvent event) throws IOException {
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/newTag.fxml"));
		root = (Parent) loader.load();
		NewTagController controller = loader.getController();
		//controller.setUser(user);
		//controller.setAlbum(album);
		controller.setPhoto(photo);
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle("Edit Tags");
		controller.start(stage);
		stage.setScene(scene);
		stage.showAndWait();

		updateTags();
		
	}
	/**
	 * this method causes a popup to appear where the user can enter new name and value for a tag
	 * @param event on click
	 * @throws IOException exception for loading the controller
	 */
	@FXML
	private void handleEditTagButton(ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		if (obsList.isEmpty()) {
			// Popup dialog for editing empty tag
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(window);
			alert.setTitle("Error");
			alert.setHeaderText(
					"Editting error");

			String content = "Error: No tags to edit ";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		//bc the obsList is not empty we can select the first item
		//listView.getSelectionModel().select(0);
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editTagValue.fxml"));
		root = (Parent) loader.load();
		EditTagValueController controller = loader.getController();
		Tag item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		controller.setPhoto(photo);
		controller.setTag(item);
		controller.setIndex(index);
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle("Edit Tags");
		controller.start(stage);
		stage.setScene(scene);
		stage.showAndWait();
		
		updateTags();
	}
	
	/**
	 * this method is for deleting a tag
	 * @param event on click 
	 * @throws FileNotFoundException exception for when a file isnt found
	 * @throws IOException exception for deleting
	 */
	@FXML
	private void handleDeleteTagButton(ActionEvent event) throws FileNotFoundException, IOException {
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		Tag item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		if (obsList.isEmpty()) {
			// Popup dialog
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(window);
			alert.setTitle("Error");
			alert.setHeaderText("Deleting error");
			String content = "Error: Tag list is empty! ";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		// Alert to confirm delete
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.initOwner(window);
		confirm.setTitle("Delete Confirmation");
		confirm.setHeaderText("Are you sure you want to delete this photo?");
		
		Optional<ButtonType> result = confirm.showAndWait();
		
		if(result.get() == ButtonType.OK) {
			photo.removeTag(item);
			obsList.remove(item);
			UserDB.writeUsers(users);
			//System.out.println("WROTE OUT - " + users);
		}else {
			// User cancelled, do nothing
		}
		
	}
	
	/**
	 * the close button is for closing the edit tag popup
	 * @param event on click
	 */
	@FXML
	private void handleCloseButton(ActionEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}
	
	/**
	 * the save button is for saving the data of the user
	 * @param event event for click
	 * @throws FileNotFoundException If file cannot be written
	 * @throws IOException If file cannot be written
	 */
	@FXML
	private void handleSaveButton(ActionEvent event) throws FileNotFoundException, IOException {
		UserDB.writeUsers(users);
		//System.out.println("WROTE OUT - " + users);
		Stage stage = (Stage) saveButton.getScene().getWindow();
		stage.close();
	}
	
	/**
	 * populates the listview with tags
	 * @param stage Stage
	 */
	public void start(Stage stage) {
		// TODO Auto-generated method stub
		//check for null and empty array list
		//photo.getTags();
		//if(!photo.getTags().isEmpty()) {//if the arrayList is not empty we still get nullPtrs
			obsList = FXCollections.observableArrayList(photo.getTags());
			listView.setItems(obsList);
			listView.getSelectionModel().select(0);
		//}
		
	}
}
