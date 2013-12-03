package filtrer;

import java.util.List;

public abstract class Format{
	
	public abstract boolean valide(String ligne);
	
	public abstract List<String> creer(String ligne);

}
