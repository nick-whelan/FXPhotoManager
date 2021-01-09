package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserDB;
/**
 * this class controls the display photos view
 * this view is called displayphoto.fxml
 *
 */
public class DisplayPhotoController {
	/**
	 * these are the fields used in the controller
	 */
	private User user;
	private UserDB userDB;
	private ObservableList<Photo> obsList;
	private ObservableList<Tag> obsTagList;
	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private Album album;
	private Photo currentPhoto;
	private int index;
	
	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy h:mm a");
	/**
	 * these are the fxml classes for the controller
	 */
	@FXML ImageView imageView;
	@FXML Button leftButton;
	@FXML Button rightButton;
	@FXML Button editCaptionButton;
	@FXML Button editTagButton;
	@FXML Button closeButton;
	@FXML Label captionLabel;
	@FXML Label dateLabel;
	@FXML ListView tagListView;
	
	/**
	 * loads all the photos into the display view
	 * @param stage the main stage
	 * @throws FileNotFoundException this exception is for loading files
	 */
	public void start(Stage stage) throws FileNotFoundException {
		
		// Set images and labels
		photos = album.getPhotosList();
		try {
			imageView.setImage(photos.get(index).getImage());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			InputStream is = new FileInputStream("photos/notfound.jpg");
			Image notFound = new Image(is);
			imageView.setImage(notFound);
		}
		centerImage();
		updateLabels();

	}
	
	/**
	 * this is a setter for albums
	 * @param a album
	 */
	public void setAlbum(Album a) {
		this.album = a;
	}
	
	/**
	 * this is a setter for photo
	 * @param p photo
	 */
	public void setPhoto(Photo p) {
		this.currentPhoto = p;
	}
	
	/**
	 * this is a setter for user
	 * @param u user
	 */
	public void setUser(User u) {
		this.user = u;
	}
	
	/**
	 * this is a setter for UserDB
	 * @param users UserDB
	 */
	public void setUserDB(UserDB users) {
		this.userDB = users;
	}
	
	/**
	 * this is a setter for the index
	 * @param index int
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * this is a update method for the labels in the view
	 */
	public void updateLabels() {
		dateLabel.setText(dateFormat.format(photos.get(index).getPhotoDate()));
		captionLabel.setText(photos.get(index).getCaption());
		ArrayList<Tag> tagList = photos.get(index).getTags();
		obsTagList = FXCollections.observableArrayList(tagList);
		tagListView.setItems(obsTagList);
	}
	
	/**
	 * this is for the left button that allows uses to view the photo in front of the current photo
	 * @param event on click
	 * @throws FileNotFoundException exception for viewing previous photo
	 */
	@FXML
	private void handleLeftButton(ActionEvent event) throws FileNotFoundException {
		//Photo prevPhoto = album.getPreviousPhoto(currentPhoto);
		//currentPhoto = prevPhoto;
		// Check if going down one is negative, if so stay where we are
		if(index - 1 < 0) {
			index = index;
		}else {
			index = index - 1;
		}
		try {
			imageView.setImage(photos.get(index).getImage());
			currentPhoto = photos.get(index);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			InputStream is = new FileInputStream("photos/notfound.jpg");
			Image notFound = new Image(is);
			imageView.setImage(notFound);
		}
		centerImage();
		updateLabels();
	}
	/**
	 * this is for a button that lets users view the next photo in the album
	 * @param event on click
	 * @throws FileNotFoundException exception for viewing the next photo
	 */
	@FXML
	private void handleRightButton(ActionEvent event) throws FileNotFoundException {
		// This is messing up display slideshow, because of multiple same obect check in findindexphoto and
		// next and previous mehtods in album 
		//Photo nextPhoto = album.getNextPhoto(currentPhoto);
		//currentPhoto = nextPhoto;

		if(index + 1 >= photos.size()) {
			index = index;
		}else {
			index = index + 1;
		}
		
		
		try {
			imageView.setImage(photos.get(index).getImage());
			currentPhoto = photos.get(index);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			InputStream is = new FileInputStream("photos/notfound.jpg");
			Image notFound = new Image(is);
			imageView.setImage(notFound);
		}
		centerImage();
		updateLabels();
	}
	
	/**
	 * this is for the editCaptionButton it allows users to edit teh caption from teh display 
	 * photos screen
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
		controller.setPhoto(currentPhoto);
		controller.setUserDB(userDB);
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle("Edit Caption");
		controller.start(stage);
		stage.setScene(scene);
		stage.showAndWait();
		
		updateLabels();
		
		
	}
	/**
	 * this is a method for when the edittag button is clicked
	 * @param event on click
	 * @throws IOException exception for clicking the edit button
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
					controller.setPhoto(currentPhoto);
					controller.setUsers(userDB);
					Scene scene = new Scene(root);
					Stage stage = new Stage();
					stage.setTitle("Edit Tags");
					controller.start(stage);
					stage.setScene(scene);
					stage.showAndWait();
					
					updateLabels();
					
					
	}
	
	/**
	 * this method is for the close button and makes it so the window gets closed
	 * @param event on click
	 * @throws IOException exception for closing the window
	 */
	@FXML
	private void handleCloseButton(ActionEvent event) throws IOException {
		// Back to album 
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumContent.fxml"));
		Parent root = (Parent) loader.load();
		AlbumContentController albumController = loader.getController();
		// Run a method like setActiveUser, that takes a parameter username and sets it as a field. 
		albumController.setUser(user);
		albumController.setUserDB(userDB);
		albumController.setAlbum(album);
		Scene albumScene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		albumController.start(stage);
		stage.setScene(albumScene);
		stage.show();
		
	}
	/**
	 * this method is for centering the image
	 */
	public void centerImage() {
        Image image = imageView.getImage();
        if (image != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / image.getWidth();
            double ratioY = imageView.getFitHeight() / image.getHeight();

            double reducCoeff = 0;
            
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            }else {
                reducCoeff = ratioX;
            }

            w = image.getWidth() * reducCoeff;
            h = image.getHeight() * reducCoeff;
            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);

        }
    }
	
	
}
