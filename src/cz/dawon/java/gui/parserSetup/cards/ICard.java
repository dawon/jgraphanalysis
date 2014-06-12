package cz.dawon.java.gui.parserSetup.cards;

import java.awt.Container;

/**
 * Interface representating one "page" in setup wizard
 * @author Jakub Zacek
 * @version 1.0
 */
public interface ICard {

	/**
	 * Gets card identification
	 * @return card identification
	 */
	public int getCardId();
	
	/**
	 * Gets card title
	 * @return card title
	 */
	public String getCardTitle();
	
	/**
	 * Gets card content
	 * @return card content
	 */
	public Container getCardContent();
	
	/**
	 * Determines whether the card is first
	 * @return true when the card is first
	 */
	public boolean isFirst();
	
	/**
	 * Determines whether the card is last
	 * @return true when the card is last
	 */
	public boolean isLast();
	
	/**
	 * Called when 'Previous' button is pressed 
	 */
	public void onPrevPress();
	
	/**
	 * Called when 'Next' button is pressed 
	 */	
	public void onNextPress();
	
	/**
	 * Called when 'Cancel' button is pressed 
	 */	
	public void onCancelPress();
	
}
