package logs;

import java.util.ArrayList;

public class Utilisateur {
	
	private String ip;
	private String agent;
    private ArrayList<Session> sessions = new ArrayList<Session>(); 	// liste de tous les sessions
    
    public Utilisateur (String agent, String ip) {
        this.ip = ip;
        this.agent = agent;
    }
    
    public String getIP() {
        return ip;
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
    		if (getDerniereSession().estNouvelleSession(requete)) {
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

