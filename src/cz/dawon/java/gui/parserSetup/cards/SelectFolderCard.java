package cz.dawon.java.gui.parserSetup.cards;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Allows user to select folder to be looked in
 * @author Jakub Zacek
 * @version 1.1
 */
public class SelectFolderCard extends JPanel implements ICard {

	private static final long serialVersionUID = 9215467740745285504L;

	/**
	 * {@link JTextField} containing path to selected folder
	 */
	private JTextField pathTF;
	
	/**
	 * {@link JGraphAnalysisSettings} instance
	 */
	private JGraphAnalysisSettings settings;	
	
	/**
	 * constructor
	 */
	public SelectFolderCard() {
		createMainPanel();
	}
	
	
	/**
	 * creates main panel
	 */
	private void createMainPanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel innerPN = new JPanel();
		innerPN.setLayout(new BoxLayout(innerPN, BoxLayout.LINE_AXIS));
		
		innerPN.add(new JLabel("Path: "));
		
		pathTF = new JTextField("");
		pathTF.setPreferredSize(new Dimension(400, 22));
		innerPN.add(pathTF);
	
		this.add(innerPN);
		
		JButton browseBTN = new JButton("Browse...");
		this.add(browseBTN);
		
		JCheckBox recursiveCB = new JCheckBox("Search recursively");
		this.add(recursiveCB);
		
		recursiveCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.recursive = ((JCheckBox)(e.getSource())).isSelected();
			}
		});
		
		browseBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				browse();
			}
		});
				
	}
	
	/**
	 * when Browse button is pressed
	 */
	private void browse() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		int res = fc.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			pathTF.setText(fc.getSelectedFile().getAbsolutePath());
		}
	}

	@Override
	public int getCardId() {
		return 2;
	}

	@Override
	public String getCardTitle() {
		return "Select Folder";
	}

	@Override
	public Container getCardContent() {
		return this;
	}

	@Override
	public boolean isFirst() {
		return false;
	}

	@Override
	public boolean isLast() {
		return false;
	}

	@Override
	public int getNextCardId() {
		return 3;
	}

	@Override
	public int getPrevCardId() {
		return 0;
	}

	@Override
	public void args(JGraphAnalysisSettings args) {
		this.settings = args;
	}

	@Override
	public boolean onPrevPress() {
		return true;
	}

	@Override
	public boolean onNextPress() {
		String path = pathTF.getText();
		if (path.trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "You must select folder!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		File f = new File(path);
		if (!f.exists() || !f.isDirectory()) {
			JOptionPane.showMessageDialog(this, "Selected folder does not exists or is not a folder!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;			
		}
		this.settings.path = f.getAbsolutePath();
		return true;
	}

	@Override
	public boolean onCancelPress() {
		return true;
	}

}
