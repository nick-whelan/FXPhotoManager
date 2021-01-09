package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import model.Album;
import model.User;
import model.UserDB;

/**
 * this class controls the albums view, where we can see all the albums a user has.
 * the albumsView is called albums.fxml
 * here we can create new albums, and search for photos.
 *
 */
public class AlbumController {
	/**
	 * fxml classes used in albumController
	 */
	@FXML Button logoutButton;
	@FXML Button quitButton;
	@FXML Button createAlbumButton;
	@FXML Button searchButton;
	@FXML ListView<Album> listView;

	/**
	 * fields used by albumController
	 */
	private ArrayList<Album> albums = new ArrayList<Album>();
	private User user;
	private ObservableList<Album> obsList;
	private UserDB userDB;
	
	/**
	 * loads all the user's albums into the listView
	 * @param primaryStage the main stage
	 * @throws FileNotFoundException exception for loading albums
	 * @throws IOException exception for loading albums
	 * @throws ClassNotFoundException exception for loading albums
	 */
	public void start(Stage primaryStage) throws FileNotFoundException, IOException, ClassNotFoundException {
		albums = user.getAlbumList();
		obsList = FXCollections.observableArrayList(albums);
		listView.setItems(obsList);
        listView.setCellFactory(albumListView -> new AlbumListCell());
	}
	/**
	 * getter for the ObservableList
	 * @return ObservableList
	 */
	public ObservableList<Album> getObsList() {
		return obsList;
	}
	
	/**
	 * removes an album
	 * @param album to be removed
	 */
	public void remove(Album album) {
		obsList.remove(album);
	}
	
	/**
	 * sets the user
	 * @param u user
	 */
	public void setUser (User u) {
		this.user = u;
	}
	
	/**
	 * handleSearchButton allows the user to go to the search view, where they can search for
	 * photos based on tags or date ranges.
	 * @param event  on click 
	 * @throws IOException exception for loading controller
	 */
	@FXML
	private void handleSearchButton(ActionEvent event) throws IOException {
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/search.fxml"));
		root = (Parent) loader.load();
		SearchController controller = loader.getController();
		// Also need to set / pass user in 
		controller.setUser(user);
		controller.setUserDB(userDB);
		Scene adminScene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		controller.start(stage);
		stage.setScene(adminScene);
		stage.show();
	}

	/**
	 * handleCreateAlbumButton allows the user to newAlbum view where they can create a new album.
	 * @param event  on click
	 * @throws IOException exception for loading controller
	 */
	@FXML
	private void handleCreateAlbumButton(ActionEvent event) throws IOException {
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/newAlbum.fxml"));
		root = (Parent) loader.load();
		NewAlbumController controller = loader.getController();
		// Also need to set / pass user in 
		controller.setUser(user);
		controller.setUserDB(userDB);
		Scene adminScene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		controller.start(stage);
		stage.setScene(adminScene);
		stage.show();
	}
	
	/**
	 * handleLogoutButton allows for the user to logout and return to the login screen
	 * @param event on click 
	 * @throws FileNotFoundException exception for logging out
	 * @throws ClassNotFoundException  exception for logging out
	 * @throws IOException exception for logging out
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
	 * handleQuitButton allows for the user to quit the application
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
	 * setter for USerDB
	 * @param users userDB
	 */
	public void setUserDB(UserDB users) {
		this.userDB = users;
	}
	
	/**
	 * this inner class allows for the listView in albums to be populated with
	 * custom listcells
	 */
	public class AlbumListCell extends ListCell<Album>{
		/**
		 * fxml classes used in AlbumListCell
		 */
		@FXML ImageView imageView;
		@FXML Button openAlbumButton;
		@FXML Button deleteButton;
		@FXML Button renameButton;
		@FXML Label nameLabel;
		@FXML Label photosLabel;
		@FXML Label dateRangeLabel;
		@FXML AnchorPane anchorPane;
		
		/**
		 * member used by the listCell
		 */
		private FXMLLoader loader;
		
		/**
		 * overridden method for our custom listcell
		 */
		@Override
	    protected void updateItem(Album album, boolean empty) {
	        super.updateItem(album, empty);

	        if(empty || album == null) {

	            setText(null);
	            setGraphic(null);

	        } else {
	            if (loader == null) {
	                loader = new FXMLLoader(getClass().getResource("/view/ListCell.fxml"));
	                loader.setController(this);

	                try {
	                    loader.load();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }

	            }
	            try {
					imageView.setImage(album.getAlbumCover());
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
				nameLabel.setText(album.getName());
				photosLabel.setText(String.valueOf(album.getSize()));
				if(album.getPhotosList().size() != 0) {
					dateRangeLabel.setText(album.getDateRangeString());
				}else {
					dateRangeLabel.setText("Empty Album");
				}
				
			
				//how are we doing date range should we keep track of oldest and youngest photo data?
				renameButton.setVisible(true);
				deleteButton.setVisible(true);
				openAlbumButton.setVisible(true);

	            setText(null);
	            setGraphic(anchorPane);
	        }

	    }
		
		/**
		 * handleOpenAlbumButton allows for us to open an album
		 * it brings us to the albumContent view, which is Albumcontent.fxml
		 * @param event  on click
		 * @throws IOException  exception for loading controller
		 */
		@FXML
		private void handleOpenAlbumButton(ActionEvent event) throws IOException {
			// Goes to albumContent view -> 
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumContent.fxml"));
			Parent root = (Parent) loader.load();
			AlbumContentController controller = loader.getController();
			controller.setAlbum(this.getItem());
			controller.setUser(user);
			controller.setUserDB(userDB);
			Scene albumContentScene = new Scene(root);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			controller.start(stage);
			stage.setScene(albumContentScene);
			stage.show();
		}
		
		/**
		 * handleDeleteButton allows the user to delete an album
		 * @param event  on click 
		 * @throws FileNotFoundException exception with deleting an album
		 * @throws IOException exception with deleting an album
		 * @throws ClassNotFoundException Class not found
		 */
		@FXML
		private void handleDeleteButton(ActionEvent event) throws FileNotFoundException, IOException, ClassNotFoundException {
			// To avoid weird nulls, reload current userDB
			//userDB = UserDB.loadUsers();
			
			Node source = (Node) event.getSource();
			Window window = source.getScene().getWindow();
			
			// Alert to confirm delete
			Alert confirm = new Alert(AlertType.CONFIRMATION);
			confirm.initOwner(window);
			confirm.setTitle("Delete Confirmation");
			confirm.setHeaderText("Are you sure you want to delete album: "+ this.getItem() + " ?");
			
			Optional<ButtonType> result = confirm.showAndWait();
			
			if(result.get() == ButtonType.OK) {
				albums.remove(this.getItem());
				obsList.remove(this.getItem());
				UserDB.writeUsers(userDB);
				//System.out.println("WROTE OUT - " + userDB);
				//System.out.println("DELETED?");
			}else {
				// User cancelled, do nothing
			}
		}
		
		/**
		 * handleRenameButton allows for the user to rename an album using a text dialog
		 * @param event on click
		 * @throws FileNotFoundException exception for renaming an album
		 * @throws IOException  exception for renaming an album
		 */
		@FXML
		private void handleRenameButton(ActionEvent event) throws FileNotFoundException, IOException {
			// Need dialog box with new name and save button, can we do this with alert?
			TextInputDialog renameDialog = new TextInputDialog();
			renameDialog.setTitle("Rename Album");
			renameDialog.getDialogPane().setContentText("Enter New Album Name");
			renameDialog.getDialogPane().setHeaderText("Rename Album:");
			Optional<String> result = renameDialog.showAndWait();
			TextField input = renameDialog.getEditor();
			
			if(input.getText() != null && input.getText().length() != 0) {
				this.getItem().renameAlbum(input.getText());
				UserDB.writeUsers(userDB);
				//System.out.println("WROTE OUT - " + userDB);
				// Without line below name label doesnt update until a item in obsList is selected and list refreshes
				this.nameLabel.setText(input.getText());
			}else {
				// Nothing entered
			}

		}
		
	}
	
}

