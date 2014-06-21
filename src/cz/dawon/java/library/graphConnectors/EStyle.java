package cz.dawon.java.library.graphConnectors;

/**
 * Values for styling Vertexes and Edges of graph
 * @author Jakub Zacek
 * @version 1.0
 */
public enum EStyle {
	/**
	 * default vertex/edge style
	 */
	DEFAULT,
	
	/**
	 * style for selected vertex
	 */
	SELECTED,
	
	/**
	 * style for selected vertexes prerequisity
	 */
	PREREQUISITY,
	
	/**
	 * style for selected vertexes tight prerequisity
	 */
	TIGHT_PREREQUISITY,
	
	/**
	 * style for selected vertexes follower
	 */	
	FOLLOWER,

	/**
	 * style for selected vertexes tight follower
	 */		
	TIGHT_FOLLOWER
}
