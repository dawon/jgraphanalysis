package cz.dawon.java.library;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import cz.dawon.java.library.graphConnectors.EStyle;
import cz.dawon.java.library.graphConnectors.IGraphClickListener;
import cz.dawon.java.library.graphConnectors.IGraphConnector;
import cz.dawon.java.library.parsers.IFileParser;


/**
 * Main Class for the Library
 * @author Jakub Zacek
 * @version 1.5.5
 *
 * @param <I> datatype for Action's identifier
 * @param <D> datatype for Action's data
 */
public class JGraphAnalysis<I, D> {
	
	/**
	 * file parser instance
	 */
	private IFileParser<I, D> parser;	
	
	/**
	 * {@link IGraphConnector} instance
	 */
	private IGraphConnector<I> graph;

	/**
	 * {@link HashMap} of parsed Actions
	 */
	private Map<I, Action<I, D>> actions;
	
	
	/**
	 * class constructor
	 */
	public JGraphAnalysis() {
		this.actions = new HashMap<I, Action<I, D>>();
	}
	
	/**
	 * sets the {@link IFileParser} for this class
	 * @param parser {@link IFileParser}
	 */
	public void setParser(IFileParser<I, D> parser) {
		this.parser = parser;
	}
	
	/**
	 * gets the {@link IFileParser} instance for this class
	 * @return {@link IFileParser}
	 */
	public IFileParser<I, D> getParser() {
		return parser;
	}
	
	/**
	 * sets the {@link IGraphConnector} for this class
	 * @param connector {@link IGraphConnector}
	 */
	public void setGraphConnector(IGraphConnector<I> connector) {
		this.graph = connector;
	}
	
	
	/**
	 * gets the {@link IGraphConnector} instance for this class
	 * @return {@link IGraphConnector}
	 */
	public IGraphConnector<I> getGraphConnector() {
		return this.graph;
	}
	
	/**
	 * Gets extension of a file
	 * @param f file
	 * @return extension of the file
	 */
	public static String getExtension(File f) {
	    String extension = null;
	    String fileName = f.getName();
	    int i = fileName.lastIndexOf('.');

	    if (i > 0 &&  i < fileName.length() - 1) {
	        extension = fileName.substring(i+1).toLowerCase();
	    }
	    return extension;
	}	
	
	/**
	 * Adds Vertices into Graph based on {@link Action}s
	 */
	public void addVertices() {
		for (Iterator<I> iterator = actions.keySet().iterator(); iterator.hasNext();) {
			Action<I, D> action = getAction(iterator.next());
			graph.addVertex(action.getId());
			graph.addVertexData(action.getId(), "data", action.getData());
		}
		graph.addClickListener(new IGraphClickListener<I>() {
			@Override
			public void onMouseDown(I vertex) {
				selectVertex(vertex);
			}
			@Override
			public void onMouseUp(I vertex) {}
		});				
	}
	
	
	/**
	 * Parses single file
	 * @param fileName name and path of the file to parse
	 * @throws ParseException when problem with parsing
	 * @throws IOException problem with reading the file
	 */
	public void parse(String fileName) throws IOException, ParseException {
			for (Iterator<Action<I, D>> iterator = parser.parse(fileName).iterator(); iterator.hasNext();) {
				Action<I, D> a = iterator.next();
				actions.put(a.getId(), a);
			}
	}
	
	
	/**
	 * Search folder (when recursive is true - and subfolders) for files with specific extension (or all files when extension if null) and parses them
	 * @param path path of the folder to walk through - must not be null
	 * @param recursive should be walkthrough recursive?
	 * @param extension extension of files to parse, or null to parse all files
	 * @throws ParseException when problem with parsing
	 * @throws IOException problem with reading the file
	 */
	public void parseFolder(String path, boolean recursive, String extension) throws IOException, ParseException {
		if (path == null) {
			throw new NullPointerException("Path can't be null!");
		}
		File folder = new File(path);
		if (!folder.exists() || !folder.isDirectory()) {
			throw new InvalidParameterException("Path must exist and must be a directory! Exiting.");	
		}
		
	    for (final File file : folder.listFiles()) {
	        if (file.isDirectory() && recursive) {
	            parseFolder(file.getAbsolutePath(), recursive, extension);
	        } else if (extension == null || extension.equalsIgnoreCase(getExtension(file))) {
	            parse(file.getAbsolutePath());
	        }
	    }		
	}
	
	/**
	 * Creates instance of {@link PrecedenceGraphCreator}
	 * @return {@link PrecedenceGraphCreator} instance
	 */
	public PrecedenceGraphCreator<I, D> init() {
		return new PrecedenceGraphCreator<>(actions);
	}

	
	/**
	 * Processes the data loaded from xml
	 * @param pgc {@link PrecedenceGraphCreator} instance
	 * @throws NoSuchElementException when connection to {@link Action} does not exist
	 * @throws InvalidAlgorithmParameterException when solution does not exist
	 */
	public void process(PrecedenceGraphCreator<I, D> pgc) throws NoSuchElementException, InvalidAlgorithmParameterException {
		pgc.process();
		for (Iterator<PrecedenceGraphCreator<I, D>.VirtualEdge> iterator = pgc.getEdges().iterator(); iterator.hasNext();) {
			PrecedenceGraphCreator<I, D>.VirtualEdge conn = iterator.next();
			graph.addEdge(graph.createEdgeId(conn.getFrom(), conn.getTo()), conn.getFrom(), conn.getTo(), true);
		}
	}
	
	/**
	 * Processes the data loaded from xml
	 * @throws NoSuchElementException when connection to {@link Action} does not exist
	 * @throws InvalidAlgorithmParameterException when solution does not exist
	 */
	public void process() throws NoSuchElementException, InvalidAlgorithmParameterException {
		this.process(this.init());
	} 
	
	/**
	 * Selects Vertex ({@link Action}) with specified id
	 * @param id ID of {@link Action}
	 */
	public void selectVertex(I id) {
		Action<I, D> action = getAction(id);
		Action<I, D> a;
		for (Iterator<I> iterator = actions.keySet().iterator(); iterator.hasNext();) {
			a =  getAction(iterator.next());
			
			if (action != null && action.getId().equals(a.getId())) {
				graph.changeVertexStyle(a.getId(), EStyle.SELECTED);
			} else
			if (action != null && action.getPrerequisities().contains(a.getId())) {
				graph.changeVertexStyle(a.getId(), EStyle.PREREQUISITY);
			} else
			if (action != null && action.getTightPrerequisities().contains(a.getId())) {
				graph.changeVertexStyle(a.getId(), EStyle.TIGHT_PREREQUISITY);
			} else
			if (action != null && action.getFollowers().contains(a.getId())) {
				graph.changeVertexStyle(a.getId(), EStyle.FOLLOWER);
			} else
			if (action != null && action.getTightFollowers().contains(a.getId())) {
				graph.changeVertexStyle(a.getId(), EStyle.TIGHT_FOLLOWER);
			} else {
				graph.changeVertexStyle(a.getId(), EStyle.DEFAULT);
			}
		}
	}

	/**
	 * Gets {@link HashMap} of {@link Action}s
	 * @return {@link HashMap} of {@link Action}s
	 */
	public Map<I, Action<I, D>> getActions() {
		return actions;
	}
	
	/**
	 * Gets single {@link Action} described by id
	 * @param id id of {@link Action}
	 * @return {@link Action}
	 */
	public Action<I, D> getAction(I id) {
		return actions.get(id);
	}
	
	
}
