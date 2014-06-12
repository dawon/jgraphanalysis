package cz.dawon.java.gui.parserSetup.cards;

import java.awt.Container;

import javax.swing.JPanel;

/**
 * Allows user to select single file to be parsed
 * @author Jakub Zacek
 * @version 1.0
 */
public class SelectFileCard extends JPanel implements ICard {

	private static final long serialVersionUID = -840790898114112531L;

	@Override
	public int getCardId() {
		return 1;
	}

	@Override
	public String getCardTitle() {
		return "Select File";
	}

	@Override
	public Container getCardContent() {
		return this;
	}

	@Override
	public boolean isFirst() {
		return false;
	}

	@Override
	public boolean isLast() {
		return false;
	}

	@Override
	public int getNextCardId() {
		return 0; //TODO
	}

	@Override
	public int getPrevCardId() {
		return 0;
	}

	@Override
	public Object getReturnArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void args(Object args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrevPress() {}

	@Override
	public void onNextPress() {}

	@Override
	public void onCancelPress() {}

}
