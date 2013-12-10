package logs;

import java.util.ArrayList;
import java.util.List;

import core.CPMCalendar;
import filtrer.Traitement;

public class Requete {

	private CPMCalendar date;
	private String contenue;
	private String statutHTTP;
	private String taille;
	private String reference;
	
	List<String> resultat = new ArrayList<>(Traitement.NUM_SECTION);;

	public Requete (String date, String contenue, String statusHTTP, String taille, String reference) {
		this.date = new CPMCalendar(date);
		this.contenue = contenue;
		this.statutHTTP = statusHTTP;
		this.taille = taille;
		this.reference = reference;
    }
	
	public long getTimeInMillis() {
		return date.getTimeInMillis();
	}
	
	public CPMCalendar getDate() {
		return date;
	}
	
	public String getContenue() {
		return contenue;
	}
	
	public String getStatutHTTP() {
		return statutHTTP;
	}
	
	public String getTaille() {
		return taille;
	}
	
	public String getReference() {
		return reference;
	}
	
	public String getPage() {
		return contenue.split(" ")[1];
	}

}
