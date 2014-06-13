package cz.dawon.java.gui.parserSetup.cards;

import cz.dawon.java.library.parsers.XMLParser;

/**
 * Class storing all data between all cards in wizard - no need to use getters and setters, this class will be used just in the wizard and all the checks are made in wizard.
 * @author Jakub Zacek	
 * @version 1.0
 */
public class JGraphAnalysisSettings {
	
	/**
	 * true - single file, false - search in folder
	 */
	public boolean singleFile;
	
	/**
	 * path to file/folder
	 */
	public String path;
	
	/**
	 * do a recursive search?
	 */
	public boolean recursive;
	
	/**
	 * what file extension to look for?
	 */
	public String extension;
	
	/**
	 * XML parser instance
	 */
	public XMLParser parser;
	
	/**
	 * empty constructor
	 */
	public JGraphAnalysisSettings() {
	}

}
