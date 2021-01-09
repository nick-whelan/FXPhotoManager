package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import controller.AlbumContentController.PhotoListCell;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserDB;

/**
 * this class is for the search Controller, which controls the search view
 * this view is called search.fxml
 *
 */
public class SearchController {
	/**
	 * these are the fields used in the controller
	 */
	private User user;
	private UserDB userDB;
	private ObservableList<Photo> obsList;
	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private String operand;
	private boolean tagSearch, dateSearch, oneTagSearch, twoTagSearch;

	/**
	 * these are the fxml classes used in the controller
	 */
	@FXML Label dateRangeLabel;
	@FXML Label photoNumLabel;
	@FXML Button logoutButton;
	@FXML Button quitButton;
	@FXML Button searchButton;
	@FXML Button allAlbumsButton;
	@FXML Button clearSearchButton;
	@FXML Button resultsAlbumButton;
	@FXML TextField nameField1;
	@FXML TextField nameField2;
	@FXML TextField valueField1;
	@FXML TextField valueField2;
	@FXML DatePicker datePickerStart;
	@FXML DatePicker datePickerEnd;
	@FXML RadioButton dateRadio;
	@FXML RadioButton tagRadio;
	@FXML RadioButton oneTagRadio;
	@FXML RadioButton twoTagRadio;
	@FXML RadioButton orRadio;
	@FXML RadioButton andRadio;
	@FXML ListView<Photo> listView;
	@FXML ToggleGroup toggleGroup1;
	@FXML ToggleGroup toggleGroup2;
	@FXML ToggleGroup toggleGroup3;
	@FXML Text filterByDateText;
	@FXML Text dateDash;
	@FXML Text tagSearchText;
	@FXML Text tagNameText1;
	@FXML Text tagNameText2;
	@FXML Text tagValueText1;
	@FXML Text tagValueText2;
	
	/**
	 * this start method is used to hide and set certain fields and buttons
	 * @param stage Stage
	 * @throws IOException exception for loading the listview
	 */
	public void start(Stage stage) throws IOException {
		listView.setCellFactory(photoListView -> new PhotoListCell());
		// Assign empty photo list to avoid null ptrs on create
		ArrayList<Photo> tempEmpty = new ArrayList<Photo>();
		obsList = FXCollections.observableArrayList(tempEmpty);
		listView.setItems(obsList);
		clearLabels();
		
		// Initially hide date search stuff
		hideDateSearch();
		hideSecondTagFields();
		oneTagRadio.setSelected(true);
		andRadio.setSelected(true);
		operand = "and";
		tagSearch = true;
		dateSearch = false;
		oneTagSearch = true;
		twoTagSearch = false;
		
		
		// Add listeners to radio buttons to show hide etc, default to tag search
		// Toggle Group 1 Listener for Tags or Dates
		toggleGroup1.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
        { 
            public void changed(ObservableValue<? extends Toggle> ob,  
                                                    Toggle o, Toggle n) 
            { 
                RadioButton radio = (RadioButton)toggleGroup1.getSelectedToggle(); 
                
                if (radio != null) { 
                    String s = radio.getText(); 
                    if(s.equalsIgnoreCase("dates")) {
                    	showDateSearch();
                		hideTagSearch();
                		dateSearch = true;
                		tagSearch = false;
                		twoTagSearch = false;
                		oneTagSearch = false;
                    }
                    if(s.equalsIgnoreCase("tags")) {
                    	hideDateSearch();
                		showTagSearch();
                		tagSearch = true;
                		dateSearch = false;
                		oneTagSearch = true;
                		twoTagSearch = false;
                    }
                    
                } 
            } 
        }); 
		
		toggleGroup2.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
        { 
            public void changed(ObservableValue<? extends Toggle> ob,  
                                                    Toggle o, Toggle n) 
            { 
                RadioButton radio = (RadioButton)toggleGroup2.getSelectedToggle(); 
                
                if (radio != null) { 
                    String s = radio.getText(); 
                    
                    if(s.equalsIgnoreCase("one tag")) {
                    	hideSecondTagFields();
                    	oneTagSearch = true;
                    	twoTagSearch = false;
                    }
                    if(s.equalsIgnoreCase("two tags")) {
                    	showSecondTagFields();
                    	twoTagSearch = true;
                    	oneTagSearch = false;
                    }
                    
                } 
            } 
        });
		
		toggleGroup3.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
        { 
            public void changed(ObservableValue<? extends Toggle> ob,  
                                                    Toggle o, Toggle n) 
            { 
                RadioButton radio = (RadioButton)toggleGroup3.getSelectedToggle(); 
                
                if (radio != null) { 
                    String s = radio.getText(); 
                    if(s.equalsIgnoreCase("and")) {
                    	operand = "and";
                    }
                    if(s.equalsIgnoreCase("or")) {
                    	operand = "or";
                    }
                    
                } 
            } 
        });
		
	}
	

	/**
	 * this method is used to clear the Labels
	 */
	public void clearLabels() {
		photoNumLabel.setText("0");
	}
	
	/**
	* this method is used to show the fields of the Second tag
 	*/
	public void showSecondTagFields() {
		tagNameText2.setVisible(true);
		tagValueText2.setVisible(true);
		valueField2.setVisible(true);
		nameField2.setVisible(true);
		andRadio.setVisible(true);
		orRadio.setVisible(true);
	}
	
	/**
	 * this method is used to hide the fields of the second tag
	 */
	public void hideSecondTagFields() {
		valueField2.setVisible(false);
		nameField2.setVisible(false);
		andRadio.setVisible(false);
		orRadio.setVisible(false);
		tagNameText2.setVisible(false);
		tagValueText2.setVisible(false);
	}
	
	/**
	 * this method is used to show the fields of the first tag
	 */
	public void showTagSearch() {
		tagSearchText.setVisible(true);
		tagNameText1.setVisible(true);
		tagValueText1.setVisible(true);
		nameField1.setVisible(true);
		valueField1.setVisible(true);
		oneTagRadio.setVisible(true);
		twoTagRadio.setVisible(true);
		
		// Two tag things
		/*
		valueField2.setVisible(true);
		nameField2.setVisible(true);
		tagNameText2.setVisible(true);
		tagValueText2.setVisible(true);
		andRadio.setVisible(true);
		orRadio.setVisible(true);
		*/
		
		// Set one tag selected
		oneTagRadio.setSelected(true);
	}
	
	/**
	 * this method is used to hide the field of both tags
	 */
	public void hideTagSearch() {
		tagSearchText.setVisible(false);
		tagNameText1.setVisible(false);
		tagValueText1.setVisible(false);
		nameField1.setVisible(false);
		valueField1.setVisible(false);
		oneTagRadio.setVisible(false);
		twoTagRadio.setVisible(false);
		
		// Two tag things
		tagNameText2.setVisible(false);
		tagValueText2.setVisible(false);
		valueField2.setVisible(false);
		nameField2.setVisible(false);
		andRadio.setVisible(false);
		orRadio.setVisible(false);
	}
	
	/**
	 * this method is used to show the fields for the date search
	 */
	public void showDateSearch() {
		filterByDateText.setVisible(true);
		dateDash.setVisible(true);
		datePickerStart.setVisible(true);
		datePickerEnd.setVisible(true);
	}
	/**
	 * this method is used to hide the fields of the date search
	 */
	public void hideDateSearch() {
		filterByDateText.setVisible(false);
		dateDash.setVisible(false);
		datePickerStart.setVisible(false);
		datePickerEnd.setVisible(false);
		
	}
	/**
	 * this is the setter for User
	 * @param u User
	 */
	public void setUser (User u) {
		this.user = u;
	}
	
	/**
	 * this is the setter for UserDB
	 * @param users UserDB
	 */
	public void setUserDB(UserDB users) {
		this.userDB = users;
	}
	
	/**
	 * this is the method for when the search button is clicked
	 * @param event on click
	 */
	@FXML
	private void handleSearchButton(ActionEvent event) {
		Parent root;
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		
		ArrayList<Photo> result = new ArrayList<Photo>();
		
		//Case: Error -> Check if we are in a date search, if so make sure both dates are filled, or both are null
		if((dateSearch == true && tagSearch == false) && ((datePickerStart.getValue() == null && datePickerEnd.getValue() != null) || (datePickerEnd.getValue() == null && datePickerStart.getValue() != null) || (datePickerStart.getValue() == null && datePickerEnd.getValue() == null))) {
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("Error: Missing Field/s");
			emptyField.setHeaderText("If searching for a date range both date fields must be filled.");
			emptyField.showAndWait();
			return;
		}
		
		
		// Case: Date Search
		if(datePickerStart.getValue() != null && datePickerEnd.getValue() != null && (dateSearch == true && tagSearch == false)) {
			LocalDate localDateStart = datePickerStart.getValue();
			LocalDate localDateEnd = datePickerEnd.getValue();
			Instant instantStart = Instant.from(localDateStart.atStartOfDay(ZoneId.systemDefault()));
			Instant instantEnd = Instant.from(localDateEnd.atStartOfDay(ZoneId.systemDefault()));
			Date dateStart = Date.from(instantStart);
			Date dateEnd = Date.from(instantEnd);
			result = user.getPhotosInDateRange(dateStart, dateEnd);
			Set<Photo> resultSet = new HashSet<Photo>(result);
			obsList = FXCollections.observableArrayList(resultSet);
			listView.setItems(obsList);
			photoNumLabel.setText(Integer.toString(obsList.size()));
			return;
		}
		
		// Case: Error -> One tag search and empty field
		if((nameField1.getText().strip().isEmpty() || valueField1.getText().strip().isEmpty()) && (dateSearch == false && tagSearch == true && oneTagSearch == true)) {
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("Error: Missing Field/s");
			emptyField.setHeaderText("If searching for a tag both name and value must be filled.");
			emptyField.showAndWait();
			return;
		}
		
		// Case: Error -> Two tag search and empty field
		if((nameField1.getText().strip().isEmpty() || valueField1.getText().strip().isEmpty() || nameField2.getText().strip().isEmpty() || valueField2.getText().strip().isEmpty()) && (dateSearch == false && tagSearch == true && twoTagSearch == true && oneTagSearch == false)) {
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("Error: Missing Field/s");
			emptyField.setHeaderText("If searching for a tag both name and value must be filled.");
			emptyField.showAndWait();
			return;
		}
		
		// Case: One tag Search
		if(!nameField1.getText().strip().isEmpty() && !valueField1.getText().strip().isEmpty() && (dateSearch == false && tagSearch == true && twoTagSearch == false && oneTagSearch==true)) {
			String nameField = nameField1.getText().toLowerCase().strip();
			String valueField = valueField1.getText().toLowerCase().strip();
			
			Tag searchTag = new Tag(nameField, valueField);
			result = user.getPhotosWithTags(searchTag);
			Set<Photo> resultSet = new HashSet<Photo>(result);
			
			result = new ArrayList<Photo>(resultSet);
			obsList = FXCollections.observableArrayList(result);
    		listView.setItems(obsList);
    		photoNumLabel.setText(Integer.toString(obsList.size()));
    		return;
		}
		
		// Case: Two tag search
		if(!valueField2.getText().strip().isEmpty() && !nameField2.getText().strip().isEmpty() && !nameField1.getText().strip().isEmpty() && !valueField1.getText().strip().isEmpty() && (dateSearch == false && tagSearch == true && twoTagSearch == true && oneTagSearch == false)) {
			String nameFieldOne = nameField1.getText().toLowerCase().strip();
			String valueFieldOne = valueField1.getText().toLowerCase().strip();
			String nameFieldTwo = nameField2.getText().toLowerCase().strip();
			String valueFieldTwo = valueField2.getText().toLowerCase().strip();
			
			Tag firstTag = new Tag(nameFieldOne, valueFieldOne);
			Tag secondTag = new Tag(nameFieldTwo, valueFieldTwo);
			
			Set<Photo> firstSet = new HashSet<Photo>(user.getPhotosWithTags(firstTag));
			Set<Photo> secondSet = new HashSet<Photo>(user.getPhotosWithTags(secondTag));
			
			
			if(operand.equals("and")) {
        		ArrayList<Photo> andList = new ArrayList<Photo>(intersection(firstSet, secondSet));
        		obsList = FXCollections.observableArrayList(andList);
        		listView.setItems(obsList);
        		photoNumLabel.setText(Integer.toString(obsList.size()));
        		return;
        		
        	}
        	else if(operand.equals("or")) {
        		ArrayList<Photo> orList = new ArrayList<Photo>(union(firstSet, secondSet));
        		obsList = FXCollections.observableArrayList(orList);
        		listView.setItems(obsList);
        		photoNumLabel.setText(Integer.toString(obsList.size()));
        		return;
        	}

		}
		
	}
	
	/**
	 * this method returns the intersection of two sets
	 * @param first is the set of photos with the first tag
	 * @param second is the set of photos with the second tag
	 * @return a set of photos
	 */
	public Set<Photo> intersection(Set<Photo> first, Set<Photo> second) {
	    Set<Photo> copy = new HashSet<Photo>(first);
	    copy.retainAll(second);
	    return copy;
	}

	/**
	 * this method returns the union of two sets
	 * @param first is the set of photos with the first tag
	 * @param second is the set of photos with the second tag
	 * @return a set of photos
	 */
	public Set<Photo> union(Set<Photo> first, Set<Photo> second) {
	    Set<Photo> copy = new HashSet<>(first);
	    copy.addAll(second);
	    return copy;
	}
	
	/**
	 * this is the method for creating a new album from the search results
	 * @param event on click
	 * @throws FileNotFoundException exception for dat file not found
	 * @throws IOException for creating a new album
	 */
	@FXML
	private void handleResultsAlbumButton(ActionEvent event) throws FileNotFoundException, IOException {
		Node source = (Node) event.getSource();
		Window window = source.getScene().getWindow();
		
		// First make sure obsList isnt empty
		if(obsList.isEmpty()) {
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("No photos in results");
			emptyField.setHeaderText("Error: Must have photos in search results to create a new album");
			emptyField.showAndWait();
			return;
		}
		
		
		// Need window for album name first 
		TextInputDialog renameDialog = new TextInputDialog();
		renameDialog.setTitle("New Album Name");
		renameDialog.getDialogPane().setContentText("Enter New Album Name");
		renameDialog.getDialogPane().setHeaderText("Album Name:");
		Optional<String> result = renameDialog.showAndWait();
		TextField input = renameDialog.getEditor();
		
		if(user.albumNameExists(input.getText())) {
			Alert emptyField = new Alert(AlertType.ERROR);
			emptyField.initOwner(window);
			emptyField.setTitle("Invalid album name");
			emptyField.setHeaderText("Error: Album name already exists, try again");
			emptyField.showAndWait();
			return;
		}
		
		ArrayList<Photo> resultPhotos = new ArrayList<Photo>();
		for(Photo p : obsList) {
			resultPhotos.add(p);
		}
		
		user.addAlbum(input.getText());
		Album album = user.getAlbumFromList(input.getText());
		for(Photo p : resultPhotos) {
			album.addPhoto(p);
		}
				
		UserDB.writeUsers(userDB);
		//System.out.println("WROTE OUT - " + userDB);
	}
	
	/**
	 * this method is for clearing the search results
	 * @param event on click
	 */
	@FXML
	private void handleClearSearchButton(ActionEvent event) {
		obsList.clear();
		listView.setItems(obsList);
		clearLabels();
	}
	
	/**
	 * this method is for returning back to the albums view
	 * this view is called albums.fxml
	 * @param event on click
	 * @throws IOException exception for returning to albums view
	 * @throws ClassNotFoundException exception for returning to albums view
	 */
	@FXML
	private void handleAllAlbumsButton(ActionEvent event) throws IOException, ClassNotFoundException {
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
	 * this method is for when the logout button is clicked and returns us back to the login screen
	 * @param event on click
	 * @throws IOException exception for returning to the login
	 * @throws ClassNotFoundException exception for returning to the login
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
	 * this method is for quiting the app
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
	 * this inner class is for displaying the search results in listCells in the listview
	 */
	public class PhotoListCell extends ListCell<Photo>{
		@FXML Label dateLabel;
		@FXML Label captionLabel;
		@FXML ImageView imageView;
		@FXML AnchorPane anchorPane;
		@FXML ListView<Tag> tagListView;
		private FXMLLoader loader;
		private ObservableList<Tag> tagObsList;
		
		/**
		 * this method is for updating the tags and captions in the listcell
		 */
		public void updateTags() {
			captionLabel.setText(this.getItem().getCaption());
			ArrayList<Tag> tagList = this.getItem().getTags();
			tagObsList = FXCollections.observableArrayList(tagList);
			tagListView.setItems(tagObsList);
		}
		/**
		 * this overrided method is for creating our custom listCell
		 */
		@Override
	    protected void updateItem(Photo photo, boolean empty) {
	        super.updateItem(photo, empty);

	        if(empty || photo == null) {
	            setText(null);
	            setGraphic(null);
	        } else {
	            if (loader == null) {
	                loader = new FXMLLoader(getClass().getResource("/view/searchCell.fxml"));
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
					e.printStackTrace();
					InputStream is = null;
					try {
						is = new FileInputStream("photos/notfound.jpg");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					Image notFound = new Image(is);
					imageView.setImage(notFound);
				}
	            updateTags();
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				dateLabel.setText(dateFormat.format(this.getItem().getPhotoDate()));

	            setText(null);
	            setGraphic(anchorPane);
	        }

	    }
		
	}
	
	
}
