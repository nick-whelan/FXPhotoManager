package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.scene.image.Image;

/**
 * Photo class holds the path path of the photo as well as a list of tags associated with the photo.
 * 
 *
 */
public class Photo implements Serializable{
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -1099397600841970955L;
	
	/**
	 * Tag list for photo
	 */
	private ArrayList<Tag> tags = new ArrayList<Tag>(); 
	
	/**
	 * Caption string
	 */
	private String caption = "";
	
	/**
	 * Date object for photo date
	 */
	private Date photoDate;
	
	/**
	 * Path string of photo
	 */
	private String path;

	/**
	 * Photo constructor 
	 * @param p Path string of photo
	 */
	public Photo(String p) {
		this.path = p;
	}
	
	/**
	 * Returns path string of photo
	 * @return Path string
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * Adds tag to tag list
	 * @param t Tag object
	 */
	public void addTag(Tag t) {
		tags.add(t);
	}
	
	/**
	 * Returns the tag list
	 * @return ArrayList of tags
	 */
	public ArrayList<Tag> getTags() {
		return this.tags;
	}
	
	/**
	 * Returns a string of all tags
	 * @return Tag string
	 */
	public String getStringTags() {
		StringBuilder sb = new StringBuilder();
		for(Tag t : tags) {
			sb.append(t.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Sets the photos date
	 * @param date Date object
	 */
	public void setPhotoDate(Date date) {
		this.photoDate = date;
	}
	
	/**
	 * Returns the photos date
	 * @return Date object
	 */
	public Date getPhotoDate() {
		return photoDate;
	}
	
	/**
	 * Returns the tag with passed name if it is found in tag list.
	 * @param name Name of tag to be searched
	 * @return Tag object if found, null Tag if not.
	 */
	public Tag getTagFromList(String name) {
		for(Tag t : tags) {
			if(t.getName().equalsIgnoreCase(name)) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Removes a tag from the tag list
	 * @param t Tag to be removed
	 */
	public void removeTag(Tag t) {
		tags.remove(t);
	}
	
	/**
	 * Checks if a photo has duplicate tags of passed tag
	 * @param temp Tag to check if duplicates exist of
	 * @return true if found, false if not
	 */
	public boolean duplicatesExist(Tag temp) {
		for(Tag t :tags) {
			if(t.equals(temp)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Updates the value of the tag in the tag list at the index passed
	 * @param index Index of tag in tag list to be edited.
	 * @param name New name for tag
	 * @param value New value for tag
	 */
	public void editTag(int index, String name, String value) {
		tags.get(index).setName(name);
		tags.get(index).setValue(value);
	}
	
	/**
	 * Returns the photos caption
	 * @return Caption string
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * Sets the photos caption
	 * @param caption Caption string
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * returns the Image object made from input stream out of path string
	 * @return Image object
	 * @throws FileNotFoundException If path is not found 
	 */
	public Image getImage() throws FileNotFoundException {
		InputStream is = new FileInputStream(path);
		Image image = new Image(is);
		return image;
	}
	
	
}
