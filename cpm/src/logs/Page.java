package logs;

public class Page {
	public static final int NOMBRE_TOTAL = 0;
	public static final int NOMBRE_DEBUT = 1;
	public static final int NOMBRE_FIN = 2;
	
    private String lien;    // lien du page
    private int nombreTotal;   // nombre de visite au total
    private int nombreDebutDeSession;  // nombre de visite au premier de session
    private int nombreFinDeSession;   // nombre de visite en fin de session
    
    public Page(String lien) {
        this.lien = lien;
        this.nombreTotal = 0;
        this.nombreDebutDeSession = 0;
        this.nombreFinDeSession = 0;
    }
    
    public Page(Page p) {
        this.lien = p.getLien();
        this.nombreTotal = p.getNombreTotal();
        this.nombreDebutDeSession = p.getNombreDebutDeSession();
        this.nombreFinDeSession = p.getNombreFinDeSession();
    }
    
    public boolean estMemeLien(String l) {
    	return lien.equals(l);
    }
    
    /*
     * GET FONCTION
     */
    public String getLien() {
        return lien;
    }
    
    public int getNombreTotal() {
        return nombreTotal;
    }
    
    public int getNombreDebutDeSession() {
        return nombreDebutDeSession;
    }
    
    public int getNombreFinDeSession() {
        return nombreFinDeSession;
    }
    
    public int getNombre(int type) {
    	if (type == NOMBRE_TOTAL) {
    		return getNombreTotal();
    	} else if (type == NOMBRE_DEBUT) {
    		return getNombreDebutDeSession();
    	} else if (type == NOMBRE_FIN) {
    		return getNombreFinDeSession();
    	}
    	return 0;
    }
    
    /*
     * SET FONCTION
     */
    public void setLien(String lien) {
        this.lien = lien;
    }
    
    public void setNombreTotal(int value) {
        nombreTotal = value;
    }
    
    public void setNombreDebutDeSession(int value) {
        nombreDebutDeSession = value;
    }
    
    public void setNombreFinDeSession(int value) {
    	nombreFinDeSession = value;
    }
    
    /*
     * INCREMENTER FONCTION
     */
    public void incrementerNombreTotal() {
        nombreTotal++;
    }
    
    public void incrementerNombreDebutDeSession() {
        nombreDebutDeSession++;
    }
    
    public void incrementerNombreFinDeSession() {
    	nombreFinDeSession++;
    }
}

