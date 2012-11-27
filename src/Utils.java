public class Utils {
	public static String truncate(String string, int width){
		if(string.length() < width){
			return string;
		}
		
		return string.substring(0, width) + "...";
	}
}