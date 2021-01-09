package controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Photo;
import model.UserDB;
/**
 * this class is for controlling the edit caption view
 *
 */
public class EditCaptionController {
	
	/**
	 * fields for the class
	 */
	private Photo photo;
	private UserDB users;
	
	/**
	 * fxml classes used by the controller
	 */
	@FXML TextArea textArea;
	@FXML Button saveButton;
	@FXML Button cancelButton;
	
	/**
	 * start method where the photo's caption is set into the textArea
	 * @param stage main stage
	 */
	public void start(Stage stage) {
		textArea.setText(photo.getCaption());
	}
	
	/**
	 * setter for photo
	 * @param p photo
	 */
	public void setPhoto(Photo p) {
		this.photo = p;
	}
	
	/**
	 * setter for UserDB
	 * @param users UserDB
	 */
	public void setUserDB(UserDB users) {
		this.users = users;
	}
	
	/**
	 * handleSaveButton allows for the user to save the caption
	 * @param event on click
	 * @throws FileNotFoundException exception for when the file isnt found
	 * @throws IOException exception for saving
	 */
	@FXML
	private void handleSaveButton(ActionEvent event) throws FileNotFoundException, IOException {
		photo.setCaption(textArea.getText());
		UserDB.writeUsers(users);
		//System.out.println("WROTE OUT - " + users);
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
	/**
	 * handleCancelButton allows for the user to cancel out of the edit caption view
	 * @param event on click
	 */
	@FXML
	private void handleCancelButton(ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
}
