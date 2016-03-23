import org.apache.commons.lang.StringUtils;


public class Search {

	public static void main(String args[])
	{
		int distance =StringUtils.getLevenshteinDistance("nitsha vk","pitsha");
		System.out.println(distance);
	}
}
