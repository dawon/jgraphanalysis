package cz.dawon.java.gui.parserSetup.cards;

/**
 * Allows user to select Attribute or Node representing Tight Follower
 * @author Jakub Zacek
 * @version 1.1.2
 */
public class SelectTightFollowerCard extends SelectActionIdCard {

	private static final long serialVersionUID = -4263410178357911072L;

	@Override
	public int getCardId() {
		return 10;
	}
	
	@Override
	public String getCardTitle() {
		return "Select Tight Follower Node or Attribute";
	}

	@Override
	public boolean onNextPress() {
		if (!super.onNextPress()) {
			return false;
		}
		settings.parser.setTightFollowerSelector(selected);
		return true;
	}	
	
	@Override
	protected boolean checkIdSelected() {
		return true;
	}
}
