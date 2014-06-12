package cz.dawon.java.gui.parserSetup.cards;

import java.awt.Container;

/**
 * Interface representating one "page" in setup wizard
 * @author Jakub Zacek
 * @version 1.1
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
	 * Gets the ID of next card
	 * @return ID of next card
	 */
	public int getNextCardId();

	/**
	 * Gets the ID of previous card
	 * @return ID of previous card
	 */	
	public int getPrevCardId();
	
	/**
	 * Gets outgoing arguments of this card
	 * @return arguments
	 */
	public Object getReturnArgs();
	
	/**
	 * Input arguments of this card
	 * @param args input arguments
	 */
	public void args(Object args);
	
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
