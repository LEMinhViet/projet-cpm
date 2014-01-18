package filtrer;

public class FiltreUtilisateur extends Filtre {
	private String utilisateur;
		
	public FiltreUtilisateur(Format f, String utilisateur) {
		super(f);
		this.utilisateur = utilisateur;
	}

	@Override
	public boolean valideFiltre(String ligne) {
		if (f.valide(ligne)) {
			String s = f.creer(ligne).get(Traitement.SECTION_UTILISATEUR);
			return utilisateur.equals(s);
		}

		return false;
	}
}
