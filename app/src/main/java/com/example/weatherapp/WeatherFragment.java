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

import org.json.JSONObject;


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