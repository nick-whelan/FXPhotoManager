package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.User;
import model.UserDB;

/**
 * 
 * Class that controls the admin view
 * This view is called admin.fxml
 * This controls the listView of Users and the buttons for deleting
 * and creating users in the admin.fxml
 * 
 */

public class AdminController {
	/**
	 * fxml classes for the controller
	 */
	@FXML Button logoutButton;
	@FXML Button quitButton;
	@FXML TextField usernameTextField;
	@FXML Button createUserButton;
	@FXML Button deleteButton;
	@FXML ListView<User> listView;
	
	/**
	 * observableList for holding the Users
	 * UserDB is an object that is serializable and is containing an list of users 
	 */
	private ObservableList<User> obsList;
	private UserDB users;
	
	/**
	 * loads all the users into the listView
	 * @param primaryStage 	the main stage
	 * @throws FileNotFoundException    exception for datfile not found
	 * @throws IOException    exception for reading the list of users
	 * @throws ClassNotFoundException   exception for reading the list of users
	 */ 
	public void start(Stage primaryStage) throws FileNotFoundException, IOException, ClassNotFoundException {
		if(users != null) {
			obsList = FXCollections.observableArrayList(users.getUserList());
			listView.setItems(obsList);
		}
	}
	
	/**
	 * setter for UserDB
	 * @param users UserDB object to be set
	 */
	public void setUserDB(UserDB users) {
		this.users = users;
	}
	
	/**
	 * handleCreateUserButton when clicked allows users to 
	 * create a new user that is not a duplicate. 
	 * @param event		the button press event
	 * @throws FileNotFoundException	exception for writing and storing users
	 * @throws IOException		exception for input
	 */
	@FXML
	private void handleCreateUserButton(ActionEvent event) throws FileNotFoundException, IOException {
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		String username;
		
		// If field not empty
		if(!usernameTextField.getText().isEmpty()) {
			// Check not admin
			if(usernameTextField.getText().equalsIgnoreCase("admin")) {
				Alert emptyField = new Alert(AlertType.ERROR);
				emptyField.initOwner(window);
				emptyField.setTitle("User Error");
				emptyField.setHeaderText("Error: Cannot create admin user");
				emptyField.showAndWait();
				return;
			}
			
			if(users.containsUser(usernameTextField.getText())) {
				// User already exists, error
				Alert emptyField = new Alert(AlertType.ERROR);
				emptyField.initOwner(window);
				emptyField.setTitle("User Error");
				emptyField.setHeaderText("Error: User with name " + usernameTextField.getText() + " already exists.");
				emptyField.showAndWait();
				
			}else {
				// New user
				username = usernameTextField.getText();
				User user = new User(username);
				users.addUser(user);
				obsList.add(user);
				UserDB.writeUsers(users);
				//System.out.println("WROTE OUT - " + users);
			}
		}else {
			// Warning
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("Empty Field");
			emptyField.setHeaderText("Error: Must enter username");
			emptyField.showAndWait();
		}
	}
	/**
	 * handleLogoutButton allows the user to logout of their user and return
	 * to the login screen
	 * @param event 	click on logout button
	 * @throws IOException	exception for getting the view
	 * @throws ClassNotFoundException exception for getting the controller class
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
	 * handleDeleteButton allows for the admin to delete other users
	 * @param event 	on click
	 * @throws FileNotFoundException	  exception for deleting users
	 * @throws IOException	exception for deleting users
	 */
	@FXML
	private void handleDeleteButton(ActionEvent event) throws FileNotFoundException, IOException {
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		
		
		// If nothing selected
		if(listView.getSelectionModel().getSelectedItem() == null) {
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("No User");
			emptyField.setHeaderText("Error: No user selected, select a user to delete");
			emptyField.showAndWait();
			return;
		}
		
		User selectedUser = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		
		if(obsList.isEmpty()) {
			// List empty, do nothing
			return;
		}
		
		// Alert to confirm delete
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.initOwner(window);
		confirm.setTitle("Delete Confirmation");
		confirm.setHeaderText("Are you sure you want to delete user: "+ selectedUser +" ?");
		
		Optional<ButtonType> result = confirm.showAndWait();
		
		if(result.get() == ButtonType.OK) {
			obsList.remove(selectedUser);
			users.removeUser(selectedUser);
			UserDB.writeUsers(users);
			//System.out.println("WROTE OUT - " + users);
		}else {
			// User cancelled, do nothing
		}
		
		
	}
	/**
	 * handleQuitButton allows for the admin to quit the app
	 * @param event on click
	 */
	@FXML
	private void handleQuitButton(ActionEvent event) {
		// Save state? -> Quit application
		Stage stage = (Stage) quitButton.getScene().getWindow();
		//close the stage
		stage.close();
		//close the app
			Platform.exit();
	}
	
	
	
	
}
