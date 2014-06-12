package cz.dawon.java.library;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents Single Action with its Followers and Prerequisities
 * 
 * @author Jakub Zacek
 * @version 1.0.1
 *
 * @param <I> datatype for Action's identifier
 * @param <D> datatype for Action's data
 */
public class Action<I, D> {

	/**
	 * Action identifier
	 */
	private I id;
	/**
	 * Action's data
	 */
	private D data;
	
	/**
	 * Set of action identifiers that must happen before this action
	 */
	private Set<I> prerequisities;
	/**
	 * Set of action identifiers that must happen just before this action
	 */	
	private Set<I> tightPrerequisities;
	/**
	 * Set of action identifiers that must happen after this action
	 */	
	private Set<I> followers;
	/**
	 * Set of action identifiers that must happen just after this action
	 */	
	private Set<I> tightFollowers;
	
	/**
	 * class constructor
	 * @param id action identifier
	 */
	public Action(I id) {
		this.id = id;
		
		prerequisities      = new HashSet<I>();
		tightPrerequisities = new HashSet<I>();
		followers           = new HashSet<I>();
		tightFollowers      = new HashSet<I>();
	}
	
	
	
	/**
	 * Gets this Action's identifier
	 * @return identifier
	 */
	public I getId() {
		return this.id;
	}
	
	/**
	 * Gets this Action's data
	 * @return data
	 */
	public D getData() {
		return this.data;
	}
	
	/**
	 * Sets this Action's data
	 * @param data data
	 */
	public void setData(D data) {
		this.data = data;
	}

	/**
	 * Adds a prerequisity
	 * @param p prerequisity identifier
	 */
	public void addPrerequisity(I p) {
		this.prerequisities.add(p);
	}
	
	/**
	 * Removes a prerequisity
	 * @param p prerequisity identifier
	 */
	public void removePrerequisity(I p) {
		this.prerequisities.remove(p);
	}	
	
	/**
	 * Gets a set of prerequisities
	 * @return set of prerequisities
	 */
	public Set<I> getPrerequisities() {
		return new HashSet<I>(this.prerequisities);
	}	
	
	
	/**
	 * Adds a tight prerequisity
	 * @param p tight prerequisity identifier
	 */
	public void addTightPrerequisity(I p) {
		this.tightPrerequisities.add(p);
	}
	
	/**
	 * Removes a tight prerequisity
	 * @param p tight prerequisity identifier
	 */
	public void removeTightPrerequisity(I p) {
		this.tightPrerequisities.remove(p);
	}	
	
	/**
	 * Gets a set of tight prerequisities
	 * @return set of tight prerequisities
	 */
	public Set<I> getTightPrerequisities() {
		return new HashSet<I>(this.tightPrerequisities);
	}
	
	
	
	
	/**
	 * Adds a follower
	 * @param f follower identifier
	 */
	public void addFollower(I f) {
		this.followers.add(f);
	}
	
	/**
	 * Removes a follower
	 * @param p follower identifier
	 */
	public void removeFollower(I f) {
		this.followers.remove(f);
	}	
	
	/**
	 * Gets a set of followers
	 * @return set of followers
	 */
	public Set<I> getFollowers() {
		return new HashSet<I>(this.followers);
	}	
	
	
	/**
	 * Adds a tight follower
	 * @param p tight follower identifier
	 */
	public void addTightFollower(I f) {
		this.tightFollowers.add(f);
	}
	
	/**
	 * Removes a tight follower
	 * @param p tight follower identifier
	 */
	public void removeTightFollower(I f) {
		this.tightFollowers.remove(f);
	}	
	
	/**
	 * Gets a set of tight followers
	 * @return set of tight followers
	 */
	public Set<I> getTightFollowers() {
		return new HashSet<I>(this.tightFollowers);
	}		
	
	@Override
	public String toString() {
		String res = "";
		
		res += "Action(id="+this.id+") { ";
		
		res += "Prerequisities=" + this.prerequisities;
		
		res += "; Tight Prerequisities=" + this.tightPrerequisities;
		
		res += "; Followers=" + this.followers;
		
		res += "; Tight Followers=" + this.tightFollowers;
		
		res += "; Data='"+this.data+"'}";
		return res;
	}
}
