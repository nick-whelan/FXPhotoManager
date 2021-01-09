package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * UserDB Class holds all users objects in an ArrayList of Users. Is serializable. 
 * 
 */
public class UserDB implements Serializable{
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -4061570660001315045L;
	
	/**
	 * ArrayList to hold users
	 */
	private ArrayList<User> userList = new ArrayList<User>();
	
	/**
	 * directory and file name information for object serialization. 
	 */
	public static final String storeDir = "dat";
	public static final String storeFile = "users.dat";
	
	
	/**
	 * Constructor 
	 * 
	 * @throws FileNotFoundException fnf Exception
	 * @throws IOException IO Exception
	 */
	public UserDB() throws FileNotFoundException, IOException {
		
	}
	
	/**
	 * addUser method takes in a username and creates a new user object and adds it to the user list.
	 * @param name Username to be passed to User constructor.
	 */
	public void addUser(String name) {
		User u = new User(name);
		this.userList.add(u);
	}
	
	/**
	 * addUser method takes in a User object and adds it to the user list.
	 * @param user User object to be added
	 */
	public void addUser(User user) {
		this.userList.add(user);
	}
	
	/**
	 * Removes a user from the user list.
	 * @param u user to be removed
	 */
	public void removeUser(User u) {
		this.userList.remove(u);
	}
	
	/**
	 * Takes a username string and returns the user object in the userlist with the corresponding name.
	 * @param username username to be searched for.
	 * @return User object found, null if it does not exist.
	 */
	public User getUserByName(String username) {
		for(User u : userList) {
			if(u.getUsername().equalsIgnoreCase(username)) {
				return u;
			}
		}
		return null;
	}
	
	/**
	 * Checks if the userlist contains a user with the passed username.
	 * @param name Username to be searched for.
	 * @return True if there is a user, false if not.
	 */
	public boolean containsUser(String name) {
		if(userList.isEmpty()) {
			return false;
		}
		
		for(User u: userList) {
			if(u.getUsername().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Static method to load the userDB object from a file
	 * 
	 * @return UserDB object 
	 * 
	 * @throws FileNotFoundException If file is not found
	 * @throws IOException Cannot be loaded
	 * @throws ClassNotFoundException Invalid classes
	 */
	public static UserDB loadUsers() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		UserDB users = (UserDB) ois.readObject();
		ois.close();
		return users;
	}
	
	/**
	 * Static method to write the userDB object to a file
	 * @param users The UserDB object to be written
	 * 
	 * @throws FileNotFoundException If file is not found
	 * @throws IOException Cannot be loaded
	 */
	public static void writeUsers(UserDB users) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(users);
		oos.close();
	}
	
	/**
	 * Returns the userlist
	 * @return ArrayList of users
	 */
	public ArrayList<User> getUserList(){
		return userList;
	}

	
	
}
