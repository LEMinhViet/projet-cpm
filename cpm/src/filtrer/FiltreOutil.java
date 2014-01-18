package filtrer;

public class FiltreOutil extends Filtre {
	private String outil;

	public FiltreOutil(Format f, String outil) {
		super(f);
		this.outil = outil;
	}

	@Override
	public boolean valideFiltre(String ligne) {
		if (f.valide(ligne)) {
			String s = f.creer(ligne).get(Traitement.SECTION_REFERENCE).toLowerCase();
			return s.contains(outil);
		}

		return false;
	}
}
