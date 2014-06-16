package cz.dawon.java.gui.parserSetup.cards;

import cz.dawon.java.library.Action;

/**
 * Allows user to select Attribute or Node representing {@link Action} Data, (Tight) Prerequisity and (Tight) Follower 
 * @author Jakub Zacek
 * @version 1.3
 */
public class SelectNodeCard extends SelectActionIdCard {

	private static final long serialVersionUID = 60851961647167187L;
	
	/**
	 * all card titles
	 */
	public static final String[] CARD_TITLES = new String[] {"Action Data", "Prerequisity", "Tight Prerequisity", "Follower", "Tight Follower"};
	
	/**
	 * which node/atribute selector ask for
	 */
	private int id;
	
	/**
	 * constructor
	 * @param id id of card to show
	 */
	public SelectNodeCard(int id) {
		super();
		this.id = id;
	}
	
	@Override
	public int getCardId() {
		return id + 6;
	}
	
	@Override
	public String getCardTitle() {
		return "Select "+CARD_TITLES[id]+" Node or Attribute";
	}
	
	@Override
	public boolean onNextPress() {
		if (!super.onNextPress()) {
			return false;
		}
		switch (id) {
		case 0:
			settings.parser.setActionDataSelector(selected);
			break;
		case 1:
			settings.parser.setPrerequisitySelector(selected);
			break;
		case 2:
			settings.parser.setTightPrerequisitySelector(selected);
			break;
		case 3:
			settings.parser.setFollowerSelector(selected);
			break;
		case 4:
			settings.parser.setTightFollowerSelector(selected);
			break;
		}
		return true;
	}	
	
	protected boolean checkIdSelected() {
		return true;		
	}
}
