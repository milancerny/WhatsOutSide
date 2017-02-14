package com.example.mcerny.theweatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
//import android.icu.text.DecimalFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Util.Utils;
import data.CityPreference;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView currentDate;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;
    private EditText inputView;
    Context context = this;

    Weather weather = new Weather();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (TextView) findViewById(R.id.cityText);
        currentDate = (TextView) findViewById(R.id.currentDate);
        iconView = (ImageView) findViewById(R.id.thumbnailIcon);
        temp = (TextView) findViewById(R.id.tempText);
        description = (TextView) findViewById(R.id.cloudText);
        humidity = (TextView) findViewById(R.id.humidText);
//        pressure = (TextView) findViewById(R.id.pressureText);
        wind = (TextView) findViewById(R.id.windText);
//        sunrise = (TextView) findViewById(R.id.riseText);
//        sunset = (TextView) findViewById(R.id.setText);
        updated = (TextView) findViewById(R.id.updateText);

        CityPreference cityPreference = new CityPreference(MainActivity.this);

        if (!cityPreference.getCity().matches("")) {
            renderWeatherData(cityPreference.getCity());
        } else {
            renderWeatherData("Bratislava,sk");
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public static String currentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(c.getTime());

        return strDate;
    }

    //Convert unix time stamp to data type with format HH:mm
    public static String unixTimeStampToDataTime(double unixTimeStamp) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        date.setTime((long) unixTimeStamp * 1000);
        return dateFormat.format(date);
    }

    //get date with format dd MMMM yyyy HH:mm
    public static String getDateNow(double unixTimeStamp) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date = new Date();
        date.setTime((long) unixTimeStamp * 1000);
        return dateFormat.format(date);
    }

    public void renderWeatherData(String city) {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric"});
    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.mcerny.theweatherapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.mcerny.theweatherapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void showInfo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("About magic weather\r\n");

        final TextView text = new TextView(MainActivity.this);
        text.setText("\n\nAuthor Milan Černý\t\nPhotography by Jozef Hasilla\n\n\nUKF production 2016, All rights reserved");
        text.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        builder.setView(text);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                CityPreference cityPreference = new CityPreference(MainActivity.this);
//
//                cityPreference.setCity(cityInput.getText().toString());
//
//                if (!cityInput.getText().toString().matches("")) {
//                    String newCity = cityPreference.getCity();
//                    renderWeatherData(newCity);
//                }
            }
        });
        builder.show();
    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));
            String city = params[0].split("\\&units=")[0];
            final String title = city + " not found!";
            final String message = "Enter different city.";
            final String buttonTitle = "OK";


            if(data.matches("Error")){
                MainActivity.this.runOnUiThread(new Runnable() {

                    public void run() {
                        new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton(buttonTitle, null).show();

                    }
                });
            }

            weather = JSONWeatherParser.getWeather(data);
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
          /* DecimalFormat je az od API 24
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());
            String speedFormat = decimalFormat.format((weather.wind.getSpeed() * 3600) / 1000); */

            //FIXME : make a function or standalone class
            relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);

            Log.v("PREEEEEED", weather.currentCondition.getCondition().toString().toLowerCase());
            String status = weather.currentCondition.getCondition().toString().toLowerCase();

            if (status.equals("clear")) {
                relativeLayout.setBackgroundResource(R.drawable.clear);
            } else if(status.equals("clouds")) {
                relativeLayout.setBackgroundResource(R.drawable.clouds);
            } else if(status.equals("rain")) {
                relativeLayout.setBackgroundResource(R.drawable.rain);
            } else if(status.equals("thunderstorm")) {
                relativeLayout.setBackgroundResource(R.drawable.storm);
            } else if(status.equals("snow")) {
                relativeLayout.setBackgroundResource(R.drawable.snow);
            } else if(status.equals("mist") || status.equals("fog") || status.equals("smoke") || status.equals("haze")) {
                relativeLayout.setBackgroundResource(R.drawable.mist);
            } else {
                relativeLayout.setBackgroundColor(Color.rgb(79,79,79)); //default color
            }

            double tempFormat = round(weather.currentCondition.getTemperature(),1);
            double speedFormat = round((weather.wind.getSpeed() * 3600) / 1000,1);

            super.onPostExecute(weather);

            cityName.setText(weather.place.getCity()); //+ "," + weather.place.getCountry()
            currentDate.setText(currentDate());
            temp.setText(tempFormat + "°C");
//
            humidity.setText(weather.currentCondition.getHumidity() + "% humidity");
//            pressure.setText("Pressure: " + weather.currentCondition.getPressure() + " hPa");
            wind.setText(speedFormat + " km/h wind force");
//
//            sunrise.setText("Sunrise: " + unixTimeStampToDataTime(weather.place.getSunrise()));
//            sunset.setText("Sunset: " + unixTimeStampToDataTime(weather.place.getSunset()));
//
            updated.setText(getDateNow(weather.place.getLastupdate()) + " last updated");
            description.setText(weather.currentCondition.getCondition().toLowerCase()); // + " (" +weather.currentCondition.getDescription() + ")"
//
            //SET IMAGE
            Picasso.with(MainActivity.this)
                    .load(Utils.getImage(weather.currentCondition.getIcon()))
                    .into(iconView);
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change city");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Bratislava");
        builder.setView(cityInput);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CityPreference cityPreference = new CityPreference(MainActivity.this);

                cityPreference.setCity(cityInput.getText().toString());

                if (!cityInput.getText().toString().matches("")) {
                    String newCity = cityPreference.getCity();
                    renderWeatherData(newCity);
                }
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.change_cityId) {
            showInputDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}
