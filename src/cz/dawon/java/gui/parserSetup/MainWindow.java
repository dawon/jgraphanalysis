package cz.dawon.java.gui.parserSetup;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import cz.dawon.java.gui.parserSetup.cards.ExportCard;
import cz.dawon.java.gui.parserSetup.cards.ICard;
import cz.dawon.java.gui.parserSetup.cards.ImportCard;
import cz.dawon.java.gui.parserSetup.cards.JGraphAnalysisSettings;
import cz.dawon.java.gui.parserSetup.cards.SelectActionCard;
import cz.dawon.java.gui.parserSetup.cards.SelectNodeCard;
import cz.dawon.java.gui.parserSetup.cards.SelectActionIdCard;
import cz.dawon.java.gui.parserSetup.cards.SelectFileCard;
import cz.dawon.java.gui.parserSetup.cards.SelectFolderCard;
import cz.dawon.java.gui.parserSetup.cards.SelectModeCard;
import cz.dawon.java.library.JGraphAnalysis;

/**
 * Shows the Main Window of the Setup wizard
 * @author Jakub Zacek
 * @version 1.7.1
 */
public class MainWindow extends JDialog {

	private static final long serialVersionUID = -5542626286639824349L;

	/**
	 * instance of JGraphAnalysisSetup
	 */
	private JGraphAnalysisSetup jgas;

	/**
	 * id of actually shown card
	 */
	private int actCard = 0;
	/**
	 * array of all cards
	 */
	private ICard[] cards = new ICard[12];

	/**
	 * instance of card layout
	 */
	private CardLayout cl = new CardLayout();

	/**
	 * card Panel
	 */
	private JPanel cardPN = new JPanel(cl);
	/**
	 * main Panel
	 */
	private JPanel mainPN = new JPanel();

	/**
	 * instance of Next Button
	 */
	private JButton nextBTN   = new JButton();
	/**
	 * instance of Previous Button
	 */
	private JButton prevBTN   = new JButton();
	/**
	 * instance of Cancel Button
	 */
	private JButton cancelBTN = new JButton();

	/**
	 * {@link JGraphAnalysisSettings} instance
	 */
	private JGraphAnalysisSettings settings = new JGraphAnalysisSettings();

	/**
	 * Constuctor
	 * @param fr parent {@link JFrame}
	 * @param jgas instance of {@link JGraphAnalysisSetup}
	 */
	public MainWindow(JFrame fr, JGraphAnalysisSetup jgas) {
		super(fr, "JGraphAnalysis - Wizard");
		this.jgas = jgas;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}		

		this.setModal(true);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setSize(600, 500);
		this.setLocationRelativeTo(fr);
		this.setResizable(false);

		this.createMainPanel();
		cards[actCard].args(settings);
		this.showCard(this.actCard);
	}

	/**
	 * Shows the window
	 */
	public void showWindow() {
		this.setVisible(true);
	}

	/**
	 * Creates the Main Panel
	 */
	private void createMainPanel() {
		mainPN.setLayout(new BorderLayout());

		mainPN.add(cardPN, BorderLayout.CENTER);

		JPanel buttonsPN = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		prepareButtons();

		buttonsPN.add(prevBTN);
		buttonsPN.add(nextBTN);
		buttonsPN.add(cancelBTN);

		mainPN.add(buttonsPN, BorderLayout.SOUTH);

		addCards();

		this.add(mainPN);
	}


	/**
	 * Sets up all buttons
	 */
	private void prepareButtons() {
		prevBTN.setText("Previous");
		nextBTN.setText("Next");
		cancelBTN.setText("Cancel");		

		nextBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				next();
			}
		});

		prevBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prev();
			}
		});

		cancelBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});

	}

	/**
	 * Called when Next is pressed on last card
	 */
	private void finish() {
		JGraphAnalysis<String, String> jga = new JGraphAnalysis<String, String>();
		jga.setParser(settings.parser);

		try {
			if (settings.singleFile) {
				jga.parse(settings.path);
			} else {
				jga.parseFolder(settings.path, settings.recursive, settings.extension);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error while reading the file: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Error while parsing the file: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		jgas.setUpDone(jga);
		this.close();
	}

	/**
	 * Called when Next button is pressed
	 */
	private void next() {
		if (!cards[actCard].onNextPress()) {
			return;
		}
		if (cards[actCard].isLast()) {
			finish();
			return;
		}
		cards[cards[actCard].getNextCardId()].args(settings);		
		this.actCard = cards[actCard].getNextCardId();
		showCard(this.actCard);
	}

	/**
	 * Called when Previous button is pressed
	 */	
	private void prev() {
		if (!cards[actCard].onPrevPress()) {
			return;
		}
		if (cards[actCard].isFirst()) {
			return;
		}
		this.actCard = cards[actCard].getPrevCardId();
		showCard(this.actCard);
	}

	/**
	 * Called when Cancel button is pressed
	 */	
	private void cancel() {
		if (!cards[actCard].onCancelPress()) {
			return;	
		}
		this.close();
	}

	private void close() {
		this.setVisible(false);
		this.dispose();
	}


	/**
	 * Shows card with specified id
	 * @param cardId id of card to be shown
	 */
	private void showCard(int cardId) {
		ICard card = cards[cardId];

		prevBTN.setEnabled(true);
		if (card.isFirst()) {
			prevBTN.setEnabled(false);
		}

		nextBTN.setText("Next");
		if (card.isLast()) {
			nextBTN.setText("Finish");
		}

		card.onCardShow();

		cl.show(cardPN, Integer.toString(cardId));
	}

	/**
	 * Creates container containing specified card
	 * @param card card
	 * @return container
	 */
	private Container createCard(ICard card) {
		JPanel res = new JPanel(new BorderLayout());
		res.setBorder(new EmptyBorder(5, 5, 5, 5));
		JPanel innerP = new JPanel(new BorderLayout());

		Border brd = BorderFactory.createTitledBorder(card.getCardTitle());
		((TitledBorder)brd).setTitleFont(new Font("Serif", Font.BOLD, 15));
		innerP.setBorder(brd);

		res.add(innerP, BorderLayout.CENTER);

		innerP.add(card.getCardContent(), BorderLayout.CENTER);

		return res;
	}	


	/**
	 * Adds all of the cards
	 */
	private void addCards() {
		cards[0]  = new SelectModeCard();
		cards[1]  = new SelectFileCard();
		cards[2]  = new SelectFolderCard();
		cards[3]  = new ImportCard();
		cards[4]  = new SelectActionCard();
		cards[5]  = new SelectActionIdCard();
		for (int i = 0; i < 5; i++) {
			cards[i+6] = new SelectNodeCard(i);
		}
		cards[11] = new ExportCard();

		for (int i = 0; i < cards.length; i++) {
			cardPN.add(createCard(cards[i]), Integer.toString(i));
		}
	}

}
