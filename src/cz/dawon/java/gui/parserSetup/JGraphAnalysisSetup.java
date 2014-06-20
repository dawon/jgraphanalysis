package cz.dawon.java.gui.parserSetup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

import cz.dawon.java.library.JGraphAnalysis;

/**
 * Shows GUI allowing user to setup JGraphAnalysis and XML parser and then returning a JGraphAnalysis instance
 * @author Jakub Zacek
 * @version 1.1
 */
public class JGraphAnalysisSetup {

	/**
	 * List of listeners
	 */
	private List<JGraphAnalysisSetupListener> listeners;
	
	/**
	 * parent {@link JFrame}
	 */
	private JFrame parent;
	
	/**
	 * constructor
	 * @param parent parent {@link JFrame}
	 */
	public JGraphAnalysisSetup(JFrame parent) {
		this.parent = parent;
	}
	
	
	
	/**
	 * Adds new JGraphAnalysisSetupListener
	 * @param listener listener
	 */
	public void addJGraphAnalysisSetupListener(JGraphAnalysisSetupListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<JGraphAnalysisSetupListener>();
		}
		listeners.add(listener);
	}
	
	/**
	 * Shows the GUI
	 */
	public void showDialog() {
		new MainWindow(parent, this).showWindow();
	}
	
	
	/**
	 * Called when setup is done
	 * @param jga setup parser
	 */
	public void setUpDone(JGraphAnalysis<String, String> jga) {
		if (listeners != null) {
			for (Iterator<JGraphAnalysisSetupListener> iterator = listeners.iterator(); iterator.hasNext();) {
				iterator.next().setupDone(jga);
			}
		}
	}
	
	
	
	
}
