package filtrer;

public class FiltreTypeFichier extends Filtre {

	private String type;

	public FiltreTypeFichier(Format f, String type) {
		super(f);
		this.type = type;
	}

	@Override
	public boolean valideFiltre(String ligne) {
		if (f.valide(ligne)) {
			String s = f.creer(ligne).get(Traitement.SECTION_CONTENUE).toLowerCase();
			if (!s.equals("-")) {

				if (type.contains("|")) {
					String[] fichierTypes = type.split("\\|");

					for (String fType : fichierTypes) {
						// System.out.println(fType);
						if (s.split(" ")[1].endsWith(fType.toLowerCase()))
							return true;
					}
				}
				// else
				// return s.split(" ")[1].contains(type.toLowerCase());
			}
		}

		return false;
	}
}
