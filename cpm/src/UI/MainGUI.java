package UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.xml.sax.SAXException;

import support.CoffeeToLog;
import filtrer.Filtre;
import filtrer.FiltreDate;
import filtrer.FiltreEtape;
import filtrer.FiltreIP;
import filtrer.FiltreNavigateurs;
import filtrer.FiltreOutil;
import filtrer.FiltreStatutHTTP;
import filtrer.FiltreTypeFichier;
import filtrer.FiltreUtilisateur;
import filtrer.Format;
import filtrer.FormatLog;
import filtrer.StockerLogs;
import filtrer.Traitement;

public class MainGUI {

	private static boolean utiliseXML = false;

	private JFrame mainFrame;

	private Traitement traitement;
	private Format f;
	private long nbLignes;
	
	private Sleeper tache;
	
	private String nomFichierLog;
	private String nomFichierARFF;
	private String nomFichierARFFResult;
	
	private JPanel panel_fichier;
	private JPanel panel_filtrage;
	private JButton btnFichier;
	private Box verticalBox;
	private JButton Button_Filtrage;
	private Box horizontalBox;
	private JCheckBox CheckBox_Date;
	private JCheckBox CheckBox_IP;
	private JCheckBox CheckBox_Navigateurs;
	private JCheckBox CheckBox_Fichier;
	private JTextField textField_IP;
	private JTextField textField_Navigateurs;
	private JTextField textField_TypeFichier;
	private JTextField textField_HTTP;
	private JCheckBox CheckBox_HTTP;
	private JScrollPane scrollPane;
	private JTextArea taResultat;
	
	private String ligne;
	private Component verticalStrut;
	private JLabel lblCharger;
	private JLabel lblChoisir;
	private JLabel lblFiltrer;
	private JProgressBar progressBar_filtrage;
	private JPanel panel_status;
	private JLabel lbl_IlYA;
	private JLabel lbl_nbReq;
	private JLabel lblRequete;
	private JSpinner spinner_DateDebut;
	private JSpinner spinner_DateFin;
	private JLabel lblStats;
	private JButton Button_Statistiques;
	private JLabel lblCreerARFF;
	private JButton Button_Creer;
	private JLabel lblAnalyser;
	private JButton Button_Analyser;
	private Box horizontalBox_Creer;
	private Box horizontalBox_Analyser;
	private JButton Button_CreationResultat;
	private JButton Button_AnalyseResultat;
	private JLabel lbl_nbSession;
	private JLabel lbl_Sessions;
	private JLabel lblResultatCreer;
	private JLabel lblResultatAnalyser;
	private Box horizontalBox_Stats;
	private JLabel lblResultatStats;
	private JLabel lblChargerXML;
	private JButton Button_XML;
	private Component fichierStutGauche;
	private Component fichierStutDroit;
	private JComboBox<Integer> comboBox_NombreGroupe;
	private JLabel Label_Ou;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.mainFrame.setVisible(true);
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
		
		Button_XML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Choisir le fichier logs
				JFileChooser c = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier XML", "xml", "xml");
				c.setFileFilter(filter);
				c.setCurrentDirectory(new File("/users/Etu1/3261731/workspace/CPM"));

				// Demonstrater "Open" dialog:
				int rVal = c.showOpenDialog(mainFrame);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					try {
						// Transformer le fichier coffee au fichier log, 
						// et le sauvegarder dans la meme location avec le fichier coffee 
						CoffeeToLog coffeeToLog = new CoffeeToLog();
						coffeeToLog.transformerCoffeeLog(c.getSelectedFile().getAbsolutePath());
						
						nomFichierLog = c.getSelectedFile().getAbsolutePath().replace("xml", "txt");
						// Obtenir le nombre des lignes dans le fichier logs					
		    		    getNbLignes(nomFichierLog);
						
		    		    // Permettre seulement le button Filtrage 
						Button_Filtrage.setEnabled(true);
						Button_Statistiques.setEnabled(false);
						Button_Creer.setEnabled(false);
						Button_CreationResultat.setEnabled(false);
						Button_Analyser.setEnabled(false);
						Button_AnalyseResultat.setEnabled(false);	
						
						// Modifier l'interface pour le fichier Xml - COFFee Log
						utiliseXML = true;
						CheckBox_Navigateurs.setEnabled(false);
						CheckBox_HTTP.setText("FiltreEtape");
						CheckBox_Fichier.setText("FiltreOutil");
						CheckBox_IP.setText("FiltreUtilisateur");
						
					} catch (ParserConfigurationException | SAXException
							| IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});		
		
		btnFichier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Choisir le fichier logs
				JFileChooser c = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier Texte", "txt", "text");
				c.setFileFilter(filter);
				c.setCurrentDirectory(new File("/users/Etu1/3261731/workspace/cpm"));

				// Demonstrater "Open" dialog:
				int rVal = c.showOpenDialog(mainFrame);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					nomFichierLog = c.getSelectedFile().getAbsolutePath();
					// Obtenir le nombre des lignes dans le fichier logs					
	    		    getNbLignes(nomFichierLog);
					
	    		    // Permettre seulement le button Filtrage 
					Button_Filtrage.setEnabled(true);
					Button_Statistiques.setEnabled(false);
					Button_Creer.setEnabled(false);
					Button_CreationResultat.setEnabled(false);
					Button_Analyser.setEnabled(false);
					Button_AnalyseResultat.setEnabled(false);	
					
					// Modifier l'interface pour le fichier log txt
					utiliseXML = false;
					CheckBox_Navigateurs.setEnabled(true);
					CheckBox_HTTP.setText("FiltreHTTP");
					CheckBox_Fichier.setText("FiltreFichier");
					CheckBox_IP.setText("FiltreIP");
				}
			}
		});

		Button_Filtrage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				traitement = new Traitement(nomFichierLog);
				f = new FormatLog();
				
				taResultat.setText("");
				traitement.removeTousFiltres();
				
				if (CheckBox_Date.isSelected()) {

					SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);			
					// Pour obtenir le value du jSpinner (Date) and formatter le date sous forme : 05/Sep/2004   
					traitement.addFiltre(new FiltreDate(f, (format.format((Date)spinner_DateDebut.getValue())), 
														   format.format((Date)spinner_DateFin.getValue())));
					System.out.println("date " + format.format((Date)spinner_DateDebut.getValue()));
				} 
				
				if (CheckBox_Fichier.isSelected()) {
					if (utiliseXML) {	// Filtrer les outils
						traitement.addFiltre(new FiltreOutil(f, textField_TypeFichier.getText()));
					} else {
						traitement.addFiltre(new FiltreTypeFichier(f, textField_TypeFichier.getText()));
					}
				} 
				
				if (CheckBox_HTTP.isSelected()) {
					if (utiliseXML) {	// Filtrer les etapes
						traitement.addFiltre(new FiltreEtape(f, textField_HTTP.getText()));
					} else {
						traitement.addFiltre(new FiltreStatutHTTP(f, textField_HTTP.getText()));
					}
				} 
				
				if (CheckBox_IP.isSelected()) {
					if (utiliseXML) { 	// Filtrer les utilisateurs
						traitement.addFiltre(new FiltreUtilisateur(f, textField_IP.getText()));
					} else {
						traitement.addFiltre(new FiltreIP(f, textField_IP.getText()));	
					}					
				} 
				
				if (CheckBox_Navigateurs.isSelected()) {
					traitement.addFiltre(new FiltreNavigateurs(f));
				}
				
				traitement.addFiltre(new StockerLogs(f));
				// Traiter les filtrages
				traiteFiltre();			        				
			}
		});

		Button_Statistiques.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				traitement.analyserSession();
				// Afficher un dialog pour les resultats 
				if (utiliseXML) {
					AxisChart_XML axisChart = new AxisChart_XML(traitement.getAnalyserSession());
					axisChart.setVisible(true);
				} else {
					AxisChart_Log axisChart = new AxisChart_Log(traitement.getAnalyserSession());
					axisChart.setVisible(true);
					Button_Creer.setEnabled(true);
				}
				
				lblResultatStats.setText("Done");
			}
		});
		
		Button_Creer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nomFichierARFF = traitement.creerARFF(nomFichierLog);
				Button_Analyser.setEnabled(true);
				lblResultatCreer.setText("Done");
				Button_CreationResultat.setEnabled(true);
			}
		});
		
		Button_CreationResultat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TexteDialog creationResultat = new TexteDialog(nomFichierARFF);
				creationResultat.setVisible(true);
			}
		});
		Button_Analyser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nomFichierARFFResult = traitement.analyserARFF(nomFichierARFF, (int)comboBox_NombreGroupe.getSelectedItem());
				lblResultatAnalyser.setText("Done");
				Button_AnalyseResultat.setEnabled(true);
			}
		});
		Button_AnalyseResultat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TexteDialog analyserResultat = new TexteDialog(nomFichierARFFResult);
				analyserResultat.setVisible(true);
			}
		});
		
	}
	
	/**
	 * Retirer nombre de lignes dans le fichier logs (plus vite)
	 * @param nom
	 */
	public void getNbLignes(String nom) {
		try {
			LineNumberReader lnr = new LineNumberReader(new FileReader(nom));
			nbLignes = 0;
			while (lnr.readLine() != null){
            	nbLignes++;
	        }
            lnr.close();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Creer et executer la tache de lire et valider le fichier log
	 */
	public void traiteFiltre() {
		if (tache != null && !tache.isDone()) {
			tache.cancel(true);
		}
		tache = new Sleeper();
        tache.execute();
	}
	
	// SwingWorker classe est pour simuler une tache 
	class Sleeper extends SwingWorker<String, String> {	
		@Override
		public String doInBackground() throws InterruptedException {	   
			try {
				int nb = 0;
				int nbtotal = 0;
				
				InputStream ips = new FileInputStream(traitement.getFichier());
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				
				StringBuilder sbTousTextes = new StringBuilder();
				
				// Desactiver le button filtrage
				Button_Filtrage.setEnabled(false);
				
				while ((ligne = br.readLine()) != null) {
					nbtotal++;
					
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
						sbTousTextes.append(ligne + "\n"); 
					}	
					// Mise a jour le progessBar
					progressBar_filtrage.setValue((int)(nbtotal * 100 / nbLignes));
				}
				// Ajouter les textes au TextField
				taResultat.setText(sbTousTextes.toString());
				// Mettre le nombre de requete dans le TextField
				lbl_nbReq.setText(String.valueOf(nb));
				// Mettre le nombre de sessions dans le TextField
				lbl_nbSession.setText(String.valueOf(traitement.afficherNombreDeSession()));
				br.close();
			} catch (IOException ioe) {
				
			}				
			return "Done";
		}
        
		//when the 'task' has finished re-enable the go button
		@Override
		public void done() {
			Button_Filtrage.setEnabled(true);
			Button_Statistiques.setEnabled(true);
			lblResultatStats.setText("");
			lblResultatAnalyser.setText("");
			lblResultatCreer.setText("");			
		}
	}

	/**
	 * Creer l'interface du programme
	 */
	public void creationInterface() {
		
//		try {
//		    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//		} catch(Exception e) {
//	        System.out.println("UIManager Exception : "+e);
//		}

		mainFrame = new JFrame();
		mainFrame.setTitle("Analyse de traces");
		BorderLayout borderLayout = (BorderLayout) mainFrame.getContentPane().getLayout();
		borderLayout.setVgap(10);
		borderLayout.setHgap(10);
		mainFrame.setBounds(100, 100, 740, 553);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel_fichier = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_fichier.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		mainFrame.getContentPane().add(panel_fichier, BorderLayout.NORTH);

		horizontalBox = Box.createHorizontalBox();
		panel_fichier.add(horizontalBox);
		
		lblChargerXML = new JLabel("1. Charger le fichier XML :      ");
		lblChargerXML.setHorizontalAlignment(SwingConstants.TRAILING);
		horizontalBox.add(lblChargerXML);
		lblChargerXML.setAlignmentX(0.5f);
		
		Button_XML = new JButton("XML");
		Button_XML.setAlignmentX(0.5f);
		horizontalBox.add(Button_XML);
		
		fichierStutGauche = Box.createHorizontalStrut(30);
		horizontalBox.add(fichierStutGauche);
		
		Label_Ou = new JLabel("Ou");
		horizontalBox.add(Label_Ou);
		
		fichierStutDroit = Box.createHorizontalStrut(30);
		horizontalBox.add(fichierStutDroit);
		
		lblCharger = new JLabel("1. Charger le fichier Logs :      ");
		lblCharger.setAlignmentX(Component.CENTER_ALIGNMENT);
		horizontalBox.add(lblCharger);
		
		btnFichier = new JButton("Fichier");
		btnFichier.setAlignmentX(Component.CENTER_ALIGNMENT);
		horizontalBox.add(btnFichier);

		panel_filtrage = new JPanel();
		panel_filtrage.setToolTipText("");
		mainFrame.getContentPane().add(panel_filtrage, BorderLayout.EAST);
		panel_filtrage.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		verticalBox = Box.createVerticalBox();
		panel_filtrage.add(verticalBox);
		
		lblChoisir = new JLabel("2. Choisir les filtrages :");
		verticalBox.add(lblChoisir);
		
		spinner_DateDebut = new JSpinner();
		spinner_DateDebut.setAlignmentX(Component.LEFT_ALIGNMENT);
		spinner_DateDebut.setModel(new SpinnerDateModel(new Date(1094317200000L), new Date(946659600000L), new Date(1388509200000L), Calendar.DAY_OF_YEAR));
		spinner_DateDebut.setEditor(new JSpinner.DateEditor(spinner_DateDebut, "dd/MMM/yyyy"));
		verticalBox.add(spinner_DateDebut);
		
		spinner_DateFin = new JSpinner();
		spinner_DateFin.setAlignmentX(Component.LEFT_ALIGNMENT);
		spinner_DateFin.setModel(new SpinnerDateModel(new Date(1094403600000L), new Date(946659600000L), new Date(1388509200000L), Calendar.DAY_OF_YEAR));
		spinner_DateFin.setEditor(new JSpinner.DateEditor(spinner_DateFin, "dd/MMM/yyyy"));
		verticalBox.add(spinner_DateFin);

		CheckBox_Date = new JCheckBox("FiltreDate");
		verticalBox.add(CheckBox_Date);

		textField_IP = new JTextField();
		textField_IP.setText("hamburg047.server4you.de");
		textField_IP.setToolTipText("");
		verticalBox.add(textField_IP);
		textField_IP.setColumns(10);

		CheckBox_IP = new JCheckBox("FiltreIP");
		verticalBox.add(CheckBox_IP);

		textField_Navigateurs = new JTextField();
		textField_Navigateurs.setEnabled(false);
		verticalBox.add(textField_Navigateurs);
		textField_Navigateurs.setColumns(10);

		CheckBox_Navigateurs = new JCheckBox("FiltreNavigateurs");
		verticalBox.add(CheckBox_Navigateurs);

		textField_HTTP = new JTextField();
		textField_HTTP.setText("2..|3..");
		verticalBox.add(textField_HTTP);
		textField_HTTP.setColumns(10);

		CheckBox_HTTP = new JCheckBox("FiltreStatutHTTP");
		verticalBox.add(CheckBox_HTTP);

		textField_TypeFichier = new JTextField();
		textField_TypeFichier.setText(".htm|.html");
		verticalBox.add(textField_TypeFichier);
		textField_TypeFichier.setColumns(10);

		CheckBox_Fichier = new JCheckBox("FiltreTypeFichier");
		verticalBox.add(CheckBox_Fichier);
		
		verticalStrut = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut);
		
		lblFiltrer = new JLabel("3. Filtrer :    ");
		verticalBox.add(lblFiltrer);
		
		Button_Filtrage = new JButton("Filtrage");
		verticalBox.add(Button_Filtrage);
		Button_Filtrage.setEnabled(false);
		
		lblStats = new JLabel("4. Stats :    ");
		verticalBox.add(lblStats);
		
		horizontalBox_Stats = Box.createHorizontalBox();
		horizontalBox_Stats.setAlignmentX(Component.LEFT_ALIGNMENT);
		verticalBox.add(horizontalBox_Stats);
		
		Button_Statistiques = new JButton("Statistiques");
		horizontalBox_Stats.add(Button_Statistiques);
		Button_Statistiques.setEnabled(false);
		
		lblResultatStats = new JLabel("");
		horizontalBox_Stats.add(lblResultatStats);
		
		lblCreerARFF = new JLabel("5. Creer ARFF : ");
		verticalBox.add(lblCreerARFF);
		
		horizontalBox_Creer = Box.createHorizontalBox();
		horizontalBox_Creer.setAlignmentX(Component.LEFT_ALIGNMENT);
		verticalBox.add(horizontalBox_Creer);
		
		Button_Creer = new JButton("Creer");
		horizontalBox_Creer.add(Button_Creer);
		Button_Creer.setEnabled(false);
		
		Button_CreationResultat = new JButton("Resultat");
		horizontalBox_Creer.add(Button_CreationResultat);
		Button_CreationResultat.setEnabled(false);
		
		lblResultatCreer = new JLabel("");
		horizontalBox_Creer.add(lblResultatCreer);
		
		lblAnalyser = new JLabel("6. Analyser ARFF : ");
		verticalBox.add(lblAnalyser);
		
		horizontalBox_Analyser = Box.createHorizontalBox();
		horizontalBox_Analyser.setAlignmentX(Component.LEFT_ALIGNMENT);
		verticalBox.add(horizontalBox_Analyser);
		
		comboBox_NombreGroupe = new JComboBox<Integer>();
		comboBox_NombreGroupe.setModel(new DefaultComboBoxModel<Integer>(new  Integer[] {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25}));
		comboBox_NombreGroupe.setSelectedIndex(0);
		comboBox_NombreGroupe.setPreferredSize(new Dimension(50, 20));
		comboBox_NombreGroupe.setMinimumSize(new Dimension(50, 20));
		comboBox_NombreGroupe.setMaximumSize(new Dimension(50, 20));
		comboBox_NombreGroupe.setMaximumRowCount(21);
		horizontalBox_Analyser.add(comboBox_NombreGroupe);
		
		Button_Analyser = new JButton("Analyser");
		horizontalBox_Analyser.add(Button_Analyser);
		Button_Analyser.setEnabled(false);
		
		Button_AnalyseResultat = new JButton("Resultat");
		Button_AnalyseResultat.setHorizontalAlignment(SwingConstants.RIGHT);
		horizontalBox_Analyser.add(Button_AnalyseResultat);
		Button_AnalyseResultat.setEnabled(false);
		
		lblResultatAnalyser = new JLabel("");
		horizontalBox_Analyser.add(lblResultatAnalyser);
		horizontalBox_Analyser.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{comboBox_NombreGroupe, Button_Analyser, Button_AnalyseResultat, lblResultatAnalyser}));
		
		scrollPane = new JScrollPane();
		mainFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBounds(new Rectangle(10, 10, 200, 200));

		taResultat = new JTextArea();
		taResultat.setEditable(false);
		taResultat.setBounds(new Rectangle(10, 10, 300, 300));
		scrollPane.setViewportView(taResultat);
		
		panel_status = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_status.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		mainFrame.getContentPane().add(panel_status, BorderLayout.SOUTH);
		
		progressBar_filtrage = new JProgressBar();
		panel_status.add(progressBar_filtrage);
		progressBar_filtrage.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		lbl_IlYA = new JLabel("Il y a ");
		panel_status.add(lbl_IlYA);
		
		lbl_nbReq = new JLabel("00000");
		panel_status.add(lbl_nbReq);
		
		lblRequete = new JLabel("requete(s) dans");
		panel_status.add(lblRequete);
		
		lbl_nbSession = new JLabel("00000");
		panel_status.add(lbl_nbSession);
		
		lbl_Sessions = new JLabel("session(s)");
		panel_status.add(lbl_Sessions);
	}

	public static boolean getUtiliseXML() {
		return utiliseXML;
	}
}
