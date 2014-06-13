package cz.dawon.java.gui.parserSetup.cards;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;

import cz.dawon.java.library.JGraphAnalysis;

/**
 * Abstract card for selecting nodes
 * @author Jakub Zacek
 * @version 1.0
 */
public abstract class AbstractSelectNodeCard extends AbstractCard {

	private static final long serialVersionUID = 4441538401058976421L;
	
	protected JComboBox<String> filesCB;
	
	protected JTree nodesT;
	
	/**
	 * constructor
	 */
	public AbstractSelectNodeCard() {
		this.createMainPanel();
	}

	/**
	 * Creates main {@link JPanel}
	 */
	private void createMainPanel() {
		this.setLayout(new BorderLayout());
		
		JPanel sidePN = new JPanel(new BorderLayout());
		
		createComboBox();
		createJTree();
		
		sidePN.add(filesCB, BorderLayout.NORTH);
		sidePN.add(nodesT, BorderLayout.NORTH);
		
		
		this.add(sidePN, BorderLayout.WEST);
	}
	
	private void findFiles() {
		findFiles(settings.path);
	}
	
	private void findFiles(String path) {
		File f = new File(path);
		if (settings.singleFile) {
			filesCB.addItem(f.getAbsolutePath());
		} else {
		    for (final File file : f.listFiles()) {
		        if (file.isDirectory() && settings.recursive) {
		            findFiles(file.getAbsolutePath());
		        } else if (settings.getExtension() == null || settings.getExtension().equalsIgnoreCase(JGraphAnalysis.getExtension(file))) {
		            filesCB.addItem(file.getAbsolutePath());
		        }
		    }				
		}
	}
	
	private void loadFile() {
		if (filesCB.getSelectedIndex() != -1) {
			
		} else {
			JOptionPane.showMessageDialog(this, "No files found in selected folder. Can't continue... Please return one step back and check path entry...", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void createComboBox() {
		filesCB = new JComboBox<String>();
	}
	
	private void createJTree() {
		nodesT = new JTree();
	
		
		
	}
	
	@Override
	public boolean onNextPress() {
		if (filesCB.getItemCount() < 1) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public void onCardShow() {
		findFiles();
		if (filesCB.getItemCount() > 0) {
			filesCB.setSelectedIndex(0);
		}
		loadFile();
	}

	@Override
	public abstract int getCardId();

	@Override
	public abstract String getCardTitle();

	@Override
	public abstract int getNextCardId();

	@Override
	public abstract int getPrevCardId();

}