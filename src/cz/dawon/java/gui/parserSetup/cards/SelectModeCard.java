package cz.dawon.java.gui.parserSetup.cards;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * First card allowing user to select whether to parse single file or folder with multiple files
 * @author Jakub Zacek
 * @version 1.1.0
 */
public class SelectModeCard extends JPanel implements ICard {

	private static final long serialVersionUID = 6985927522329253517L;

	/**
	 * selected mode, 1 - single file, 2 - folder
	 */
	private int mode = 1;
	
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
				mode = 1;
			}
		});
		
		multiRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mode = 2;
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
	public Container getCardContent() {
		return this;
	}

	@Override
	public boolean isFirst() {
		return true;
	}

	@Override
	public boolean isLast() {
		return false;
	}

	@Override
	public boolean onPrevPress() {
		return true;
	}

	@Override
	public boolean onNextPress() {
		return true;
	}

	@Override
	public boolean onCancelPress() {
		return true;
	}

	@Override
	public int getNextCardId() {
		return this.mode;
	}

	@Override
	public int getPrevCardId() {
		return -1;
	}

	@Override
	public Object getReturnArgs() {
		return null;
	}

	@Override
	public void args(Object args) {}

}
