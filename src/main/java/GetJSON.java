import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * GetJSON connects to an exchange rate API to obtain currency rate data.
 * It is currently defaulted to retrieve rates relative to the U.S. dollar.
 */
public class GetJSON {

    /**
     * Sends a GET request to the exchange rate API to obtain currency rate data and store it in a String.
     */
    public static String getJSON() {
        String json = "";
        try {
            URL url = new URL("https://api.exchangeratesapi.io/latest?base=USD");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputRead;
            StringBuilder content = new StringBuilder();
            while ((inputRead = in.readLine()) != null) {
                content.append(inputRead);
            }
            in.close();

            json = content.toString();
            System.out.println(json);
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL");
        } catch (IOException e) {
            System.out.println("Connection to API could not be established");
        }
        return json;
    }
}
