package cz.dawon.java.gui.parserSetup.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Lets user export settings to file
 * @author Jakub Zacek
 * @version 1.1
 */
public class ExportCard extends AbstractCard {

	private static final long serialVersionUID = 2492862283759120223L;

	/**
	 * constructor
	 */
	public ExportCard() {
		createMainPanel();
	}

	/**
	 * creates main panel of this card
	 */
	private void createMainPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JButton exportBTN = new JButton("Export parser settings to file");
		
		exportBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doExport();
			}
		});
		
		this.add(exportBTN);
	}		
	
	/**
	 * Allows user to select file to export to
	 * @return path of file or null
	 */
	private String selectExportFile() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		int res = fc.showSaveDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile().getAbsolutePath();
		}
		return null;
	}	
	
	/**
	 * Exports to the file
	 */
	private void doExport() {
		String path = selectExportFile();
		if (path == null) {
			return;
		}
		
		try {
			settings.parser.exportToFile(path);
			JOptionPane.showMessageDialog(this, "Successfully exported!", "Info", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Unable to export file! Check, that the destination file is writeable...", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}	
	
	@Override
	public int getCardId() {
		return 11;
	}

	@Override
	public String getCardTitle() {
		return "Export";
	}
	
	@Override
	public boolean isLast() {
		return true;
	}

}
