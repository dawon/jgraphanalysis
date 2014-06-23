package cz.dawon.java.library.graphConnectors;

/**
 * Handles clicks in graph
 * @author Jakub Zacek
 * @version 1.0
 * @param <I> data type of vertex identifier
 */
public interface IGraphClickListener<I> {

	/**
	 * On mouse down press
	 * @param vertex Vertex id
	 */
	public void onMouseDown(I vertex);
	
	/**
	 * On mouse up release
	 * @param vertex Vertex id
	 */
	public void onMouseUp(I vertex);
	
}
