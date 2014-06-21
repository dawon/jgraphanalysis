package cz.dawon.java;

import java.awt.Component;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Custom style for {@link JTree} items
 * @author Jakub Zacek
 * @version 1.0
 */
public class ActionTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1774978931010072786L;

	/**
	 * root node {@link Icon}
	 */
	private static Icon rootIcon = null;
	

	/**
	 * action node {@link Icon}
	 */
	private static Icon actionIcon = null;	
	
	/**
	 * node {@link Icon}s
	 */
	private static Icon icons[] = new Icon[4];
	
	static {
		Arrays.fill(icons, null);
	}
	
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		ActionMutableTreeNode node = (ActionMutableTreeNode) value;
		
		Font f = this.getFont();
		
		switch (node.getType()) {
		case 0:
			this.setFont(new Font(f.getFamily(), Font.BOLD, f.getSize()));
			this.setIcon(getROOTIcon());			
			break;
		case 1:
			this.setFont(new Font(f.getFamily(), Font.BOLD, f.getSize()));
			this.setIcon(getActionIcon());	
			break;
		case 2:
		case 3:
		case 4:
		case 5:
			this.setFont(new Font(f.getFamily(), Font.ITALIC, f.getSize()));
			this.setIcon(getIcon(node.getType()-2));
			break;
		case 6:
			this.setFont(new Font(f.getFamily(), Font.PLAIN, f.getSize()));
			break;
		}
		
		return this;
	}
	
	/**
	 * Gets root node {@link Icon}
	 * @return {@link Icon}
	 */
	public static Icon getROOTIcon() {
		if (rootIcon == null) {
			rootIcon = new ImageIcon(ActionTreeCellRenderer.class.getResource("icons/ROOT.png"));
		}
		return rootIcon;
	}
	
	/**
	 * Gets action node {@link Icon}
	 * @return {@link Icon}
	 */
	public static Icon getActionIcon() {
		if (actionIcon == null) {
			actionIcon = new ImageIcon(ActionTreeCellRenderer.class.getResource("icons/A.png"));
		}
		return actionIcon;
	}
		
	
	public static String[] ICON_NAMES = new String[] {"P", "TP", "F", "TF"};
	
	/**
	 * Gets node {@link Icon}
	 * @return {@link Icon}
	 */
	public static Icon getIcon(int id) {
		if (icons[id] == null) {
			icons[id] = new ImageIcon(ActionTreeCellRenderer.class.getResource("icons/"+ICON_NAMES[id]+".png"));
		}
		return icons[id];
	}	
	
}
