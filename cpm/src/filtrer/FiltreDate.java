package filtrer;

import core.CPMCalendar;

public class FiltreDate extends Filtre {

	private String debut;
	private String fin;
	
	public FiltreDate(Format f, String debut, String fin) {
		super(f);
		// TODO Auto-generated constructor stub
		this.debut = debut;
		this.fin = fin;
	}

	@Override
	public boolean valideFiltre(String ligne) {
		if (f.valide(ligne)) {
			String adresse = f.creer(ligne).get(Traitement.SECTION_TEMPS);
			CPMCalendar date = new CPMCalendar(adresse);
			
			if ((debut == null || debut.isEmpty()) && (fin == null || fin.isEmpty())) {
				
				return false;
			} else if (debut == null || debut.isEmpty()) {
				
				CPMCalendar date_fin = new CPMCalendar(fin, date.getTimeZone());
				return date.compareDate(date_fin) == 0;
			} else if (fin == null || fin.isEmpty()) {
				
				CPMCalendar date_debut = new CPMCalendar(debut, date.getTimeZone());
				return date.compareDate(date_debut) == 0;
			} else {
				
				CPMCalendar date_fin = new CPMCalendar(fin, date.getTimeZone());
				CPMCalendar date_debut = new CPMCalendar(debut, date.getTimeZone());
				
				return (date.compareDate(date_debut) >= 0 &&
						date.compareDate(date_fin) <= 0);
			}
		}

		return false;
	}

}
