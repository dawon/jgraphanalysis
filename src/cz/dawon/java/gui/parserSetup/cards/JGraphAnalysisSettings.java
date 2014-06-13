package cz.dawon.java.gui.parserSetup.cards;

import cz.dawon.java.library.parsers.XMLParser;

/**
 * Class storing all data between all cards in wizard - no need to use getters and setters, this class will be used just in the wizard and all the checks are made in wizard.
 * @author Jakub Zacek	
 * @version 1.2
 */
public class JGraphAnalysisSettings {
	
	/**
	 * true - single file, false - search in folder
	 */
	public boolean singleFile = true;
	
	/**
	 * path to file/folder
	 */
	public String path = "";
	
	/**
	 * do a recursive search?
	 */
	public boolean recursive = false;
	
	/**
	 * what file extension to look for?
	 */
	public String extension = "";
	
	/**
	 * {@link XMLParser} instance
	 */
	public XMLParser parser = new XMLParser();
	
	/**
	 * empty constructor
	 */
	public JGraphAnalysisSettings() {
	}

	/**
	 * Gets extension or null if no extension is selected
	 * @return extension
	 */
	public String getExtension() {
		if (extension != null && !extension.trim().isEmpty()) {
			return extension;
		}
		return null;
	}

}
