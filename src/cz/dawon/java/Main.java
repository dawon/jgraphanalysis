package cz.dawon.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidAlgorithmParameterException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import cz.dawon.java.gui.parserSetup.JGraphAnalysisSetup;
import cz.dawon.java.gui.parserSetup.JGraphAnalysisSetupListener;
import cz.dawon.java.gui.parserSetup.cards.XMLMutableTreeNode;
import cz.dawon.java.library.Action;
import cz.dawon.java.library.GraphStreamConnector;
import cz.dawon.java.library.JGraphAnalysis;

/**
 * Main example class
 * @author Jakub Zacek
 * @version 1.5
 */
public class Main extends JFrame {

	private static final long serialVersionUID = 3105670329966866466L;

	/**
	 * Main {@link JPanel}
	 */
	private JPanel mainPN;
	/**
	 * {@link JPanel} containing graph
	 */
	private JPanel graphPN;
	/**
	 * {@link JTextArea} containing Data of {@link Action}
	 */
	private JTextArea detailsTA;
	/**
	 * {@link JToolBar}
	 */
	private JToolBar toolbarTB = new JToolBar(JToolBar.HORIZONTAL);
	
	/**
	 * {@link JTree} model
	 */
	protected DefaultTreeModel model;

	/**
	 * {@link JTree} containing {@link Action}s
	 */
	protected JTree actionsT;
		
	/**
	 * {@link JGraphAnalysis} instance
	 */
	private JGraphAnalysis<String, String> jga;
	
	/**
	 * Constructor
	 */
	public Main() {
		super("JGraphAnalysis");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 600);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.createMainPanel();
		this.setVisible(true);
	}
	
	/**
	 * Creates main {@link JPanel}
	 */
	private void createMainPanel() {
		mainPN = new JPanel(new BorderLayout());
		this.add(mainPN);
		
		toolbarTB.setPreferredSize(new Dimension(600, 30));
		toolbarTB.setFloatable(false);
		mainPN.add(toolbarTB, BorderLayout.NORTH);
		
		mainPN.add(createJTree(), BorderLayout.WEST);
		
		JButton startBTN = new JButton("New");
		toolbarTB.add(startBTN);
		
		startBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		
		JButton processBTN = new JButton("Process");
		processBTN.setEnabled(false);
		toolbarTB.add(processBTN);
		
		processBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				process();
			}
		});		
		
		graphPN = new JPanel(new BorderLayout());
		graphPN.setPreferredSize(new Dimension(600, 570));
		graphPN.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		mainPN.add(graphPN, BorderLayout.CENTER);
		
		detailsTA = new JTextArea();
		detailsTA.setEnabled(false);
		detailsTA.setFont(new Font(detailsTA.getFont().getFamily(), 25, Font.PLAIN));
		detailsTA.setPreferredSize(new Dimension(200, 570));
		mainPN.add(detailsTA, BorderLayout.EAST);
	
	}
	
	/**
	 * Creates {@link JTree}
	 * @return {@link Component} containing {@link JTree}
	 */
	private Component createJTree() {
		XMLMutableTreeNode rootNode = new XMLMutableTreeNode("Actions");
		model = new DefaultTreeModel(rootNode);		
		actionsT = new JTree(model);
		//actionsT.setCellRenderer(new XMLTreeCellRenderer());
		actionsT.setRootVisible(true);
		actionsT.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		JScrollPane scrollSP = new JScrollPane(actionsT);
		scrollSP.setPreferredSize(new Dimension(200, 570));
		
		actionsT.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if (jga == null || actionsT.getSelectionPath() == null) {
					return;
				}
				Object[] pth = actionsT.getSelectionPath().getPath();
				if (pth.length > 1) {
					String id = (String)((DefaultMutableTreeNode) pth[1]).getUserObject();
					jga.selectVertex(id);
					detailsTA.setText(jga.getAction(id).getData());
				} else {
					jga.selectVertex(null);
					detailsTA.setText("");
				}
			}
		});		
		
		return scrollSP;
	}
	
	/**
	 * Processes the data
	 */
	private void process() {
		try {
			jga.process();
			toolbarTB.getComponent(1).setEnabled(false);
		} catch (NoSuchElementException e) {
			JOptionPane.showMessageDialog(null, "Some referenced Actions don't exist!", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (InvalidAlgorithmParameterException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		jga.getGraphConnector().updateUI();
	}	

	/**
	 * Entry point of program
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new Main();
	}
	
	/**
	 * shows the wizard
	 */
	private void start() {
		JGraphAnalysisSetup setup = new JGraphAnalysisSetup(this);
		setup.addJGraphAnalysisSetupListener(new JGraphAnalysisSetupListener() {
			@Override
			public void setupDone(JGraphAnalysis<String, String> jga) {
				onDone(jga);
			}
		});
		
		setup.showDialog();
	}
	
	/**
	 * Expands all nodes in {@link JTree}
	 */
	protected void expandAll() {
		for (int i = 0; i < actionsT.getRowCount(); i++) {
			actionsT.expandRow(i);
		}
	}	
	
	/**
	 * Updates {@link JTree}
	 */
	private void updateJTree() {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		DefaultMutableTreeNode ac, ref;
		root.removeAllChildren();
		
		for (Iterator<String> iterator = jga.getActions().keySet().iterator(); iterator.hasNext();) {
			Action<String, String> action = jga.getAction(iterator.next());
			ac = new DefaultMutableTreeNode(action.getId());
			root.add(ac);
			
			if (!action.getPrerequisities().isEmpty()) {
				ref = new DefaultMutableTreeNode("Prerequisities");
				for (Iterator<String> iterator2 = action.getPrerequisities().iterator(); iterator2.hasNext();) {
					ref.add(new DefaultMutableTreeNode(iterator2.next()));
				}
				ac.add(ref);
			}
			if (!action.getTightPrerequisities().isEmpty()) {
				ref = new DefaultMutableTreeNode("Tight Prerequisities");
				for (Iterator<String> iterator2 = action.getTightPrerequisities().iterator(); iterator2.hasNext();) {
					ref.add(new DefaultMutableTreeNode(iterator2.next()));
				}
				ac.add(ref);				
			}
			if (!action.getFollowers().isEmpty()) {
				ref = new DefaultMutableTreeNode("Followers");
				for (Iterator<String> iterator2 = action.getFollowers().iterator(); iterator2.hasNext();) {
					ref.add(new DefaultMutableTreeNode(iterator2.next()));
				}
				ac.add(ref);				
			}
			if (!action.getTightFollowers().isEmpty()) {
				ref = new DefaultMutableTreeNode("Tight Followers");
				for (Iterator<String> iterator2 = action.getTightFollowers().iterator(); iterator2.hasNext();) {
					ref.add(new DefaultMutableTreeNode(iterator2.next()));
				}
				ac.add(ref);				
			}			
			
		}
		model.nodeStructureChanged(root);
		expandAll();
	}

	/**
	 * When wizard id done
	 * @param jga {@link JGraphAnalysis}
	 */
	private void onDone(JGraphAnalysis<String, String> jga) {
		this.jga = jga;
	
		jga.setGraphConnector(new GraphStreamConnector());
		jga.getGraphConnector().createGraph("Test");
		
		jga.addVertices();
		toolbarTB.getComponent(1).setEnabled(true);
		
		this.graphPN.removeAll();
		this.graphPN.add(jga.getGraphConnector().getComponent(), BorderLayout.CENTER);

		updateJTree();
		
		this.graphPN.getComponent(0).revalidate();
		this.graphPN.getComponent(0).repaint();
	}

}
