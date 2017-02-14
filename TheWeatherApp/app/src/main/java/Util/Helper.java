package Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mcernyux on 15.12.2016.
 */

public class Helper {

    static String stream = null;

    public Helper() {

    }

    //make request to OpenWeatherMap API and get return result
    public String getHTTPData(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            if(httpURLConnection.getResponseCode() == 200) { //if is response status code = 200 - OK
                BufferedReader r = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = r.readLine()) != null)
                    sb.append(line);
                stream = sb.toString();
                httpURLConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream;
    }

}
