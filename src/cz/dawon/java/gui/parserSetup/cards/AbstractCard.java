package cz.dawon.java.gui.parserSetup.cards;

import java.awt.Container;

import javax.swing.JPanel;

/**
 * Abstract implementation for {@link ICard}
 * @author Jakub Zacek
 * @version 1.1
 */
public abstract class AbstractCard extends JPanel implements ICard {

	private static final long serialVersionUID = -3008670597768478751L;
	
	/**
	 * {@link JGraphAnalysisSettings} instance
	 */
	protected JGraphAnalysisSettings settings;		

	@Override
	public abstract int getCardId();

	@Override
	public abstract String getCardTitle();

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
	public abstract int getNextCardId();

	@Override
	public abstract int getPrevCardId();

	@Override
	public void args(JGraphAnalysisSettings args) {
		this.settings = args;
	}

	@Override
	public boolean onPrevPress() {
		return true;
	}

	@Override
	public boolean onNextPress() {
		return true;
	}

	@Override
	public boolean onCancelPress() {
		return true;
	}
	
	@Override
	public void onCardShow() {}

}
