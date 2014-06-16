package cz.dawon.java.gui.parserSetup.cards;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cz.dawon.java.library.JGraphAnalysis;
import cz.dawon.java.library.parsers.XMLParser;
import cz.dawon.java.library.parsers.XMLParser.NodeSelector;

/**
 * Abstract card for selecting nodes
 * @author Jakub Zacek
 * @version 1.2.5
 */
public abstract class AbstractSelectNodeCard extends AbstractCard {

	private static final long serialVersionUID = 4441538401058976421L;

	/**
	 * {@link JComboBox} for selecting file
	 */
	protected JComboBox<String> filesCB;

	/**
	 * {@link JTree} model
	 */
	protected DefaultTreeModel model;

	/**
	 * {@link JTree} containing xml elements
	 */
	protected JTree nodesT;
	
	/**
	 * {@link JLabel} showing node name
	 */
	protected JLabel nodeNameLB = new JLabel();
	/**
	 * {@link JLabel} showing attribute name
	 */
	protected JLabel attrNameLB = new JLabel();
	/**
	 * {@link JLabel} showing node parents
	 */
	protected JLabel pathLB = new JLabel();
	
	/**
	 * Result selector
	 */
	protected NodeSelector selected = null;

	/**
	 * constructor
	 */
	public AbstractSelectNodeCard() {
		this.createMainPanel();
	}

	/**
	 * Creates main {@link JPanel}
	 */
	private void createMainPanel() {
		this.setLayout(new BorderLayout());

		JPanel sidePN = new JPanel(new BorderLayout());

		createComboBox();

		sidePN.add(filesCB, BorderLayout.NORTH);
		sidePN.add(createJTree(), BorderLayout.CENTER);
		
		JPanel detailPN = new JPanel();
		detailPN.setLayout(new BoxLayout(detailPN, BoxLayout.PAGE_AXIS));

		detailPN.add(nodeNameLB);
		if (this.getShowAttributes()) {
			detailPN.add(attrNameLB);
		}
		detailPN.add(pathLB);

		this.add(sidePN, BorderLayout.WEST);
		this.add(detailPN, BorderLayout.CENTER);
	}

	/**
	 * Finds all files in folder
	 * @param path path
	 */
	protected void findFiles(String path) {
		File f = new File(path);
		filesCB.removeAllItems();
		if (settings.singleFile) {
			filesCB.addItem(f.getAbsolutePath());
		} else {
			for (final File file : f.listFiles()) {
				if (file.isDirectory() && settings.recursive) {
					findFiles(file.getAbsolutePath());
				} else if (settings.getExtension() == null || settings.getExtension().equalsIgnoreCase(JGraphAnalysis.getExtension(file))) {
					filesCB.addItem(file.getAbsolutePath());
				}
			}				
		}
	}

	/**
	 * Adds attributes into {@link JTree}
	 * @param xmlNode node in XML
	 * @param node node in {@link JTree}
	 * @param allowAttributes show attributes?
	 */
	protected void addAttributes(Node xmlNode, XMLMutableTreeNode node, boolean allowAttributes) {
		if (!allowAttributes || xmlNode.getAttributes() == null) {
			return;
		}
		for (int i = 0; i < xmlNode.getAttributes().getLength(); i++) {
			node.add(new XMLMutableTreeNode(xmlNode.getAttributes().item(i)));
		}
	}

	/**
	 * Recursively loads nodes into {@link JTree}
	 * @param nodes {@link NodeList} in XML
	 * @param node node in {@link JTree}
	 * @param allowAttributes allow Attributes?
	 */
	protected void loadNodes(NodeList nodes, XMLMutableTreeNode node, boolean allowAttributes) {
		XMLMutableTreeNode newNode;
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			newNode = new XMLMutableTreeNode(nodes.item(i));
			addAttributes(newNode.getNode(), newNode, allowAttributes);
			loadNodes(newNode.getNode().getChildNodes(), newNode, allowAttributes);
			node.add(newNode);
		}
	}
	
	/**
	 * Adds {@link Node} into {@link JTree} and recursively all subnodes and attributes
	 * @param XMLNode {@link Node} to be added
	 * @param node {@link JTree} node
	 * @param allowAttributes allow attributes?
	 */
	protected void addNodeWithSubNodes(Node XMLNode, XMLMutableTreeNode node, boolean allowAttributes) {
		XMLMutableTreeNode newNode = new XMLMutableTreeNode(XMLNode);
		node.add(newNode);
		addAttributes(newNode.getNode(), newNode, allowAttributes);
		loadNodes(XMLNode.getChildNodes(), newNode, allowAttributes);
	}

	/**
	 * Expands all nodes in {@link JTree}
	 */
	protected void expandAll() {
		int row = 0;
		while (row < nodesT.getRowCount()) {
			nodesT.expandRow(row);
			row++;
		}
	}
	
	


	/**
	 * Loads selected file in {@link JComboBox}
	 */
	protected void loadFile() {
		if (filesCB.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(this, "No files found in selected folder. Can't continue... Please return one step back and check path entry...", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			Document doc = XMLParser.loadXMLFile((String)filesCB.getSelectedItem());

			XMLMutableTreeNode root = (XMLMutableTreeNode)model.getRoot();
			root.removeAllChildren();
			
			if (getRootNode() == null) {
				loadNodes(doc.getChildNodes(), root, getShowAttributes());
			} else {
				int skip = 0;
				Node node;
				while ((node = XMLParser.findNode(doc.getChildNodes(), getRootNode(), skip)) != null) {
					addNodeWithSubNodes(node, root, getShowAttributes());
					skip++;
				}
				if (root.getChildCount() == 0) {
					JOptionPane.showMessageDialog(this, "This document does not contain any Action!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			model.nodeStructureChanged(root);
			expandAll();

		} catch (IOException | ParseException e) {
			JOptionPane.showMessageDialog(this, "Error opening or parsing file!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Returns {@link NodeSelector} of root node.
	 * @return {@link NodeSelector} or null when whole XML should be used
	 */
	protected NodeSelector getRootNode() {
		return null;
	}
	
	/**
	 * Should attributes be shown?
	 * @return true if yes
	 */
	protected abstract boolean getShowAttributes();

	/**
	 * Updates details of selected node
	 * @param ns {@link NodeSelector}
	 * @param force if there was already selected some node and new value is null, should it really be done?
	 */
	protected void updateDetails(NodeSelector ns, boolean force) {
		if (selected == null || ns != null || force) {
			selected = ns;
		}
		nodeNameLB.setText("Element name: -");
		attrNameLB.setText("Attribute name: -");
		pathLB.setText("Element parents: -");
		if (selected == null) {
			return;
		}
		if (selected.nodeName != null) {
			nodeNameLB.setText("Element name: '"+selected.nodeName+"'");
		}
		if (selected.attributeName != null) {
			attrNameLB.setText("Attribute name: '"+selected.attributeName+"'");
		}
		if (selected.parents != null) {
			pathLB.setText("Element parents: '"+selected.parents+"'");
		}	
	}
	
	/**
	 * Creates {@link JComboBox}
	 */
	private void createComboBox() {
		filesCB = new JComboBox<String>();
		filesCB.setEditable(false);
		
		filesCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (filesCB.getSelectedIndex() == -1) {
					return;
				}
				loadFile();
			}
		});
	}


	/**
	 * Creates {@link JTree}
	 * @return {@link Component} containing {@link JTree}
	 */
	private Component createJTree() {
		XMLMutableTreeNode rootNode = new XMLMutableTreeNode("Document");
		model = new DefaultTreeModel(rootNode);
		nodesT = new JTree(model);
		nodesT.setCellRenderer(new XMLTreeCellRenderer());
		nodesT.setRootVisible(true);
		JScrollPane scrollSP = new JScrollPane(nodesT);
		scrollSP.setPreferredSize(new Dimension(200, 500));
		
		nodesT.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				XMLMutableTreeNode sel = (XMLMutableTreeNode)nodesT.getLastSelectedPathComponent();
				NodeSelector ns = null;
				if (sel != null) {
					ns = sel.getNodeSelector(getRootNode() != null);
				}
				updateDetails(ns, sel != null);
			}
		});
		
		nodesT.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		return scrollSP;
	}

	@Override
	public boolean onNextPress() {
		if (filesCB.getItemCount() < 1) {
			return false;
		}

		return true;
	}

	@Override
	public void onCardShow() {
		findFiles(settings.path);
		if (filesCB.getItemCount() > 0) {
			filesCB.setSelectedIndex(0);
		}
		loadFile();
		updateDetails(null, false);
	}

	@Override
	public abstract int getCardId();

	@Override
	public abstract String getCardTitle();

}