package cz.dawon.java.gui.parserSetup.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileFilter;

/**
 * Lets user select whether to setup parser manually or from file
 * @author Jakub Zacek
 * @version 1.1.1
 */
public class ImportCard extends AbstractCard {

	private static final long serialVersionUID = -2884348305922445217L;

	/**
	 * constructor
	 */
	public ImportCard() {
		createMainPanel();
	}

	/**
	 * creates main panel of this card
	 */
	private void createMainPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JRadioButton manualRB = new JRadioButton("Setup parser manually");
		JRadioButton importRB = new JRadioButton("Import parser settings from file");

		ButtonGroup bg = new ButtonGroup();
		bg.add(manualRB);
		bg.add(importRB);

		this.add(manualRB);
		this.add(importRB);

		manualRB.setSelected(true);

		manualRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.manual = true;
			}
		});

		importRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.manual = false;
			}
		});
	}	

	/**
	 * Allows user to select file to import from
	 * @return path of file or null
	 */
	private String selectImportFile() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		FileFilter ff = new UniversalFileFilter("JGraphAnalysis Settings files", true).addExtension("gas");
		fc.addChoosableFileFilter(ff);
		fc.setFileFilter(ff);
		File f = new File(settings.path);
		if (!f.isDirectory()) {
			f = new File(f.getAbsolutePath());
		}
		fc.setCurrentDirectory(f);
		int res = fc.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	/**
	 * Imports parser settings from the file
	 * @return true on success false on error
	 */
	private boolean doImport() {
		String path = selectImportFile();
		if (path == null) {
			return false;
		}
		try {
			settings.parser.importFromFile(path);
			JOptionPane.showMessageDialog(this, "Successfully imported!", "Info", JOptionPane.INFORMATION_MESSAGE);
			return true;
		} catch (FileNotFoundException | ParseException e) {
			JOptionPane.showMessageDialog(this, "Unable to import settings from file! Please check, that the file is readable and is not corrupted...\n\n"+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}	
	}

	@Override
	public boolean onNextPress() {
		if (!settings.manual) {
			return doImport();
		}
		return true;
	}


	@Override
	public int getCardId() {
		return 3;
	}

	@Override
	public int getPrevCardId() {
		return settings.singleFile ? 1 : 2;
	}

	@Override
	public boolean isLast() {
		return !settings.manual;
	}

	@Override
	public String getCardTitle() {
		return "Import";
	}

}
