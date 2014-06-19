package cz.dawon.java.library;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Graph connector for library GraphStream
 * @author Jakub Zacek
 * @version 1.2
 */
public class GraphStreamConnector implements IGraphConnector<String> {

	/**
	 * Graph instance
	 */
	private SingleGraph graph;

	@Override
	public void createGraph(String name) {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph = new SingleGraph(name);
		try {
			String text = getFileContents("style/style.css");
			graph.removeAttribute("ui.stylesheet");
			graph.addAttribute("ui.stylesheet", text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads style from css from resource
	 * @param path path to css file
	 * @return text of css file
	 * @throws IOException problem reading file
	 */
	public String getFileContents(String path) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));

		StringBuffer stringBuffer = new StringBuffer();
		String line = null;

		while((line = bufferedReader.readLine())!=null){

			stringBuffer.append(line).append("\n");
		}

		return stringBuffer.toString();
	}

	@Override
	public void addGraphData(String id, Object value) {
		graph.addAttribute(id, value);
	}

	@Override
	public Object getGraphData(String id) {
		return graph.getAttribute(id);
	}

	@Override
	public void removeGraphData(String id) {
		graph.removeAttribute(id);
	}

	@Override
	public void addVertex(String vertex) {
		Node n = graph.addNode(getVertexIdentifier(vertex));
		n.addAttribute("label", vertex);
	}

	@Override
	public void removeVertex(String vertex) {
		graph.removeNode(getVertexIdentifier(vertex));
	}

	@Override
	public boolean existVertex(String vertex) {
		return graph.getNode(getVertexIdentifier(vertex)) != null;
	}

	@Override
	public void addVertexData(String vertex, String id, Object value) {
		graph.getNode(getVertexIdentifier(vertex)).addAttribute(id, value);
	}

	@Override
	public Object getVertexData(String vertex, String id) {
		return graph.getNode(getVertexIdentifier(vertex)).getAttribute(id);
	}

	@Override
	public void removeVertexData(String vertex, String id) {
		graph.getNode(getVertexIdentifier(vertex)).removeAttribute(id);
	}

	@Override
	public void addEdge(String edge, String from, String to, boolean oriented) {
		graph.addEdge(getEdgeIdentifier(edge), getVertexIdentifier(from), getVertexIdentifier(to), oriented);
	}

	@Override
	public void removeEdge(String edge) {
		graph.removeEdge(getEdgeIdentifier(edge));
	}

	@Override
	public boolean existEdge(String from, String to) {
		return graph.getNode(getVertexIdentifier(from)).hasEdgeToward(getVertexIdentifier(to));
	}

	@Override
	public boolean existEdge(String edge) {
		return graph.getEdge(getEdgeIdentifier(edge)) != null;
	}

	@Override
	public void addEdgeData(String edge, String id, Object value) {
		graph.getEdge(getEdgeIdentifier(edge)).addAttribute(id, value);
	}

	@Override
	public Object getEdgeData(String edge, String id) {
		return graph.getEdge(getEdgeIdentifier(edge)).getAttribute(id);
	}

	@Override
	public void removeEdgeData(String edge, String id) {
		graph.getEdge(getEdgeIdentifier(edge)).removeAttribute(id);
	}

	@Override
	public Component getComponent() {
		graph.display();
		// TODO
		return null;
	}

	@Override
	public String createEdgeId(String from, String to) {
		return from + "_" + to;
	}

	@Override
	public void changeVertexStyle(String vertex, EStyle style) {
		Node n = graph.getNode(getVertexIdentifier(vertex));
		switch (style) {
		case DEFAULT:
			n.removeAttribute("ui.class");
			break;
		case SELECTED:
			n.setAttribute("ui.class", "selected");
			break;
		case PREREQUISITY:
			n.setAttribute("ui.class", "prerequisity");
			break;
		case TIGHT_PREREQUISITY:
			n.setAttribute("ui.class", "tightPrerequisity");
			break;
		case FOLLOWER:
			n.setAttribute("ui.class", "follower");
			break;
		case TIGHT_FOLLOWER: 
			n.setAttribute("ui.class", "tightFollower");
			break;
		}
	}

	@Override
	public void changeEdgeStyle(String edge, EStyle style) {}

	/**
	 * Gets Vertex identifier
	 * @param id id
	 * @return identifier
	 */
	private String getVertexIdentifier(String id) {
		return "n" + id;
	}

	/**
	 * Gets Edge identifier
	 * @param id id
	 * @return identifier
	 */	
	private String getEdgeIdentifier(String id) {
		return "e" + id;
	}	

}
