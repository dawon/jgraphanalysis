package cz.dawon.java.gui.parserSetup.cards;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Allows user to select single file to be parsed
 * @author Jakub Zacek
 * @version 1.6
 */
public class SelectFileCard extends JPanel implements ICard {

	private static final long serialVersionUID = -840790898114112531L;

	/**
	 * {@link JTextField} containing path to selected file
	 */
	private JTextField pathTF;
	
	/**
	 * {@link JGraphAnalysisSettings} instance
	 */
	private JGraphAnalysisSettings settings;	
	
	/**
	 * constructor
	 */
	public SelectFileCard() {
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
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		int res = fc.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			pathTF.setText(fc.getSelectedFile().getAbsolutePath());
		}
	}

	@Override
	public int getCardId() {
		return 1;
	}

	@Override
	public String getCardTitle() {
		return "Select File";
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
			JOptionPane.showMessageDialog(this, "You must select file to be parsed!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		File f = new File(path);
		if (!f.exists() || f.isDirectory()) {
			JOptionPane.showMessageDialog(this, "Selected file does not exists or is a folder!", "Error", JOptionPane.ERROR_MESSAGE);
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
