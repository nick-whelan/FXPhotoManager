package model;
import java.io.Serializable;

/**
 * Tag class represents a name and value tag that can be added to a photo.
 * 
 *
 */
public class Tag implements Serializable{
	
	/**
	 * Serial UID 
	 */
	private static final long serialVersionUID = -8501177911058824172L;
	
	/**
	 * Name of tag
	 */
	private String name;
	
	/**
	 * Value of tag
	 */
	private String value;
	
	/**
	 * Tag constructor
	 * @param name Name of tag
	 * @param value Value of tag
	 */
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Returns the name of the tag
	 * @return Name string
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of tag
	 * @param name Name string
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the value of tag
	 * @return Value string
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value of tag
	 * @param value Value string
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * toString override for tag printing
	 */
	public String toString() {
		return name + ":" + value;
	}
	
	/**
	 * Equals method for tags
	 */
	public boolean equals(Object obj) {
		if(!(obj instanceof Tag)||obj == null) {
			return false;
		}
		else {
			Tag t = (Tag) obj;
			return t.getName().toLowerCase().equals(name.toLowerCase()) && t.getValue().toLowerCase().equals(value.toLowerCase()); 
		}
	}
	
	
}
