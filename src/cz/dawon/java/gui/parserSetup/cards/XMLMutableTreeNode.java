package cz.dawon.java.gui.parserSetup.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Node;

import cz.dawon.java.library.parsers.XMLParser;
import cz.dawon.java.library.parsers.XMLParser.NodeSelector;

/**
 * Modified {@link DefaultMutableTreeNode} to be used with {@link Node}
 * @author Jakub Zacek
 * @version 1.0
 */
public class XMLMutableTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 3837676570798233740L;

	/**
	 * XML {@link Node} to be represented by this {@link XMLMutableTreeNode}
	 */
	transient protected Node node;

	/**
	 * constructor
	 */
	public XMLMutableTreeNode() {
		super(null);
	}

	/**
	 * constructor
	 * @param node {@link Node}
	 */
	public XMLMutableTreeNode(Node node) {
		this(node, true);
	}	

	/**
	 * constructor
	 * @param node user defined Object
	 */
	public XMLMutableTreeNode(Object obj) {
		super(obj, true);
		node = null;
	}	


	/**
	 * constructor
	 * @param node {@link Node}
	 * @param allowsChildren does this Node allow children?
	 */
	public XMLMutableTreeNode(Node node, boolean allowsChildren) {
		super(null, allowsChildren);
		this.node = node;
	}    

	/**
	 * Gets {@link Node}
	 * @return {@link Node}
	 */
	public Node getNode() {
		return this.node;
	}

	/**
	 * Sets {@link Node}
	 * @param node {@link Node}
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * Generates {@link NodeSelector} for this node
	 * @return {@link NodeSelector}
	 */
	public NodeSelector getNodeSelector() {
		NodeSelector ns = new NodeSelector();
		//Node n = node;
		XMLMutableTreeNode n = this;
		if (node == null) {
			return null;
		}
		ns.attributeName = null;
		if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
			ns.attributeName = node.getNodeName();
			if (this.getParent() != null) {
				n = (XMLMutableTreeNode)this.getParent();
				ns.nodeName = n.getNode().getNodeName();
			}
		} else {
			ns.nodeName = node.getNodeName();
		}
		n = (XMLMutableTreeNode)n.getParent();
		List<String> parents = new ArrayList<String>();
		while (n != null && n.getNode() != null && n.getNode().getNodeType() == Node.ELEMENT_NODE) {
			parents.add(n.getNode().getNodeName());
			n = (XMLMutableTreeNode)n.getParent();
		}
		if (parents.size() == 0) {
			ns.parents = null;
		} else {
			Collections.reverse(parents);
			ns.parents = XMLParser.implode(".", Arrays.copyOf(parents.toArray(), parents.toArray().length, String[].class));
		}
		return ns;
	}

	@Override
	public String toString() {
		if (node == null) {
			return super.toString();
		}
		if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
			return "attr:" + node.getNodeName();
		}
		return "<"+node.getNodeName()+">";
	}
}
