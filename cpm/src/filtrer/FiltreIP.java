package filtrer;

public class FiltreIP extends Filtre {

	private String ip;
	
	public FiltreIP(Format f, String ip) {
		super(f);
		this.ip = ip;
	}

	@Override
	public boolean valideFiltre(String ligne) {
		if (f.valide(ligne)) {
			String s = f.creer(ligne).get(Traitement.SECTION_IP);
			return(ip.equals(s));
		}

		return false;
	}
	
}
