package filtrer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatLog extends Format {

	private static final Pattern pattern = Pattern
			.compile("^([\\w.-]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d+|-) (\\d+|-) \"([^\"]+)\" \"([^\"]+)\"$");

	@Override
	public List<String> creer(String ligne) {
		List<String> resultat = new ArrayList<>(Traitement.NUM_SECTION);

		Matcher match = pattern.matcher(ligne);
		match.matches();
		for (int i = 1; i < match.groupCount() + 1; i++) {
			resultat.add(match.group(i));
		}
		return resultat;
	}

	@Override
	public boolean valide(String ligne) {
		Matcher match = pattern.matcher(ligne);

		if (!match.matches() || match.groupCount() != Traitement.NUM_SECTION) {
			System.err.println("ERROR REGEX ON : " + ligne);
			return false;
		}
		return true;
	}

}
