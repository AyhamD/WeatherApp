package com.example.weatherapp;

import android.util.Pair;

public class WeatherForecast {

    public String temperature;
    public String windSpeed;
    public String winDirection;
    public String cloudiness;
    public Pair<String,String> precipition;
    public String symbol;

    public WeatherForecast(String temperature, String windSpeed, String windDirectionm, String coludiness,Pair<String,String> precipition ,String sympol){
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.winDirection = windDirectionm;
        this.cloudiness = coludiness;
        this.precipition = precipition;
        this.symbol = sympol;
    }
    @Override
    public String toString() {
        return "Temperature: " + temperature + " celsius\n" +
                "Wind speed: " + windSpeed + " mps, toward " + winDirection + "\n" +
                "Cloudiness: " + cloudiness + "%\n" +
                "Precipitation: between " + precipition.first + " mm and " + precipition.second + " mm\n";
    }
}
