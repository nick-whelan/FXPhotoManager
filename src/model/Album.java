package model;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

import javafx.scene.image.Image;

/**
 * Album class holds an ArrayList of photos in the album.
 * 
 *
 */
public class Album implements Serializable{
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -454896047563792086L;
	
	/**
	 * Name of album
	 */
	private String name;

	
	/**
	 * Photos list
	 */
	private ArrayList<Photo> photos;
	
	/**
	 * Album constructor
	 * @param name Name of album
	 */
	public Album(String name) {
		photos = new ArrayList<Photo>();
		this.name = name;
	}

	
	
	/**
	 * Renames the album
	 * @param name New album name
	 */
	public void renameAlbum(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the album
	 * @return Name string
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the albums photo list
	 * @return List of photos
	 */
	public ArrayList<Photo> getPhotosList(){
		return photos;
	}

	
	public void setPhotosList(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	
	/**
	 * Adds a photo object to the album
	 * @param p Photo to be added
	 */
	public void addPhoto(Photo p) {
		photos.add(p);
	}
	
	/**
	 * Deletes the photo at the passed index in the photo list
	 * @param index Index of photo to be removed
	 */
	public void deletePhoto(int index) {
		photos.remove(index);
	}
	
	/**
	 * Returns the a reference to the photo object in photo list if passed photo has same path
	 * @param p Photo object to search for
	 * @return Photo object from photo list
	 */
	public Photo getPhoto(Photo p) {
		return photos.get(findIndexOfPhoto(p));
	}
	
	/**
	 * Checks if a given path is in the album
	 * @param path Path to search for
	 * @return True if path is found, false if not
	 */
	public boolean isPhotoInAlbum(String path) {
		for(Photo p : photos) {
			if(p.getPath().equalsIgnoreCase(path)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns index of photo object in photo list if found
	 * @param photo Photo to be searched for
	 * @return Index of photo if found, -1 if not
	 */
	public int findIndexOfPhoto(Photo photo) {
		for (int i = 0 ; i < photos.size(); i++) {
			if(photo.equals(photos.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns the next photo object in the photo list, same photo object if we are already at end of list.
	 * @param p Photo object to try to get subsequent photo of
	 * @return Next photo object
	 */
	public Photo getNextPhoto(Photo p) {
		// Get the photo in albumList after passed photo
		int index = findIndexOfPhoto(p);
		if((index) == photos.size()-1) {
			// is last photo, cant go farther
			return p;
		}
		return photos.get(index+1);
	}	
	
	/**
	 * Returns the previous photo object in phoo list, same object if passed photo is at the beginning of the list.
	 * @param p Photo object to try to get previous photo of
	 * @return Previous photo object
	 */
	public Photo getPreviousPhoto(Photo p) {
		int index = findIndexOfPhoto(p);
		if(index == 0) {
			return p;
		}
		return photos.get(index-1);
	}
	
	/**
	 * Returns the image of the first photo in the album for an album thumbnail
	 * @return Image of first photo in list
	 * @throws FileNotFoundException Exception if photo file is not found
	 */
	public Image getAlbumCover() throws FileNotFoundException {
		if(photos.isEmpty()) {
			return null;
		}
		return photos.get(0).getImage();
	}
	
	/**
	 * Returns the size of the photo list
	 * @return Size integer
	 */
	public int getSize() {
		return photos.size();
	}
	
	/**
	 * toString override to print album name
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * Returns a list of photos in the valid date range
	 * @param start Start date
	 * @param end End date
	 * @return Photo result list
	 */
	public ArrayList<Photo> getPhotosInDateRange(Date start, Date end){
		ArrayList<Photo> resultPhotos = new ArrayList<Photo>();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		for(Photo p : photos) {
			if( (p.getPhotoDate().after(start) && p.getPhotoDate().before(end)) || (dateFormat.format(p.getPhotoDate()).equalsIgnoreCase(dateFormat.format(start))) || (dateFormat.format(p.getPhotoDate()).equalsIgnoreCase(dateFormat.format(end)))) {
				resultPhotos.add(p);
			}
		}
		return resultPhotos;
	}
	
	/**
	 * Returns a string of the date range for the album
	 * @return Date string
	 */
	public String getDateRangeString() {
		String start, end, result;
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		ArrayList<Photo> list = new ArrayList<Photo>(photos);
		DateCompare dc = new DateCompare();
		list.sort(dc);
		
		start = dateFormat.format(list.get(0).getPhotoDate());
		end = dateFormat.format(list.get(list.size() - 1).getPhotoDate());
		
		result = start + " - " + end;
		return result;
		
	}
	
	/**
	 * Returns a list of photos that have the passed tag
	 * @param tag Tag object to search for
	 * @return Resulting photo list
	 */
	public ArrayList<Photo> getPhotosWithTags(Tag tag){
		ArrayList<Photo> resultPhotos = new ArrayList<Photo>();
		ArrayList<Tag> photoTags = new ArrayList<Tag>();
		for(Photo p : photos) {
			photoTags = p.getTags();
			for(Tag t : photoTags) {
				if(tag.equals(t)) {
					resultPhotos.add(p);
				}
			}
		}
		return resultPhotos;
	}
	
	/**
	 * Date Comparator for sorting lists of photos by date
	 * 
	 */
	public class DateCompare implements Comparator<Photo> {
		@Override
		public int compare(Photo p1, Photo p2) {
			return p1.getPhotoDate().compareTo(p2.getPhotoDate());
		}
	}
	
}
