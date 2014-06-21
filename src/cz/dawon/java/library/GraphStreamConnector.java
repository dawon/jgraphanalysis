package cz.dawon.java.library;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

/**
 * Graph connector for library GraphStream
 * @author Jakub Zacek
 * @version 1.4
 */
public class GraphStreamConnector implements IGraphConnector<String> {

	/**
	 * Graph instance
	 */
	private MultiGraph graph;

	/**
	 * {@link Viewer} instance
	 */
	private Viewer viewer;

	/**
	 * unique int
	 */
	private int unique = 0;

	@Override
	public void createGraph(String name) {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph = new MultiGraph(name);
		try {
			String text = getFileContents("style/style.css");
			graph.removeAttribute("ui.stylesheet");
			graph.addAttribute("ui.stylesheet", text);
			graph.addAttribute("ui.quality");
			graph.addAttribute("ui.antialias");
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
		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);		
		viewer.enableAutoLayout();
		View view = viewer.addDefaultView(false);
		viewer.enableAutoLayout();
		return view;
	}

	@Override
	public String createEdgeId(String from, String to) {
		return from + "_" + to + Integer.toString(unique++);
	}

	@Override
	public void changeVertexStyle(String vertex, EStyle style) {
		Node n = graph.getNode(getVertexIdentifier(vertex));
		n.removeAttribute("ui.class");
		switch (style) {
		case DEFAULT:
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
	
	/**
	 * Reverts Identifier back to initial format
	 * @param identifier identifier
	 * @return id
	 */
	private String revertIdentifier(String identifier) {
		return identifier.substring(1);
	}


	@Override
	public void updateUI() {
		viewer.disableAutoLayout();
		viewer.enableAutoLayout();
	}

	@Override
	public List<String> getEdgesFromVertex(String vertex) {
		ArrayList<String> res = new ArrayList<String>();
		Node n = graph.getNode(getVertexIdentifier(vertex));
		if (n == null) {
			return res;
		}
		for (Iterator<Edge> iterator = n.getEachLeavingEdge().iterator(); iterator.hasNext();) {
			res.add(revertIdentifier(iterator.next().getId()));
		}
		return res;
	}

	@Override
	public String getEdgeTo(String edge) {
		return revertIdentifier(graph.getEdge(getEdgeIdentifier(edge)).getTargetNode().getId());
	}

	@Override
	public String getEdgeFrom(String edge) {
		return revertIdentifier(graph.getEdge(getEdgeIdentifier(edge)).getSourceNode().getId());
	}	
	
	/**
	 * Returns {@link MultiGraph} instance
	 * @return {@link MultiGraph} instance
	 */
	public MultiGraph getGraph() {
		return graph;
	}

}
