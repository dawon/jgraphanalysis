package cz.dawon.java.gui.parserSetup;

import java.util.EventListener;

import cz.dawon.java.library.JGraphAnalysis;

/**
 * Listener for JGraphAnalysis Setup
 * @author Jakub Zacek
 * @version 1.0
 */
public interface JGraphAnalysisSetupListener extends EventListener {

	/**
	 * Called when JGraphAnalysis is set up
	 * @param jga JGraphAnalysis
	 */
	public void setupDone(JGraphAnalysis<String, String> jga);
	
}
