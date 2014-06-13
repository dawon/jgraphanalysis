package cz.dawon.java.gui.parserSetup.cards;

import javax.swing.JOptionPane;

import cz.dawon.java.library.Action;

/**
 * Allows user to select Node representing {@link Action}
 * @author Jakub Zacek
 * @version 1.0
 */
public class SelectActionCard extends AbstractSelectNodeCard {

	private static final long serialVersionUID = 5166187820123228952L;

	@Override
	public int getCardId() {
		return 3;
	}

	@Override
	public String getCardTitle() {
		return "Select Action Node";
	}

	@Override
	public int getNextCardId() {
		return 4;
	}

	@Override
	public int getPrevCardId() {
		return settings.singleFile ? 1 : 2;
	}

	@Override
	protected boolean getShowAttributes() {
		return false;
	}
	
	@Override
	public boolean onNextPress() {
		if (!super.onNextPress()) {
			return false;
		}
		if (selected == null) {
			JOptionPane.showMessageDialog(this, "You must select some Node representing Action!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		settings.parser.setActionSelector(selected);
		return true;
	}

}
