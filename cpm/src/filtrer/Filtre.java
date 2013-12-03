package filtrer;

public abstract class Filtre {

	protected Format f;
	
	public Filtre(Format f) {
		super();
		this.f = f;
	}

	public abstract boolean valideFiltre(String ligne);
	
}
