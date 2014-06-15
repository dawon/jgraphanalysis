package cz.dawon.java.gui.parserSetup.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import cz.dawon.java.library.parsers.XMLParser.NodeSelector;

/**
 * Lets user export settings to file
 * @author Jakub Zacek
 * @version 1.0
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
	 * Creates string from {@link NodeSelector}
	 * @param ns {@link NodeSelector}
	 * @return output string
	 */
	private String write(NodeSelector ns) {
		if (ns == null) {
			return "-";
		}
		return ns.toString();
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
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			
			out.write(write(settings.parser.getActionSelector())); out.newLine();
			out.write(write(settings.parser.getActionIdSelector())); out.newLine();
			out.write(write(settings.parser.getActionDataSelector())); out.newLine();
			out.write(write(settings.parser.getPrerequisitySelector())); out.newLine();
			out.write(write(settings.parser.getTightPrerequisitySelector())); out.newLine();
			out.write(write(settings.parser.getFollowerSelector())); out.newLine();
			out.write(write(settings.parser.getTightFollowerSelector())); out.newLine();
			
			out.close();
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
