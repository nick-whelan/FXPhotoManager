package controller;

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
import model.Photo;
import model.Tag;
/**
 * this class is the controller for the edittagvalue view
 * this view is called editTagValue.fxml
 *
 */
public class EditTagValueController {
	/**
	 * these are the fxml classes used in the controller
	 */
	@FXML Button cancelButton;
	@FXML Button saveButton;
	@FXML TextField tagNameTextField;
	@FXML TextField tagValueTextField;
	
	/**
	 * these are the fields used in the controller
	 */
	private Tag tag;
	private Photo photo;
	private int index;
	
	/**
	 * setter for the tag being editted
	 * @param tag Tag
	 */
	public void setTag(Tag tag) {
		this.tag =tag;
	}
	
	/**
	 * setter for the photo with the tag
	 * @param photo Photo
	 */
	public void setPhoto(Photo photo) {
		this.photo =photo;
	}
	
	/**
	 * setter for the index
	 * @param index Int
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * method for the cancel button, closes the stage
	 * @param event on click
	 */
	@FXML
	private void handleCancelButton(ActionEvent event){
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
	/**
	 * method for the save button, saves the data
	 * @param event Event for button click
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
			name = tagNameTextField.getText().trim();
			value = tagValueTextField.getText().trim();
			Tag tempTag = new Tag(name,value);
			
			if(tag.equals(tempTag)){
				// Edit button was hit but no changes were made, pop up dialog 
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initOwner(window);
				alert.setTitle("No Edits");
				alert.setHeaderText("No edits were made");
				String content = "Select OK to dismiss";
				alert.setContentText(content);
				alert.showAndWait();
			}
			else if(photo.duplicatesExist(tempTag)) {
				//if tag already exist
				Alert emptyField = new Alert(AlertType.ERROR);
				emptyField.initOwner(window);
				emptyField.setTitle("Tag already exist");
				emptyField.setHeaderText("Error: That tag already exist");
				emptyField.showAndWait();
			}
			else {
				//we set the tag values
				//tag.setName(name);
				//tag.setValue(value);
				photo.editTag(index, name, value);
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
	 * start method
	 * @param stage Stage
	 */
	public void start(Stage stage) {
		// TODO Auto-generated method stub
		
	}
}
