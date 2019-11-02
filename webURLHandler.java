import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class webURLHandler {

	public static String pageToString(String URLString) {
		String result = "";
		try {
			URL url = new URL(URLString);	// turn string into URL
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String str;	// temp variable to store each line
			while ((str = in.readLine()) != null) {
				result += str;
				System.out.println(str);	// debugging
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return result;
	}
	
	
}
