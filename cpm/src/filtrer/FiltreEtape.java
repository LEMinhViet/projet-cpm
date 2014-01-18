package filtrer;

public class FiltreEtape extends Filtre {
	private String etape;
		
	public FiltreEtape(Format f, String etape) {
		super(f);
		this.etape = etape;
	}

	@Override
	public boolean valideFiltre(String ligne) {
		if (f.valide(ligne)) {
			String s = f.creer(ligne).get(Traitement.SECTION_STATUT_HTTP);
			return s.matches(etape);
		}

		return false;
	}
}