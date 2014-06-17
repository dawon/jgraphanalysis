package cz.dawon.java.library;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.dawon.java.library.parsers.IFileParser;


/**
 * Main Class for the Library
 * @author Jakub Zacek
 * @version 1.0.3
 *
 * @param <I> datatype for Action's identifier
 * @param <D> datatype for Action's data
 */
public class JGraphAnalysis<I, D> {

	/**
	 * Logger instance
	 */
	private final static Logger LOGGER = Logger.getLogger(JGraphAnalysis.class.getName());
	
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
	 * Parses single file
	 * @param fileName name and path of the file to parse
	 */
	public void parse(String fileName) {
		LOGGER.logp(Level.INFO, "JGraphAnalysis", "parse", "Parsing file '"+fileName+"'...");
		try {
			for (Iterator<Action<I, D>> iterator = parser.parse(fileName).iterator(); iterator.hasNext();) {
				Action<I, D> a = iterator.next();
				actions.put(a.getId(), a);
			}
		} catch (IOException e) {
			LOGGER.logp(Level.WARNING, "JGraphAnalysis", "parse", "Can't read file '"+fileName+"'! Skipping.", e);
		} catch (ParseException e) {
			LOGGER.logp(Level.WARNING, "JGraphAnalysis", "parse", "Can't parse file '"+fileName+"'! Skipping.", e);
		}
		LOGGER.logp(Level.INFO, "JGraphAnalysis", "parse", "File '"+fileName+"' successfully parsed...");
	}
	
	
	/**
	 * Search folder (when recursive is true - and subfolders) for files with specific extension (or all files when extension if null) and parses them
	 * @param path path of the folder to walk through - must not be null
	 * @param recursive should be walkthrough recursive?
	 * @param extension extension of files to parse, or null to parse all files
	 */
	public void parseFolder(String path, boolean recursive, String extension) {
		if (path == null) {
			LOGGER.logp(Level.SEVERE, "JGraphAnalysis", "parseFolder", "Parameter 'path' can't be NULL. Exiting.");
			return;
		}
		LOGGER.logp(Level.INFO, "JGraphAnalysis", "parseFolder", "Opening folder '"+path+"'...");
		File folder = new File(path);
		if (!folder.exists() || !folder.isDirectory()) {
			LOGGER.logp(Level.SEVERE, "JGraphAnalysis", "parseFolder", "Path must exist and must be a directory! Exiting.");
			return;			
		}
		
	    for (final File file : folder.listFiles()) {
	        if (file.isDirectory() && recursive) {
	            parseFolder(file.getAbsolutePath(), recursive, extension);
	        } else if (extension == null || extension.equalsIgnoreCase(getExtension(file))) {
	            parse(file.getAbsolutePath());
	        }
	    }		
	    LOGGER.logp(Level.INFO, "JGraphAnalysis", "parseFolder", "Finished folder '"+path+"'...");
	}
	
	private void checkIndexes(Set<I> actionIds) throws NoSuchElementException {
		I id;
		for (Iterator<I> iterator = actionIds.iterator(); iterator.hasNext();) {
			id = iterator.next();
			if (actions.get(id) == null) {
				throw new NoSuchElementException("Can't find Action with ID '"+id.toString()+"'!");
			}
		}
	}
	
	
	public void process() throws NoSuchElementException {
		Action<I, D> a;
		for (Iterator<I> iterator = actions.keySet().iterator(); iterator.hasNext();) {
			a = getAction(iterator.next());
			checkIndexes(a.getPrerequisities());
			checkIndexes(a.getTightPrerequisities());
			checkIndexes(a.getFollowers());
			checkIndexes(a.getTightFollowers());
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
