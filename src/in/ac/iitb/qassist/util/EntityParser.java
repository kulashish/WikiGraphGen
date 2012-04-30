package in.ac.iitb.qassist.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityParser {

//	private static final String ENTITY_REGEX = "([\\w-'\\s]*+).[0-9/NA]*.";
	private static final String ENTITY_REGEX = "([-\\w'\\s]*+)\\x7C[\\dNA]+/\\d+\\x7C";
	private static Pattern entityPattern;
	private String encodedEntity;

	static {
		entityPattern = Pattern.compile(ENTITY_REGEX);
	}

	public EntityParser(String encoded) {
		encodedEntity = encoded;
	}

	public String[] getEntityNames() {
		Matcher matcher = entityPattern.matcher(encodedEntity);
		String match = null;
		List<String> entityNamesList = new ArrayList<String>();
//		System.out.println("Count: " + matcher.groupCount());
		while (matcher.find()) {
			match = matcher.group(1);
			System.out.println(match);
			if(null!=match)
			entityNamesList.add(match.trim());
		}
//		System.out.println("Count: " + entityNamesList.size());
		return (String[]) entityNamesList.toArray(new String[entityNamesList
				.size()]);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String in = "rajiv gandhi administration |28184752/0| nehru administration |28184948/0| prime minister's office india |31559773/0| indian emergency 1975–1977 |27972051/0| indian national congress organisations |28863819/0| indian youth congress |30926118/0| indian marxists |11184974/0| all india forward bloc politicians |15264033/0| indian christian socialists |17152094/0| indian hare krishnas |17335478/0| indian shaivites |17336625/0| indian hindu missionaries |20814727/0| indian vaishnavites |21198570/0| indian emergency 1975–1977 |27972051/0| jawaharlal nehru |16243/1| prime minister of india |24452/1| rajiv gandhi |26129/1| premiership of morarji desai |23850188/1| premiership of lal bahadur shastri |23915381/1| prime minister's office india |30476919/1| jawaharlal nehru |16243/1| mohandas karamchand gandhi |19379/1| rajiv gandhi |26129/1| indian national congress |149333/1| sonia gandhi |169798/1| sanjay gandhi |562629/1| motilal nehru |762733/1| indian youth congress |1108844/1| indian national congress organisation |1526859/1| ashok kumar indian politician |3442008/1| national students union of india |18019450/1| indian national congress breakaway parties |24816453/1| mohandas karamchand gandhi |19379/1| rajiv gandhi |26129/1| list of assassinated indian politicians |5979506/1| jawaharlal nehru |16243/1| mohandas karamchand gandhi |19379/1| virchand gandhi |3007865/1| mohandas karamchand gandhi |19379/1| rajiv gandhi |26129/1| harilal gandhi |990119/1| kamala nehru |1727530/1|";
//		String in = "Indira /225587| Gandhi (disambiguation) /1638937|";
		EntityParser parser = new EntityParser(in);
		parser.getEntityNames();
	}

}
