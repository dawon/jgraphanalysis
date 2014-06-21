package cz.dawon.java.library;

import java.awt.Component;
import java.util.List;

/**
 * Used to implement Graph functions to specific library
 * @author Jakub Zacek
 * @version 1.3
 * @param <I> data type of Vertex and Edge identificators
 */
public interface IGraphConnector<I> {

	/**
	 * Creates new graph
	 * @param name graph name
	 */
	public void createGraph(I name);

	/**
	 * Adds some data to the graph
	 * @param id data identificator
	 * @param value data
	 */
	public void addGraphData(String id, Object value);
	
	/**
	 * Gets data from the graph
	 * @param id data identificator
	 * @return data
	 */
	public Object getGraphData(String id);
	
	/**
	 * Removes data from the graph
	 * @param id data identificator
	 */	
	public void removeGraphData(String id);	
	
	/**
	 * Adds vertex to the graph
	 * @param vertex vertex identificator
	 */
	public void addVertex(I vertex);
	
	/**
	 * Removes vertex to the graph
	 * @param vertex vertex identificator
	 */	
	public void removeVertex(I vertex);
	
	/**
	 * Returns true if vertex with specified id exists
	 * @param vertex vertex identificator
	 * @return true if exists, false otherwise
	 */
	public boolean existVertex(I vertex);
	
	/**
	 * Adds some data to vertex
	 * @param vertex vertex identificator
	 * @param id data identificator
	 * @param value data
	 */
	public void addVertexData(I vertex, String id, Object value);
	
	/**
	 * Gets some data from vertex
	 * @param vertex vertex identificator
	 * @param id data identificator
	 * @return data
	 */	
	public Object getVertexData(I vertex, String id);
	
	/**
	 * Removes data from vertex
	 * @param vertex vertex identificator
	 * @param id data identificator
	 */		
	public void removeVertexData(I vertex, String id);	
	
	/**
	 * Changes style of a vertex
	 * @param vertex vertex identificator
	 * @param style {@link EStyle}
	 */
	public void changeVertexStyle(I vertex, EStyle style);
	
	/**
	 * Returns {@link List} of Edges that has oriented Edge from this Vertex
	 * @param vertex initial Vertex
	 * @return {@link List} of Edges
	 */
	public List<I> getEdgesFromVertex(I vertex);
	
	/**
	 * Adds edge to graph
	 * @param edge edge identificator
	 * @param from first vertex identificator
	 * @param to second vertex identificator
	 * @param oriented should this edge be oriented?
	 */
	public void addEdge(I edge, I from, I to, boolean oriented);
	
	/**
	 * Removes edge from graph
	 * @param edge edge identificator
	 */
	public void removeEdge(I edge);
	
	/**
	 * Returns true if edge between two vertices exists
	 * @param from first vertex identificator
	 * @param to second vertex identificator
	 * @return true if exists, false otherwise
	 */	
	public boolean existEdge(I from, I to);
	
	/**
	 * Returns true if edge with specified id exists
	 * @param edge edge identificator
	 * @return true if exists, false otherwise
	 */		
	public boolean existEdge(I edge);
	
	/**
	 * Returns ID of Vertex Edge is leading to
	 * @param edge Edge ID
	 * @return Vertex Vertex ID
	 */
	public I getEdgeTo(I edge);

	/**
	 * Returns ID of Vertex Edge is leading from
	 * @param edge Edge ID
	 * @return Vertex Vertex ID
	 */
	public I getEdgeFrom(I edge);	
	
	/**
	 * Adds some data to edge
	 * @param edge edge identificator
	 * @param id data identificator
	 * @param value data
	 */	
	public void addEdgeData(I edge, String id, Object value);
	
	/**
	 * Gets some data from edge
	 * @param edge edge identificator
	 * @param id data identificator
	 * @return data
	 */		
	public Object getEdgeData(I edge, String id);
	
	/**
	 * Removes data from edge
	 * @param edge edge identificator
	 * @param id data identificator
	 */			
	public void removeEdgeData(I edge, String id);		
	
	/**
	 * Changes style of a edge
	 * @param edge edge identificator
	 * @param style {@link EStyle}
	 */
	public void changeEdgeStyle(I edge, EStyle style);	
	
	/**
	 * Creates from two IDs one unique ID for edge
	 * @param from first vertex identificator
	 * @param to second vertex identificator
	 * @return identificator for edge
	 */
	public I createEdgeId(I from, I to);
	
	/**
	 * Gets {@link Component} containing rendered graph
	 * @return {@link Component}
	 */
	public Component getComponent();
	
	/**
	 * Updates Graph Visualisation UI
	 */
	public void updateUI();
	
}
