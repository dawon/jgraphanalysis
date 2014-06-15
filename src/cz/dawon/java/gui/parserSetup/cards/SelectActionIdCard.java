package cz.dawon.java.gui.parserSetup.cards;

import javax.swing.JOptionPane;

import cz.dawon.java.library.Action;
import cz.dawon.java.library.parsers.XMLParser.NodeSelector;

/**
 * Allows user to select Attribute or Node representing {@link Action} ID
 * @author Jakub Zacek
 * @version 1.2.3
 */
public class SelectActionIdCard extends AbstractSelectNodeCard {

	private static final long serialVersionUID = -1466867765369851390L;

	@Override
	protected boolean getShowAttributes() {
		return true;
	}

	@Override
	public int getCardId() {
		return 5;
	}

	@Override
	public String getCardTitle() {
		return "Select Action ID Node or Attribute";
	}

	@Override
	protected NodeSelector getRootNode() {
		return settings.parser.getActionSelector();
	}

	/**
	 * Checks whether Id node or attribute is selected
	 * @return true when is selected and can continue
	 */
	protected boolean checkIdSelected() {
		if (selected == null) {
			JOptionPane.showMessageDialog(this, "You must select some Node or Attribute representing Action ID!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		settings.parser.setActionIdSelector(selected);		
		return true;
	}
	
	@Override
	public boolean onNextPress() {
		if (!super.onNextPress() || !checkIdSelected()) {
			return false;
		}
		return true;
	}	
	
}
