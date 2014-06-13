package cz.dawon.java.gui.parserSetup.cards;

import cz.dawon.java.library.Action;

/**
 * Allows user to select Attribute or Node representing {@link Action} Data
 * @author Jakub Zacek
 * @version 1.0
 */
public class SelectActionDataCard extends SelectActionIdCard {

	private static final long serialVersionUID = 60851961647167187L;

	@Override
	public int getCardId() {
		return 5;
	}
	
	@Override
	public String getCardTitle() {
		return "Select Action Data Node or Attribute";
	}
	
	@Override
	public int getPrevCardId() {
		return 4;
	}
	
	@Override
	public int getNextCardId() {
		return 6;
	}
	
	@Override
	public boolean onNextPress() {
		if (!super.onNextPress()) {
			return false;
		}
		settings.parser.setActionDataSelector(selected);
		return true;
	}	
}