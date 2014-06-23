package cz.dawon.java.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents Single Action with its Followers and Prerequisities
 * 
 * @author Jakub Zacek
 * @version 1.1.2
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
	 * {@link HashMap} of {@link Set}s containing Prerequisities (0), Tight Prerequisities (1), Followers (2) and Tight Followers (3)
	 */
	private Map<Integer, Set<I>> references;

	/**
	 * {@link String} names of all references...
	 */
	public static final String[] REFERENCES_NAMES = new String[] {"Prerequisities", "Tight Prerequisities", "Followers", "Tight Followers"};

	/**
	 * class constructor
	 * @param id action identifier
	 */
	public Action(I id) {
		this.id = id;

		references = new HashMap<Integer, Set<I>>();

		for (int i = 0; i < REFERENCES_NAMES.length; i++) {
			references.put(i, new HashSet<I>());	
		}
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
		this.references.get(0).add(p);
	}

	/**
	 * Removes a prerequisity
	 * @param p prerequisity identifier
	 */
	public void removePrerequisity(I p) {
		this.references.get(0).remove(p);
	}	

	/**
	 * Gets a set of prerequisities
	 * @return set of prerequisities
	 */
	public Set<I> getPrerequisities() {
		return this.references.get(0);
	}	


	/**
	 * Adds a tight prerequisity
	 * @param p tight prerequisity identifier
	 */
	public void addTightPrerequisity(I p) {
		this.references.get(1).add(p);
	}

	/**
	 * Removes a tight prerequisity
	 * @param p tight prerequisity identifier
	 */
	public void removeTightPrerequisity(I p) {
		this.references.get(1).remove(p);
	}	

	/**
	 * Gets a set of tight prerequisities
	 * @return set of tight prerequisities
	 */
	public Set<I> getTightPrerequisities() {
		return this.references.get(1);
	}




	/**
	 * Adds a follower
	 * @param f follower identifier
	 */
	public void addFollower(I f) {
		this.references.get(2).add(f);
	}

	/**
	 * Removes a follower
	 * @param p follower identifier
	 */
	public void removeFollower(I f) {
		this.references.get(2).remove(f);
	}	

	/**
	 * Gets a set of followers
	 * @return set of followers
	 */
	public Set<I> getFollowers() {
		return this.references.get(2);
	}	


	/**
	 * Adds a tight follower
	 * @param p tight follower identifier
	 */
	public void addTightFollower(I f) {
		this.references.get(3).add(f);
	}

	/**
	 * Removes a tight follower
	 * @param p tight follower identifier
	 */
	public void removeTightFollower(I f) {
		this.references.get(3).remove(f);
	}	

	/**
	 * Gets a set of tight followers
	 * @return set of tight followers
	 */
	public Set<I> getTightFollowers() {
		return this.references.get(3);
	}		

	/**
	 * Returns {@link HashMap} containing all references
	 * @return {@link HashMap}
	 */
	public Map<Integer, Set<I>> getRawReferences() {
		return this.references;
	}

	@Override
	public String toString() {
		String res = "";

		res += "Action(id="+this.id+") { ";
		res += "Data='"+this.data+"'";

		for (int i = 0; i < REFERENCES_NAMES.length; i++) {
			if (!references.get(i).isEmpty()) {
				res += "; " + REFERENCES_NAMES[i] + "=" + this.references.get(i);
			}
		}

		res += "}";
		return res;
	}

	/**
	 * Returns exact copy of this {@link Action}
	 * @return copy of {@link Action}
	 */
	public Action<I, D> copy() {
		Action<I, D> action = new Action<I, D>(id);
		action.data = this.data;
		action.references = new HashMap<Integer, Set<I>>();
		
		for (int i = 0; i < 4; i++) {
			action.references.put(i, new HashSet<I>(this.references.get(i)));
		}

		return action;
	}

}
