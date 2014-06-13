package cz.dawon.java;

import java.util.Iterator;

import javax.swing.JOptionPane;

import cz.dawon.java.gui.parserSetup.JGraphAnalysisSetup;
import cz.dawon.java.gui.parserSetup.JGraphAnalysisSetupListener;
import cz.dawon.java.library.Action;
import cz.dawon.java.library.JGraphAnalysis;

public class Main {

	public static void main(String[] args) {
		JGraphAnalysisSetup setup = new JGraphAnalysisSetup();
		setup.addJGraphAnalysisSetupListener(new JGraphAnalysisSetupListener() {
			@Override
			public void setupDone(JGraphAnalysis<String, String> jga) {
				onDone(jga);
			}
		});
		
		setup.showDialog();
	}
	
	private static void onDone(JGraphAnalysis<String, String> jga) {
		String res = "Done with result:\n\n";
		
		for (Iterator<Action<String, String>> iterator = jga.getActions().iterator(); iterator.hasNext();) {
			Action<String, String> action = (Action<String, String>) iterator.next();
			res += action.toString() + "\n";
		}
		JOptionPane.showMessageDialog(null, res);
	}

}
