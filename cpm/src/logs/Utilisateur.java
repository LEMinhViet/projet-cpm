package logs;

import java.util.ArrayList;

import UI.MainGUI;

public class Utilisateur {
	
	public static final int[] ETAPE = new int[] {1, 2, 3, 4};
	
	private String ip;
	private String nom;
	private String agent;
    private ArrayList<Session> sessions = new ArrayList<Session>(); 	// liste de tous les sessions
    
    public Utilisateur (String agent, String ip, String nom) {
        this.ip = ip;
        this.agent = agent;
        this.nom = nom;
    }
    
    public String getIP() {
        return ip;
    }
    
    public String getNom() {
    	return nom;
    }
    
    public void addSession(Session ses) {
        sessions.add(ses);
    }
    
    public Session getDerniereSession() {
    	if (sessions.size() == 0) {
    		return null;
    	} else {
    		return sessions.get(sessions.size() - 1);
    	}
    }
    
    public ArrayList<Session> getSessions() {
        return sessions;
    }
    
    public int getNombreAction(int etape) {
    	int nombre = 0;    	
    	for (int i = 0; i < sessions.size(); i++) {
    		if (etape == sessions.get(i).getEtape()) {
    			nombre = sessions.get(i).getNombreRequetes();
    		}
    	}    	
    	return nombre;
    }
    
    public int getNombreText() {
    	int nombre = 0;    	
    	for (int i = 0; i < sessions.size(); i++) {
    		nombre += sessions.get(i).getNombreText();
    	}    	
    	return nombre;
    }
    
    public boolean egalUtilisateur(Utilisateur utilisateur) {
    	return (ip.equals(utilisateur.ip) && agent.equals(utilisateur.agent));
    }
    
    /**
     * - Ajouter une session s'il n'y a rien de session
     * - Si non, verifier si la derniere session est terminée ou non
     * 	+ Si oui, creer une nouvelle session
     * 	+ Si non, ajouter le requete dans cette session
	 * 	
     * @param requete
     */
    public void addRequete(Requete requete) {
    	if (getDerniereSession() != null) {
    		if (!MainGUI.getUtiliseXML() && getDerniereSession().estNouvelleSession_30mins(requete)) {
    			Session session = new Session();
        		session.addRequete(requete);
        		this.addSession(session);
    		} else if (MainGUI.getUtiliseXML() && getDerniereSession().estNouvelleSession_tache(requete)) {
    			Session session = new Session();
        		session.addRequete(requete);
        		this.addSession(session);
    		} else {
    			getDerniereSession().addRequete(requete);
    		}
    	} else {
    		Session session = new Session();
    		session.addRequete(requete);
    		this.addSession(session);
    	}
    }
}

