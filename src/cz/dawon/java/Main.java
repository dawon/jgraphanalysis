package cz.dawon.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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
import cz.dawon.java.library.Action;
import cz.dawon.java.library.JGraphAnalysis;
import cz.dawon.java.library.PrecedenceGraphCreator;
import cz.dawon.java.library.PrecedenceGraphCreator.ReferenceIdentificator;
import cz.dawon.java.library.graphConnectors.GraphStreamConnector;
import cz.dawon.java.library.graphConnectors.IGraphClickListener;

/**
 * Main example class
 * @author Jakub Zacek
 * @version 1.7
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
	 * original {@link Set} of {@link Action}s
	 */
	private Set<Action<String, String>> actions = new HashSet<Action<String, String>>();

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

		createToolbar();
		
		mainPN.add(createJTree(), BorderLayout.WEST);

		graphPN = new JPanel(new BorderLayout());
		graphPN.setPreferredSize(new Dimension(600, 570));
		graphPN.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		mainPN.add(graphPN, BorderLayout.CENTER);

		detailsTA = new JTextArea();
		detailsTA.setFont(new Font(detailsTA.getFont().getFamily(), Font.PLAIN, 25));
		detailsTA.setPreferredSize(new Dimension(200, 570));
		detailsTA.setEditable(false);
		mainPN.add(detailsTA, BorderLayout.EAST);

	}

	/**
	 * Creates {@link JToolBar}
	 */
	private void createToolbar() {
		toolbarTB.setPreferredSize(new Dimension(600, 30));
		toolbarTB.setFloatable(false);
		mainPN.add(toolbarTB, BorderLayout.NORTH);

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
	}

	/**
	 * Creates {@link JTree}
	 * @return {@link Component} containing {@link JTree}
	 */
	private Component createJTree() {
		ActionMutableTreeNode rootNode = new ActionMutableTreeNode(0);
		model = new DefaultTreeModel(rootNode);		
		actionsT = new JTree(model);
		actionsT.setCellRenderer(new ActionTreeCellRenderer());
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
			PrecedenceGraphCreator<String, String> pgc = jga.init();

			GraphStreamConnector gr = new GraphStreamConnector();
			gr.createGraph("");
			List<ReferenceIdentificator<String>> refs = pgc.findAndFixCycles(gr, true);

			if (!refs.isEmpty()) {
				String res = "Some Refereces would cause troubles (cycles) so I fixed (removed) them:\n";
				for (Iterator<ReferenceIdentificator<String>> iterator = refs.iterator(); iterator.hasNext();) {
					res += iterator.next().toString() + "\n";
				}
				JOptionPane.showMessageDialog(this, res, "Warning", JOptionPane.WARNING_MESSAGE);
			}
		
			updateJTree(refs);
			
			jga.process(pgc);
			toolbarTB.getComponent(1).setEnabled(false);
		} catch (InvalidAlgorithmParameterException | NoSuchElementException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
	 * @param refs {@link List} of {@link ReferenceIdentificator}s
	 */
	private void updateJTree(List<ReferenceIdentificator<String>> refs) {
		ActionMutableTreeNode root = (ActionMutableTreeNode)model.getRoot();
		ActionMutableTreeNode ac, ref, rf;
		ReferenceIdentificator<String> refid;
		String action1;
		root.removeAllChildren();

		for (Iterator<Action<String, String>> iterator = actions.iterator(); iterator.hasNext();) {
			Action<String, String> action = iterator.next();
			ac = new ActionMutableTreeNode(1, action.getId());
			root.add(ac);

			for (int i = 0; i < 4; i++) {
				if (!action.getRawReferences().get(i).isEmpty()) {
					ref = new ActionMutableTreeNode(i+2);
					for (Iterator<String> iterator2 = action.getRawReferences().get(i).iterator(); iterator2.hasNext();) {
						action1 = iterator2.next();
						rf = new ActionMutableTreeNode(6, action1);
						
						refid = new ReferenceIdentificator<String>(i, i < 2 ? action1 : action.getId(), i < 2 ? action.getId() : action1);
						
						if (refs != null && refs.contains(refid)) {
							rf.setRemoved(true);
						}
						
						ref.add(rf);
					}
					ac.add(ref);
				}
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
		
		for (Iterator<String> iterator = jga.getActions().keySet().iterator(); iterator.hasNext();) {
			actions.add(jga.getAction(iterator.next()).copy());
		}		
		
		toolbarTB.getComponent(1).setEnabled(true);

		this.graphPN.removeAll();
		this.graphPN.add(jga.getGraphConnector().getComponent(), BorderLayout.CENTER);
		
		jga.getGraphConnector().addClickListener(new IGraphClickListener<String>() {
			
			@Override
			public void onMouseUp(String vertex) {}	
			@Override
			public void onMouseDown(String vertex) {
				detailsTA.setText(Main.this.jga.getAction(vertex).getData());				
			}
		});
		
		
		

		updateJTree(null);

		this.graphPN.getComponent(0).revalidate();
		this.graphPN.getComponent(0).repaint();
	}

}
