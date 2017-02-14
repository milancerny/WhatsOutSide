package data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import Util.Utils;

/**
 * Created by mcernyux on 14.12.2016.
 */

public class WeatherHttpClient {

    public String getWeatherData(String place) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) (new URL(Utils.BASE_URL + place + "&appid=d4e760f6648758ca8527b55ae1556173")).openConnection();
            Log.v("URL", Utils.BASE_URL + place + "&appid=d4e760f6648758ca8527b55ae1556173");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoInput(true);
            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode == 400 || responseCode == 502){
                Log.d("ZLY KOD", "" + responseCode);
                return "Error";
            }

            //READ RESPONSE FROM GET
            StringBuffer stringBuffer = new StringBuffer();
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\r\n");
            }

            inputStream.close();
            connection.disconnect();
            Log.d("DOBRY KOD", "" + responseCode);

            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
