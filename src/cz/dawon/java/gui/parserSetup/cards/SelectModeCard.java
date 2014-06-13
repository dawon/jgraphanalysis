package cz.dawon.java.gui.parserSetup.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

/**
 * First card allowing user to select whether to parse single file or folder with multiple files
 * @author Jakub Zacek
 * @version 1.4
 */
public class SelectModeCard extends AbstractCard {

	private static final long serialVersionUID = 6985927522329253517L;
	
	/**
	 * constructor
	 */
	public SelectModeCard() {
		createMainPanel();
	}

	/**
	 * creates main panel of this card
	 */
	private void createMainPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JRadioButton singleRB = new JRadioButton("Select single file to be parsed");
		JRadioButton multiRB = new JRadioButton("Select folder with files to be parsed");
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(singleRB);
		bg.add(multiRB);
		
		this.add(singleRB);
		this.add(multiRB);
		
		singleRB.setSelected(true);
		
		singleRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.singleFile = true;
			}
		});
		
		multiRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.singleFile = false;
			}
		});
	}

	@Override
	public int getCardId() {
		return 0;
	}

	@Override
	public String getCardTitle() {
		return "Parse Mode";
	}

	@Override
	public boolean isFirst() {
		return true;
	}

	@Override
	public int getNextCardId() {
		return this.settings.singleFile ? 1 : 2;
	}

}
