package com.example.weatherapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class WeatherFragment extends Fragment {

    private Typeface weatherFont;
    private TextView cityField;
    private TextView updatedField;
    private TextView detailsField;
    private TextView currentTemperatureField;
    private TextView weatherIcon;

    private Handler handler;

    public WeatherFragment() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = layoutInflater.inflate(R.layout.fragment_weather, container, false);
        cityField = rootView.findViewById(R.id.city_field);
        updatedField = rootView.findViewById(R.id.updated_field);
        detailsField = rootView.findViewById(R.id.details_field);
        currentTemperatureField = rootView.findViewById(R.id.current_temp);
        weatherIcon = rootView.findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "font/weather.ttf");
        updateWeatherData(new CityPreference(getActivity()).getCity());
    }

    private void updateWeatherData (final String city) {
        new Thread() {
            public void run() {
                final JSONObject jsonObject = RemoteFetch.getJSON(getActivity(), city);
                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), getActivity().getString(R.string.place_not_found), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(jsonObject);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject jsonObject) {
        try {
            cityField.setText(jsonObject.getString("name").toUpperCase(Locale.ENGLISH) +
                    ", " + jsonObject.getJSONObject("sys").getString("country"));
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");
            detailsField.setText(details.getString("description").toUpperCase(Locale.ENGLISH) +
                    "\n" + "Humidity: " + main.getString("humidity") + "%" +
                    "\n" + "Pressure: " + main.getString("pressure" + "hPa"));

            currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + " ℃");
            DateFormat df = DateFormat.getDateInstance();
            String updatedOn = df.format(new Date(jsonObject.getLong("dt") * 1000));
            updatedField.setText("Last update: " + updatedOn);
            setWeatherIcon(details.getInt("id"), 
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setWeatherIcon(int currentId, long sunrise, long sunset) {
        int id = currentId / 100;
        String icon = "";
        if (currentId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2: icon = getActivity().getString(R.string.weather_thunder);
                        break;
                case 3: icon = getActivity().getString(R.string.weather_drizzle);
                        break;
                case 5: icon = getActivity().getString(R.string.weather_foggy);
                        break;
                case 6: icon = getActivity().getString(R.string.weather_cloudy);
                        break;
                case 7: icon = getActivity().getString(R.string.weather_snowy);
                        break;
                case 8: icon = getActivity().getString(R.string.weather_rainy);
                        break;
            }
        }
        weatherIcon.setText(icon);
    }


    public TextView getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(TextView weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public TextView getCurrentTemperatureField() {
        return currentTemperatureField;
    }

    public void setCurrentTemperatureField(TextView currentTemperatureField) {
        this.currentTemperatureField = currentTemperatureField;
    }

    public TextView getDetailsField() {
        return detailsField;
    }

    public void setDetailsField(TextView detailsField) {
        this.detailsField = detailsField;
    }

    public TextView getCityField() {
        return cityField;
    }

    public void setCityField(TextView cityField) {
        this.cityField = cityField;
    }

    public Typeface getWeatherFont() {
        return weatherFont;
    }

    public void setWeatherFont(Typeface weatherFont) {
        this.weatherFont = weatherFont;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public TextView getUpdatedField() {
        return updatedField;
    }

    public void setUpdatedField(TextView updatedField) {
        this.updatedField = updatedField;
    }
}
