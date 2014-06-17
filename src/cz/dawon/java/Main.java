package cz.dawon.java;

import java.util.Iterator;

import javax.swing.JOptionPane;

import cz.dawon.java.gui.parserSetup.JGraphAnalysisSetup;
import cz.dawon.java.gui.parserSetup.JGraphAnalysisSetupListener;
import cz.dawon.java.library.Action;
import cz.dawon.java.library.JGraphAnalysis;

/**
 * Main example class
 * @author Jakub Zacek
 * @version 1.2
 */
public class Main {

	/**
	 * Entry point of program
	 * @param args arguments
	 */
	public static void main(String[] args) {
		start();
	}
	
	/**
	 * shows the wizard
	 */
	private static void start() {
		JGraphAnalysisSetup setup = new JGraphAnalysisSetup();
		setup.addJGraphAnalysisSetupListener(new JGraphAnalysisSetupListener() {
			@Override
			public void setupDone(JGraphAnalysis<String, String> jga) {
				onDone(jga);
			}
		});
		
		setup.showDialog();
	}

	/**
	 * When wizard id done
	 * @param jga {@link JGraphAnalysis}
	 */
	private static void onDone(JGraphAnalysis<String, String> jga) {
		String res = "Done with result:\n\n";
		
		for (Iterator<String> iterator = jga.getActions().keySet().iterator(); iterator.hasNext();) {
			Action<String, String> action = jga.getAction(iterator.next());
			res += action.toString() + "\n";
		}
		JOptionPane.showMessageDialog(null, res);
		
		int r = JOptionPane.showConfirmDialog(null, "Do you want to process another file?");
		if (r == 0) {
			start();
		}
	}

}
