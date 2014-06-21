package cz.dawon.java;

import javax.swing.tree.DefaultMutableTreeNode;

import cz.dawon.java.library.Action;

/**
 * Modified {@link DefaultMutableTreeNode} to be used with Actions
 * @author Jakub Zacek
 * @version 1.0
 */
public class ActionMutableTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 3837676570798233740L;

	/**
	 * type of this node
	 */
	protected int type;

	/**
	 * constructor
	 */
	public ActionMutableTreeNode() {
		super(null);
	}

	/**
	 * constructor
	 * @param type type of node
	 * @param obj object in node
	 */
	public ActionMutableTreeNode(int type, Object obj) {
		this(type, obj, true);
	}	

	/**
	 * constructor
	 * @param obj object in node
	 */	
	public ActionMutableTreeNode(Object obj) {
		this(0, obj);
	}	
	
	/**
	 * constructor
	 * @param type type of node
	 */	
	public ActionMutableTreeNode(int type) {
		this(type, null);
	}

	/**
	 * constructor
	 * @param type type of node
	 * @param obj object in node
	 * @param allowsChildren can node have children?
	 */
	public ActionMutableTreeNode(int type, Object obj, boolean allowsChildren) {
		super(obj, allowsChildren);
		this.type = type;
	}    

	/**
	 * Gets type of node
	 * @return type
	 */
	public int getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		switch(type) {
		case 0:
			return "Actions";
		case 2:
		case 3:
		case 4:
		case 5:
			return Action.REFERENCES_NAMES[type-2];
		case 1:
		default:
			return super.toString();
		}
	}

}
