package cz.dawon.java.gui.parserSetup.cards;

/**
 * Allows user to select Attribute or Node representing Follower
 * @author Jakub Zacek
 * @version 1.2.1
 */
public class SelectFollowerCard extends SelectActionIdCard {

	private static final long serialVersionUID = -5630531187853280267L;

	@Override
	public int getCardId() {
		return 9;
	}
	
	@Override
	public String getCardTitle() {
		return "Select Follower Node or Attribute";
	}

	@Override
	public boolean onNextPress() {
		if (!super.onNextPress()) {
			return false;
		}
		settings.parser.setFollowerSelector(selected);
		return true;
	}	
	
	@Override
	protected boolean checkIdSelected() {
		return true;
	}
}
