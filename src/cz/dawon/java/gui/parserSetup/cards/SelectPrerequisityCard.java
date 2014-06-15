package cz.dawon.java.gui.parserSetup.cards;

/**
 * Allows user to select Attribute or Node representing Prerequisity
 * @author Jakub Zacek
 * @version 1.3.1
 */
public class SelectPrerequisityCard extends SelectActionIdCard {

	private static final long serialVersionUID = -8685233344290195935L;
	
	@Override
	public int getCardId() {
		return 7;
	}
	
	@Override
	public String getCardTitle() {
		return "Select Prerequisity Node or Attribute";
	}
	
	@Override
	public boolean onNextPress() {
		if (!super.onNextPress()) {
			return false;
		}
		settings.parser.setPrerequisitySelector(selected);
		return true;
	}	
	
	@Override
	protected boolean checkIdSelected() {
		return true;
	}
}
