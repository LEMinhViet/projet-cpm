package UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import filtrer.Filtre;
import filtrer.FiltreDate;
import filtrer.FiltreIP;
import filtrer.FiltreNavigateurs;
import filtrer.FiltreStatutHTTP;
import filtrer.FiltreTypeFichier;
import filtrer.Format;
import filtrer.FormatLog;
import filtrer.Traitement;

public class MainGUI {

	private JFrame frame;

	private Traitement traitement;
	private Format f;

	private JPanel panel_fichier;
	private JPanel panel_filtrage;
	private JButton Button_fichier;
	private Box verticalBox;
	private JButton Button_Filtrage;
	private Box horizontalBox;
	private JCheckBox CheckBox_Date;
	private JCheckBox CheckBox_IP;
	private JCheckBox CheckBox_Navigateurs;
	private JCheckBox CheckBox_Fichier;
	private JTextField textField_Date;
	private JTextField textField_IP;
	private JTextField textField_Navigateurs;
	private JTextField textField_Fichier;
	private JTextField textField_HTTP;
	private JCheckBox CheckBox_HTTP;
	private JScrollPane scrollPane;
	private JTextArea taResultat;
	
	private String ligne;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		creationInterface();

		Button_fichier.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
				c.setFileFilter(filter);
				c.setCurrentDirectory(new File("/users/Etu1/3261731/workspace/cpm"));

				// Demonstrate "Open" dialog:
				int rVal = c.showOpenDialog(frame);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					String nom = c.getSelectedFile().getName();
					String dir = c.getCurrentDirectory().toString();

					Button_Filtrage.setEnabled(true);
					traitement = new Traitement(nom);
					f = new FormatLog();
				}
				if (rVal == JFileChooser.CANCEL_OPTION) {
					// filename.setText("You pressed cancel");
					// dir.setText("");
				}
			}
		});

		Button_Filtrage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				taResultat.setText("");
				traitement.removeTousFiltres();

				if (CheckBox_Date.isSelected()) {
					traitement.addFiltre(new FiltreDate(f, "05/Sep/2004", "05/Sep/2004"));
				} else if (CheckBox_Fichier.isSelected()) {
					traitement.addFiltre(new FiltreTypeFichier(f, ".htm|.html"));
				} else if (CheckBox_HTTP.isSelected()) {
					traitement.addFiltre(new FiltreStatutHTTP(f, "2..|3.."));
				} else if (CheckBox_IP.isSelected()) {
					traitement.addFiltre(new FiltreIP(f, "hamburg047.server4you.de"));
				} else if (CheckBox_Navigateurs.isSelected()) {
					traitement.addFiltre(new FiltreNavigateurs(f));
				}

				try {
					traite();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

	public void traite() throws IOException {

		int nb = 0;
		InputStream ips = new FileInputStream(traitement.getFichier());
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);

		while ((ligne = br.readLine()) != null) {
			ligne = ligne.replace("\uFEFF", "");
			boolean valide = true;
			for (Filtre f : traitement.getFiltrage()) {
				if (!f.valideFiltre(ligne)) {
					valide = false;
					break;
				}
			}

			if (valide) {
				nb++;
				taResultat.append(ligne + "\n");
				// System.out.println(ligne);
			}
		}
		System.out.println(nb);
		br.close();

	}

	public void creationInterface() {

		frame = new JFrame();
		frame.setBounds(100, 100, 607, 461);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel_fichier = new JPanel();
		frame.getContentPane().add(panel_fichier, BorderLayout.NORTH);

		horizontalBox = Box.createHorizontalBox();
		panel_fichier.add(horizontalBox);

		Button_fichier = new JButton("fichier");
		horizontalBox.add(Button_fichier);

		panel_filtrage = new JPanel();
		panel_filtrage.setToolTipText("");
		frame.getContentPane().add(panel_filtrage, BorderLayout.EAST);
		panel_filtrage.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		verticalBox = Box.createVerticalBox();
		panel_filtrage.add(verticalBox);

		textField_Date = new JTextField();
		verticalBox.add(textField_Date);
		textField_Date.setColumns(10);

		CheckBox_Date = new JCheckBox("FiltreDate");
		verticalBox.add(CheckBox_Date);

		textField_IP = new JTextField();
		verticalBox.add(textField_IP);
		textField_IP.setColumns(10);

		CheckBox_IP = new JCheckBox("FiltreIP");
		verticalBox.add(CheckBox_IP);

		textField_Navigateurs = new JTextField();
		verticalBox.add(textField_Navigateurs);
		textField_Navigateurs.setColumns(10);

		CheckBox_Navigateurs = new JCheckBox("FiltreNavigateurs");
		verticalBox.add(CheckBox_Navigateurs);

		textField_HTTP = new JTextField();
		verticalBox.add(textField_HTTP);
		textField_HTTP.setColumns(10);

		CheckBox_HTTP = new JCheckBox("FiltreStatutHTTP");
		verticalBox.add(CheckBox_HTTP);

		textField_Fichier = new JTextField();
		verticalBox.add(textField_Fichier);
		textField_Fichier.setColumns(10);

		CheckBox_Fichier = new JCheckBox("FiltreTypeFichier");
		verticalBox.add(CheckBox_Fichier);

		Button_Filtrage = new JButton("Filtrage");
		Button_Filtrage.setEnabled(false);

		verticalBox.add(Button_Filtrage);

		scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBounds(new Rectangle(10, 10, 200, 200));

		taResultat = new JTextArea();
		taResultat.setBounds(new Rectangle(10, 10, 300, 300));
		scrollPane.setViewportView(taResultat);
	}

}
