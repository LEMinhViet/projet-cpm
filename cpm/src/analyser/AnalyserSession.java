package analyser;

import java.util.ArrayList;
import java.util.List;

import logs.Page;
import logs.Session;
import logs.Utilisateur;

/**
 * e partir de la question 2.4, on doit travailler sur des parcours et non pas des requetes
 * Cette classe est pour separer avec la premiere partie du programme
 */
public class AnalyserSession {
		
	private List<Utilisateur> utilisateurs;
	private List<Session> sessions;
	private List<Page> pages;
	
	private TraiterARFF traiterArff;
	
	public AnalyserSession(List<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
		pages = new ArrayList<Page>();
		sessions = new ArrayList<Session>();
		
		traiterPages();
		
		genererPagesVisitesPourSession();
	}
	
	/**
	 * Chercher tous les pages dans les requetes, sauf les pages blanche
	 */
	public void traiterPages() {
		String page;
		
		for (Utilisateur u : utilisateurs) {
			for (Session ses : u.getSessions()) {
				
				sessions.add(ses);
				
				for (int i = 0; i < ses.getRequetes().size(); i++) {
					page = ses.getRequetes().get(i).getPage();
					if (!page.equals("-")) {
						addPage(page, 
								i == 0,  // La requete est au debut de session ou non
								i == ses.getRequetes().size() - 1); // La requete est en fin de session ou non
					}
				}
			}
		}
	}
	
	/**
	 * Afficher les pages
	 */
	public void afficherPages(int nombreDePage) {
		int nombre = 0;
		
		for (Page p : pages) {
			if (nombre == nombreDePage) {
				return;
			}
			nombre++;
			System.out.println(nombre + ". " + p.getLien() + " " + p.getNombreTotal() 
														   + " " + p.getNombreDebutDeSession() 
														   + " " + p.getNombreFinDeSession());			
		}
	}
	
	/**
	 * Si il y a deja la page dans la liste, incrementer ses nombres de visite
	 * Si non, ajouter une page dans la liste
	 */
	public void addPage(String page, boolean estDebutDeSession, boolean estFinDeSession) {
		Page newPage;
		for (Page p : pages) {
			if (p.estMemeLien(page)) {
				
				p.incrementerNombreTotal();
				if (estDebutDeSession)	p.incrementerNombreDebutDeSession();
				if (estFinDeSession) 	p.incrementerNombreFinDeSession();
				
				return;
			} 
		}
		
		newPage = new Page(page);
		newPage.setNombreTotal(1);
		newPage.setNombreDebutDeSession(estDebutDeSession ? 1 : 0);
		newPage.setNombreFinDeSession(estFinDeSession ? 1 : 0);
		
		pages.add(newPage);		
	}
	
	/**
	 * Il y a 3 types de trier les pages 
	 * 0 - NOMBRE de visite au TOTAL
	 * 1 - NOMBRE de visite au DEBUT de Session
	 * 2 - NOMBRE de visite en FIN de Session
	 * @param type
	 */
	public void trierPages(int type) {
		Page tmpPage;
        for (int i = 0; i < pages.size(); i++) {
            for (int j = i + 1; j < pages.size(); j++) {
                if (pages.get(j).getNombre(type) > pages.get(i).getNombre(type)) {
                	// echanger les 2 pages
                    tmpPage = new Page(pages.get(j));
                    
                    pages.set(j, pages.get(i));
                    pages.set(i, tmpPage);

                }
            }
        }       
	}
	
	public List<Page> getPages() {
		return pages;
	}
	
	public List<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}
	
	
	/*
	 * Les fonctions pour traiter fichier ARFF
	 */
	public void genererPagesVisitesPourSession() {
		for (Session ses : sessions) {
			ses.genererPagesVisites(pages);
		}
	}

	public String creerARFF(String nomFichier) {
		traiterArff = new TraiterARFF();
		traiterArff.creerARFF(sessions, pages, nomFichier.split("\\.")[0] + ".arff");	// Couper .txt dans le fichier logs
		return nomFichier.split("\\.")[0] + ".arff";
	}
	
	public String analyserARFF(String nomFichier) {
		traiterArff.traiterARFF_KMeans(nomFichier, 9);
		return traiterArff.interpreterCluster(nomFichier.split("\\.")[0] + "_kmeans.arff"); 
	}
}
