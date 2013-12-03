package filtrer;

import java.util.List;

import logs.Requete;
import logs.Utilisateur;

public class StockerLogs extends Filtre {

	public StockerLogs(Format f) {
		super(f);		
	}

	@Override
	/** C'est pour la question 2.3
	 * Cette fonction, pour chaque ligne dans le fichier logs, ajouter un requete dans l'ensemble d'utilisateur
	 * - un utilisateur a des sessions - un session a des requetes
	 */
	public boolean valideFiltre(String ligne) {
		if (f.valide(ligne)) {
			List<String> s = f.creer(ligne);
			
			Traitement.addUtilisateur(new Utilisateur(s.get(Traitement.SECTION_AGENT_UTILISATEUR), 
													  s.get(Traitement.SECTION_IP)), 
									  new Requete(s.get(Traitement.SECTION_TEMPS),
											  	  s.get(Traitement.SECTION_CONTENUE), 
											  	  s.get(Traitement.SECTION_STATUT_HTTP), 
											  	  s.get(Traitement.SECTION_TAILLE), 
											  	  s.get(Traitement.SECTION_REFERENCE)));
		}

		return true;
	}

}
