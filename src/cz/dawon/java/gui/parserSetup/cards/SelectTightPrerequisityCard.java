package cz.dawon.java.gui.parserSetup.cards;

/**
 * Allows user to select Attribute or Node representing Tight Prerequisity
 * @author Jakub Zacek
 * @version 1.1
 */
public class SelectTightPrerequisityCard extends SelectActionIdCard {

	private static final long serialVersionUID = 9124530787660430994L;

	@Override
	public int getCardId() {
		return 7;
	}
	
	@Override
	public String getCardTitle() {
		return "Select Tight Prerequisity Node or Attribute";
	}
	
	@Override
	public boolean onNextPress() {
		if (!super.onNextPress()) {
			return false;
		}
		settings.parser.setTightPrerequisitySelector(selected);
		return true;
	}		
}
