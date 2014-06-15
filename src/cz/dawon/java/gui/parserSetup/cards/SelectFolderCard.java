package cz.dawon.java.gui.parserSetup.cards;

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
 * @version 1.3.1
 */
public class SelectFolderCard extends AbstractCard {

	private static final long serialVersionUID = 9215467740745285504L;

	/**
	 * {@link JTextField} containing path to selected folder
	 */
	private JTextField pathTF;

	/**
	 * {@link JTextField} containing extension
	 */
	private JTextField extensionTF;	
	
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
		
		this.createPathJTextField();		
		
		this.createCheckBox();
		
		this.createExtensionTextField();
			
	}


	/**
	 * Creates {@link JTextField} and {@link JButton} for path selection
	 */
	private void createPathJTextField() {
		JPanel innerPN = new JPanel();
		innerPN.setLayout(new BoxLayout(innerPN, BoxLayout.LINE_AXIS));
		
		innerPN.add(new JLabel("Path: "));
		
		pathTF = new JTextField("");
		pathTF.setPreferredSize(new Dimension(400, 22));
		innerPN.add(pathTF);
	
		this.add(innerPN);
		
		JButton browseBTN = new JButton("Browse...");
		this.add(browseBTN);		
		
		browseBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				browse();
			}
		});
	}


	/**
	 * Creates {@link JTextField} for extension selection
	 */
	private void createExtensionTextField() {
		JPanel innerPN = new JPanel();
		innerPN.setLayout(new BoxLayout(innerPN, BoxLayout.LINE_AXIS));
		
		innerPN.add(new JLabel("Extension of files to be parsed (ie. xml): "));		
		
		extensionTF = new JTextField("");
		extensionTF.setPreferredSize(new Dimension(100, 22));
		innerPN.add(extensionTF);	
		
		innerPN.add(new JLabel(" (leave blank for all files)"));		

		this.add(innerPN);
	}
	
	/**
	 * Creates {@link JCheckBox} for recursive selection
	 */
	private void createCheckBox() {
		JCheckBox recursiveCB = new JCheckBox("Search recursively");
		this.add(recursiveCB);
		
		recursiveCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.recursive = ((JCheckBox)(e.getSource())).isSelected();
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
	public int getPrevCardId() {
		return 0;
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
		this.settings.extension = this.extensionTF.getText();
		return true;
	}
}
