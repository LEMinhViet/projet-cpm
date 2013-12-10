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
    
    public Requete getDerniereRequete() {
    	return requetes.get(requetes.size() - 1);
    }
    
    public boolean estNouvelleSession(Requete requete) {
    	// Comparer la duree entre le derniere requete de la session et le nouveau requete,
    	// Si elle > 30m * 60s * 1000ms => on a une nouvelle session
    	return requete.getTimeInMillis() - getDerniereRequete().getTimeInMillis() > 30 * 60 * 1000;
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

