package data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.example.mcerny.theweatherapp.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Util.Utils;
import model.Place;
import model.Weather;

/**
 * Created by mcernyux on 14.12.2016.
 */

public class JSONWeatherParser {

    public static Weather getWeather(String data) { //string from buffer
        Weather weather = new Weather();
        String defaultCity = "bratislava&units=metric";

        Log.d("DATA", data);
        if(data.matches("Error")){
            data = ((new WeatherHttpClient()).getWeatherData(defaultCity)); //if city not found, find default city (Bratislava)
        }


        //create JsonObject from data

        try {
            JSONObject jsonObject = new JSONObject(data);

            Place place = new Place();
            JSONObject coorObj = Utils.getObject("coord", jsonObject);
            place.setLat(Utils.getFloat("lat", coorObj));
            place.setLon(Utils.getFloat("lon", coorObj));

            //Get sys object
            JSONObject sysObj = Utils.getObject("sys", jsonObject);
            place.setCountry(Utils.getString("country", sysObj));
            place.setLastupdate(Utils.getInt("dt", jsonObject));

            place.setSunrise(Utils.getInt("sunrise", sysObj));
            place.setSunset(Utils.getInt("sunset", sysObj));
            place.setCity(Utils.getString("name", jsonObject));

            weather.place = place;

            JSONObject mainObj = Utils.getObject("main", jsonObject);
            weather.currentCondition.setTemperature(Utils.getDouble("temp", mainObj));
            weather.currentCondition.setPressure(Utils.getString("pressure", mainObj));
            weather.currentCondition.setHumidity(Utils.getString("humidity", mainObj));

            //Get weather info
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(Utils.getInt("id", jsonWeather));
            weather.currentCondition.setDescription(Utils.getString("description", jsonWeather));
            weather.currentCondition.setIcon(Utils.getString("icon", jsonWeather));


            weather.currentCondition.setCondition(Utils.getString("main", jsonWeather));
            weather.currentCondition.setIcon(Utils.getString("icon", jsonWeather));

            JSONObject windObj = Utils.getObject("wind", jsonObject);
            weather.wind.setSpeed(Utils.getFloat("speed", windObj));

            if(windObj.has("deg")) {
                weather.wind.setDeg(Utils.getFloat("deg", windObj));
            }

            JSONObject cloudObj = Utils.getObject("clouds", jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all", cloudObj));

            return weather;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
