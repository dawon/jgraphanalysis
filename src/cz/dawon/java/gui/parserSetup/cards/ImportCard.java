package cz.dawon.java.gui.parserSetup.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import cz.dawon.java.library.parsers.XMLParser.NodeSelector;

/**
 * Lets user select whether to setup parser manually or from file
 * @author Jakub Zacek
 * @version 1.0
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
	 * Reads line and creates {@link NodeSelector} instance
	 * @param str line to be parsed
	 * @param mandatory when true throws {@link ParseException} when is not set up
	 * @return {@link NodeSelector}
	 * @throws ParseException when problem with parsing
	 */
	private NodeSelector read(String str, boolean mandatory) throws ParseException {
		if (str.trim().equals("-")) {
			if (mandatory) {
				throw new ParseException("This NodeSelector is mandatory!", 0);
			}
			return null;
		}
		return new NodeSelector(str);
	}
	
	/**
	 * Imports from the file
	 * @return true on success, false on fail
	 */
	private boolean doImport() {
		String path = selectImportFile();
		if (path == null) {
			return false;
		}
		File f = new File(path);
		try {
			Scanner sc = new Scanner(new FileReader(f));
			
			settings.parser.setActionSelector(read(sc.nextLine(), true));
			settings.parser.setActionIdSelector(read(sc.nextLine(), true));
			settings.parser.setActionDataSelector(read(sc.nextLine(), false));
			settings.parser.setPrerequisitySelector(read(sc.nextLine(), false));
			settings.parser.setTightPrerequisitySelector(read(sc.nextLine(), false));
			settings.parser.setFollowerSelector(read(sc.nextLine(), false));
			settings.parser.setTightFollowerSelector(read(sc.nextLine(), false));
			
			sc.close();
			JOptionPane.showMessageDialog(this, "Successfully imported!", "Info", JOptionPane.INFORMATION_MESSAGE);
			return true;
		} catch (FileNotFoundException | ParseException | NoSuchElementException e) {
			JOptionPane.showMessageDialog(this, "Unable to import settings from file! Please check, that the file is readable and is not corrupted...\n\n"+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	/**
	 * Allows user to select file to import from
	 * @return path of file or null
	 */
	private String selectImportFile() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		int res = fc.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile().getAbsolutePath();
		}
		return null;
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
