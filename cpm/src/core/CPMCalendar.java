package core;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CPMCalendar extends GregorianCalendar {
		
	// Avec le texte qu'on filtre du fichier log, on utilise cette fonction pour
	// sauvegarder tous les infos (Date, Temps, TimeZone)
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CPMCalendar(String date) {		
		super();
		
		date = remplacerMois(date);
		
		SimpleDateFormat format;
	
		// Transformer le texte (String) à temps (Calendar)
		format = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss Z");
		// Set le Calendar à sa propre TimeZone
		setTimeZone(TimeZone.getTimeZone("GMT" + date.split(" ")[1]));
		
		try {
			setTime(format.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// Avec le texte qui a seulement la date (sans temps, sans TimeZone, utilisé pour comparer les dates), 
	// on utilise cette fonction 
	
	public CPMCalendar(String date, TimeZone timeZone) {		
		super();
		
		date = remplacerMois(date);
		
		SimpleDateFormat format;
		
		format = new SimpleDateFormat("dd/MM/yyyy");
		format.setTimeZone(timeZone);
				
		try {
			setTime(format.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * Comparer 2 date
	 * @param autreDate : la date pour comparer
	 * @return
	 */
	public int compareDate(CPMCalendar autreDate) {
		CPMCalendar date = this;
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);

		// Si on utilise la fonction compareTo du Java (de la classe Calendar), il va calculer avec 
		// les temps du TimeZone de l'ordinateur qui executer ce programme. 
		// Donc on compare comme ci-dessous pour verifier que les 2 dates sont le TimeZone approprié
		if (get(Calendar.YEAR) == autreDate.get(Calendar.YEAR)) {
			if (get(Calendar.MONTH) == autreDate.get(Calendar.MONTH)) {
				if (get(Calendar.DATE) == autreDate.get(Calendar.DATE)) {
					return 0;
				} else {
					return get(Calendar.DATE) - autreDate.get(Calendar.DATE);
				}
			} else {
				return get(Calendar.MONTH) - autreDate.get(Calendar.MONTH);
			}
		} else {
			return get(Calendar.YEAR) - autreDate.get(Calendar.YEAR);
		}
	}
	
	public void printDate() {
		System.out.println("date " + get(Calendar.DATE) + 
							" " + get(Calendar.MONTH) + 	// Janvier = 0 => Septembre = 8
							" " + get(Calendar.YEAR) + 
							" " + get(Calendar.HOUR_OF_DAY) + 		// Avec le timeZone actuel
							" " + get(Calendar.MINUTE) + 
							" " + get(Calendar.SECOND) + 
							" " + getTimeZone().toString());
	}
	
	/**
	 * Remplacer les text du mois en chiffres pour faciliter le control
	 * @param date
	 * @return
	 */
	private String remplacerMois(String date) {
		
		if (date.contains("Jan")) {
			return date.replace("Jan", "01");
		} else if (date.contains("Feb")) {
			return date.replace("Feb", "02");
		} else if (date.contains("Mar")) {
			return date.replace("Mar", "03");
		} else if (date.contains("Apr")) {
			return date.replace("Apr", "04");
		} else if (date.contains("May")) {
			return date.replace("May", "05");
		} else if (date.contains("Jun")) {
			return date.replace("Jun", "06");
		} else if (date.contains("Jul")) {
			return date.replace("Jul", "07");
		} else if (date.contains("Aug")) {
			return date.replace("Aug", "08");
		} else if (date.contains("Sep")) {
			return date.replace("Sep", "09");
		} else if (date.contains("Oct")) {
			return date.replace("Oct", "10");
		} else if (date.contains("Nov")) {
			return date.replace("Nov", "11");
		} else if (date.contains("Dec")) {
			return date.replace("Dec", "12");
		}
		
		return date;
	}
}
