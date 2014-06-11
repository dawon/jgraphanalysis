package cz.dawon.java.library;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.dawon.java.library.parsers.IFileParser;


/**
 * Main Class for the Library
 * @author Jakub Zacek
 * @version 1.0
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
	
	private Set<Action<I, D>> actions;
	
	
	/**
	 * class constructor
	 */
	public JGraphAnalysis() {
		this.actions = new HashSet<Action<I, D>>();
	}
	
	/**
	 * sets the parser instance for this class
	 * @param parser parser instance
	 */
	public void setParser(IFileParser<I, D> parser) {
		this.parser = parser;
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
			actions.addAll(parser.parse(fileName));
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
	
	
}
