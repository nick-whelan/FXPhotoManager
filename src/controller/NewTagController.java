package controller;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
/**
 * this class is the controller for the new tag view
 * this view is called newTag.fxml
 *
 */
public class NewTagController {
	
	/**
	 * these are the fxml classes used in the class
	 */
	@FXML Button cancelButton;
	@FXML Button saveButton;
	@FXML TextField tagNameTextField;
	@FXML TextField tagValueTextField;

	/**
	 * these are the fields used in the controller
	 */
	public static Tag tempTag;
	private Photo photo;
	
	/**
	 * this is the setter method for photo
	 * @param photo Photo
	 */
	public void setPhoto(Photo photo) {
		this.photo =photo;
	}
	
	/**
	 * this is the method for when the cancel button is pressed
	 * @param event on click
	 */
	@FXML
	private void handleCancelButton(ActionEvent event){
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
	/**
	 * this is the method for when the save button is pressed
	 * saves the new tag to the selected photo
	 * @param event on click
	 */
	@FXML
	private void handleSaveButton(ActionEvent event){
		//adds tags to a photo but im not sure if it saves
		String name;
		String value;
		
		Parent root;
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		
		//if both fields not empty
		if(!tagNameTextField.getText().isEmpty() && !tagValueTextField.getText().isEmpty()) {
			//create a tag for comparisons
			name = tagNameTextField.getText();
			value = tagValueTextField.getText();
			Tag tempTag = new Tag(name,value);
		
			if(photo.duplicatesExist(tempTag)) {
				Alert emptyField = new Alert(AlertType.ERROR);
				emptyField.initOwner(window);
				emptyField.setTitle("Tag already exist");
				emptyField.setHeaderText("Error: That tag already exist");
				emptyField.showAndWait();
			}
			else {
				photo.addTag(tempTag);
				
					//closes the new tag popup
					Stage stage = (Stage) saveButton.getScene().getWindow();
					stage.close();
					
			}
			
			
		
		
		}
		//if one or both fields are empty
		else {
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("Empty Name and Value Field");
			emptyField.setHeaderText("Error: Must enter both a Name and Vaue");
			emptyField.showAndWait();
		}
	}

	/**
	 * the start method
	 * @param stage Stage
	 */
	public void start(Stage stage) {
		// TODO Auto-generated method stub
		
	}

}
