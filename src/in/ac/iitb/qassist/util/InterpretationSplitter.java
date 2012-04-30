package in.ac.iitb.qassist.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterpretationSplitter {
	private static final String INTERPRETATIONS_REGEX = "//[0-9/]+//";
	private static Pattern interpretationsPattern;
	private String encodedInterpretation;

	static {
		interpretationsPattern = Pattern.compile(INTERPRETATIONS_REGEX);
	}

	public InterpretationSplitter() {

	}

	public InterpretationSplitter(String encoded) {
		encodedInterpretation = encoded;
	}

	public String[] getInterpretations() {
		Matcher matcher = interpretationsPattern.matcher(encodedInterpretation);
		String match = null;
		String[] entities = null;
		List<String> entityList = new ArrayList<String>();
		while (matcher.find()) {
			match = matcher.group().replaceAll("//", "");
			entities = match.split("/");
			entityList.addAll(Arrays.asList(entities));
		}
		return (String[]) entityList.toArray(new String[entityList.size()]);
	}
}
