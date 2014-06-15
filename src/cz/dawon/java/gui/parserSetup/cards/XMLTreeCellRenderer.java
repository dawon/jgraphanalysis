package cz.dawon.java.gui.parserSetup.cards;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.w3c.dom.Node;

/**
 * Custom style for {@link JTree} items
 * @author Jakub Zacek
 * @version 1.0
 */
public class XMLTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1774978931010072786L;

	/**
	 * root node {@link Icon}
	 */
	private static Icon xmlIcon = null;
	
	/**
	 * node {@link Icon}
	 */
	private static Icon nodeIcon = null;
	
	/**
	 * attribute {@link Icon}
	 */
	private static Icon attrIcon = null;
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		XMLMutableTreeNode node = (XMLMutableTreeNode) value;
		
		Font f = this.getFont();
		
		if (node.getNode() == null) { // root element
			this.setFont(new Font(f.getFamily(), Font.BOLD, f.getSize()));
			this.setIcon(getXMLIcon());
		} else {
			if (node.getNode().getNodeType() == Node.ELEMENT_NODE) { // node
				this.setFont(new Font(f.getFamily(), Font.PLAIN, f.getSize()));
				this.setIcon(getNodeIcon());	
			} else { // attribute
				this.setFont(new Font(f.getFamily(), Font.ITALIC, f.getSize()));
				this.setIcon(getAttrIcon());
			}
		}
		
		return this;
	}
	
	/**
	 * Gets root node {@link Icon}
	 * @return {@link Icon}
	 */
	public static Icon getXMLIcon() {
		if (xmlIcon == null) {
			xmlIcon = new ImageIcon(XMLTreeCellRenderer.class.getResource("ico/xml.png"));
		}
		return xmlIcon;
	}
	
	/**
	 * Gets node {@link Icon}
	 * @return {@link Icon}
	 */
	public static Icon getNodeIcon() {
		if (nodeIcon == null) {
			nodeIcon = new ImageIcon(XMLTreeCellRenderer.class.getResource("ico/node.png"));
		}
		return nodeIcon;
	}	
	
	/**
	 * Gets attribute {@link Icon}
	 * @return {@link Icon}
	 */
	public static Icon getAttrIcon() {
		if (attrIcon == null) {
			attrIcon = new ImageIcon(XMLTreeCellRenderer.class.getResource("ico/attr.png"));
		}
		return attrIcon;
	}	

}
