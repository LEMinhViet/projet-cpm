package logs;

import java.util.ArrayList;
import java.util.List;

public class Session {
    // Une session a ses requetes
    private ArrayList<Requete> requetes = new ArrayList<Requete>();
    private int[] pagesVisites;
    
    public void addRequete(Requete req) {
    	requetes.add(req);
    }
    
    public ArrayList<Requete> getRequetes() {
        return requetes;
    }   
    
    public int getNombreRequetes() {
    	return requetes.size();
    }
    
    public int getEtape() {
    	// seulement pour le fichier COFFEE
    	return Integer.valueOf(requetes.get(0).getStatutHTTP());
    }
    
    public int getNombreText() {
    	int nombre = 0;
    	for (int i = 0; i < requetes.size(); i++) {
    		nombre += requetes.get(i).getNombreText();
    	}    	
    	return nombre;
    }
    
    public Requete getDerniereRequete() {
    	return requetes.get(requetes.size() - 1);
    }
    
    public boolean estNouvelleSession_30mins(Requete requete) {
    	// Comparer la duree entre le derniere requete de la session et le nouveau requete,
    	// Si elle > 30m * 60s * 1000ms => on a une nouvelle session
    	return requete.getTimeInMillis() - getDerniereRequete().getTimeInMillis() > 30 * 60 * 1000;
    }
    
    public boolean estNouvelleSession_tache(Requete requete) {
    	// La reference est maintenant l'outil, et le statu HTTP est l'etape de COFFEE
    	// => ici on compare si l'utilisateur fait une autre tache ou il change a un autre etape
    	return !(getDerniereRequete().getReference().equals(requete.getReference())
    				&& getDerniereRequete().getStatutHTTP().equals(requete.getStatutHTTP()));
    }
    
    public long getDuree() {
    	return getDerniereRequete().getTimeInMillis() - requetes.get(0).getTimeInMillis();
    }
    
    public void genererPagesVisites(List<Page> pages) {
    	pagesVisites = new int[pages.size()];
    	
    	for (Requete req : requetes) {
    		for (int i = 0; i < pages.size(); i++) {
    			if (req.getPage().equals(pages.get(i).getLien())) {
    				pagesVisites[i] = 1;
    			}
    		}
    	}
    }
    
    public int[] getPagesVisites() {
    	return pagesVisites;
    }
}

