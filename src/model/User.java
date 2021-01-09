package model;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * User class holds a list of the respective users albums.
 * 
 */
public class User implements Serializable{
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -2714597558175704937L;
	
	/**
	 * Username field for user
	 */
	private String username;
	
	/**
	 * Albums list
	 */
	private ArrayList<Album> albums = new ArrayList<Album>(); // changed from albums;
	
	/**
	 * User constructor
	 * @param username Username to be set
	 */
	public User(String username) {
		this.username = username;
	}
	
	/**
	 * Returns username
	 * @return The username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Creates an album with the passed name and adds it to the user's album list.
	 * @param name Name of new album
	 */
	public void addAlbum(String name){
		albums.add(new Album(name));
	}
	
	/**
	 * Removes an album from the users album list.
	 * @param album Album object to be removed
	 */
	public void deleteAlbum(Album album) {
		albums.remove(album);
	}
	
	/**
	 * Returns the users album list
	 * @return ArrayList of albums
	 */
	public ArrayList<Album> getAlbumList(){
		return albums;
	}
	
	/**
	 * Returns album object from users album list that matches the name argument.
	 * @param name Name to be searched for in album list.
	 * @return Album object to be returned if found, null if does not exist.
	 */
	public Album getAlbumFromList(String name) {
		if(albums.isEmpty()) {
			return null;
		}
		for(Album album : albums) {
			if(album.getName().equalsIgnoreCase(name)) {
				return album;
			}
		}
		return null;
	}
	
	/**
	 * Checks if an album with the passed name exists.
	 * @param name Name to be searched for in album list.
	 * @return True if album exists, false if not.
	 */
	public boolean albumNameExists(String name) {
		if(albums.isEmpty()) {
			return false;
		}
		for(Album album : albums) {
			if(album.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Returns an ArrayList of photos in an album that have dates between the passed start and end date values.
	 * @param start Start date for search range
	 * @param end End date for search range
	 * @return The resulting arraylist of photos that are in range.
	 */
	public ArrayList<Photo> getPhotosInDateRange(Date start, Date end){
		ArrayList<Photo> resultPhotos = new ArrayList<Photo>();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		
		for(Album a : albums) {
			for(Photo p : a.getPhotosList()) {
				if( (p.getPhotoDate().after(start) && p.getPhotoDate().before(end)) || (dateFormat.format(p.getPhotoDate()).equalsIgnoreCase(dateFormat.format(start))) || (dateFormat.format(p.getPhotoDate()).equalsIgnoreCase(dateFormat.format(end)))) {
					resultPhotos.add(p);
				}
			}
		}
		return resultPhotos;
	}
	
	/**
	 * Returns an ArrayList of photos in the album that have the passed tag
	 * @param tag Tag objet to be searched for
	 * @return The resulting ArrayList of photos.
	 */
	public ArrayList<Photo> getPhotosWithTags(Tag tag){
		ArrayList<Photo> resultPhotos = new ArrayList<Photo>();
		ArrayList<Tag> photoTags = new ArrayList<Tag>();
		
		for(Album a : albums) {
			for(Photo p : a.getPhotosList()) {
				photoTags = p.getTags();
				for(Tag t : photoTags) {
					//for each tag we compare and add the matchign photo to 
					//result list.
					if(tag.equals(t)) {
						resultPhotos.add(p);
					}
				}
			}
		}
		return resultPhotos;
		
	}
	
	/**
	 * Checks if a photo (a path string) is in any of the users album
	 * @param path Path string
	 * @return True if found, false if not.
	 */
	public boolean photoExists(String path) {
		for(Album album : albums) {
			for(Photo p : album.getPhotosList()) {
				if(p.getPath().equalsIgnoreCase(path)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method takes in a path string, and returns the corresponding photo object.
	 * @param path Path of photo object.
	 * @return Photo object with corresponding path string.
	 */
	public Photo getPhotoFromPath(String path) {
		for(Album album : albums) {
			for(Photo p : album.getPhotosList()) {
				if(p.getPath().equalsIgnoreCase(path)) {
					return p;
				}
			}
		}
		return null;
	}
	
	/**
	 * Copies a photo from one album to another.
	 * @param photo Photo to be copied.
	 * @param destAlbumIndex Index of the destination album in the users album list
	 */
	public void copyPhoto(Photo photo, int destAlbumIndex) {
		albums.get(destAlbumIndex).addPhoto(photo);
	}
	
	/**
	 * Moves a photo from one album to another (removes photo from original album)
	 * @param photo Photo to be moved
	 * @param originAlbumIndex Index of the origin album in album list.
	 * @param destAlbumIndex Index of the destination album in album list.
	 */
	public void movePhoto(Photo photo,int originAlbumIndex ,int destAlbumIndex) {
		albums.get(destAlbumIndex).addPhoto(photo);
		albums.get(originAlbumIndex).deletePhoto(albums.get(originAlbumIndex).findIndexOfPhoto(photo));
	}
	
	/**
	 * Returns the index of the passed album object in the users album list.
	 * @param a Album object to get index for.
	 * @return index of album object if found, -1 if no album exists
	 */
	public int getAlbumIndex(Album a) {
		for (int i =0;i<albums.size(); i++) {
			if(a.getName().equals(albums.get(i).getName())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * toString override for printing username
	 */
	public String toString() {
		return username;
	}
	
	
}
