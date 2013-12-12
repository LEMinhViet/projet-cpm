package filtrer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import analyser.AnalyserSession;
import logs.Requete;
import logs.Session;
import logs.Utilisateur;

public class Traitement {
	// Pour + d'info : http://httpd.apache.org/docs/1.3/logs.html
	public static final int NUM_SECTION = 9;

	public static final int SECTION_IP = 0;
	public static final int SECTION_UTILISATEUR = 1; //
	public static final int SECTION_ID = 2;
	public static final int SECTION_TEMPS = 3;
	public static final int SECTION_CONTENUE = 4;
	public static final int SECTION_STATUT_HTTP = 5;
	public static final int SECTION_TAILLE = 6;
	public static final int SECTION_REFERENCE = 7;
	public static final int SECTION_AGENT_UTILISATEUR = 8;

	private List<Filtre> filtrage;
	// Parce que'n doit trier les requetes par utilisateur, donc on stocke les
	// utilisateurs dans cette liste
	private static List<Utilisateur> utilisateurs;
	
	private AnalyserSession analyserSession;
	private String fichier;

	public Traitement(String fichier) {
		this.fichier = fichier;
		filtrage = new LinkedList<Filtre>();
		utilisateurs = new LinkedList<Utilisateur>();
	}

	public void addFiltre(Filtre f) {
		filtrage.add(f);
	}
	
	public void removeTousFiltres() {
		filtrage.clear();
	}

	public String traiteFiltre() throws IOException {

		String resultat = "";
		int nb = 0;
		InputStream ips = new FileInputStream(fichier);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);

		String ligne;

		while ((ligne = br.readLine()) != null) {
			ligne = ligne.replace("\uFEFF", "");
			boolean valide = true;
			for (Filtre f : filtrage) {
				if (!f.valideFiltre(ligne)) {
					valide = false;
					break;
				}
			}

			if (valide) {
				nb++;
				resultat += ligne + "\n";
				System.out.println(ligne);
			}

		}
		System.out.println(nb);
		br.close();
		
		return resultat;
	}

	/**
	 * LE : cette fonction est pour ajouter un utilisateur dans la liste -
	 * Ajouter un nouveau utilisateur s'il n'existe pas - Si non, ajouter un
	 * nouveau requete dans cet utilisateur
	 * 
	 * @param u
	 */
	public static void addUtilisateur(Utilisateur utilisateur, Requete requete) {
		for (Utilisateur u : utilisateurs) {
			if (u.egalUtilisateur(utilisateur)) {
				u.addRequete(requete);
				return;
			}
		}

		utilisateur.addRequete(requete);
		utilisateurs.add(utilisateur);
	}

	/**
	 * Traverser tous les utilisateurs pour denombrer le nombre des sessions
	 */
	public int afficherNombreDeSession() {
		int nomSession = 0;
		long dureeSession = 0;
		int nomReq = 0;

		for (Utilisateur u : utilisateurs) {
			nomSession += u.getSessions().size();

			for (Session s : u.getSessions()) {
				nomReq += s.getRequetes().size();
				dureeSession += s.getDuree();
			}
		}

		if (nomSession != 0) {
			System.out.println(" Il y a " + nomSession + " sessions dans " + nomReq + " requetes \n" + "  dans " + (dureeSession / 1000)
					+ " seconds => la duree moyenne est " + (dureeSession / 1000) / nomSession);
		}
		return nomSession;
	}

//	public static void main(String[] args) throws IOException {
//		Traitement t = new Traitement("bourges.txt");
//		Format f = new FormatLog();
//
//		// Pour la question 2.1
//		t.addFiltre(new FiltreStatutHTTP(f, "2..|3.."));
//		t.addFiltre(new FiltreTypeFichier(f, ".htm|.html"));
//		// t.addFiltre(new FiltreNavigateurs(f));
//		// t.addFiltre(new FiltreDate(f, "05/Sep/2004", "05/Sep/2004"));
//		// t.addFiltre(new FiltreIP(f, "hamburg047.server4you.de"));
//		t.addFiltre(new StockerLogs(f));
//		t.traiteFiltre();
//
//		// Pour la question 2.3
//		t.afficherNombreDeSession();
//
//		// Pour la question 2.4
//		AnalyserSession analyserSession = new AnalyserSession(utilisateurs);
//		analyserSession.trierPages(Page.NOMBRE_TOTAL);
//		analyserSession.afficherPages(10);
//	}
	
	public void analyserSession() {
		analyserSession = new AnalyserSession(utilisateurs);
	}
	
	public void trierPages(int type) {
		analyserSession.trierPages(type);
	}
	
	public String creerARFF(String nomFichier) {
		return analyserSession.creerARFF(nomFichier);
	}
	
	public String analyserARFF(String nomFichier) {
		return analyserSession.analyserARFF(nomFichier);
	}
	
	public String getFichier() {
		return fichier;
	}
	
	public List<Filtre> getFiltrage() {
		return filtrage;
	}
	
	public AnalyserSession getAnalyserSession() {
		return analyserSession;
	}
}
