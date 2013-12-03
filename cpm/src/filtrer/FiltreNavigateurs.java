package filtrer;

public class FiltreNavigateurs extends Filtre {

	public FiltreNavigateurs(Format f) {
		super(f);
	}

	@Override
	public boolean valideFiltre(String ligne) {
		if (f.valide(ligne)) {
			String adresse = f.creer(ligne).get(Traitement.SECTION_IP).toLowerCase();
			String systeme = f.creer(ligne).get(Traitement.SECTION_AGENT_UTILISATEUR).toLowerCase();
			return ((!adresse.contains("bot")) && (!systeme.contains("bot")));
		}
		return false;
	}
}
