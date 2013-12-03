package filtrer;

public class FiltreStatutHTTP extends Filtre {

	private String code;

	public FiltreStatutHTTP(Format f, String code) {
		super(f);
		this.code = code;
	}

	@Override
	public boolean valideFiltre(String ligne) {
		if (f.valide(ligne)) {
			String s = f.creer(ligne).get(Traitement.SECTION_STATUT_HTTP);
			// LE : j'ai utilisé la fonction "matches" pour pouvoir trouver plusieurs statuts HTTP
//			return s.equalsIgnoreCase(code);
			return s.matches(code);
		}
		return false;
	}

}
