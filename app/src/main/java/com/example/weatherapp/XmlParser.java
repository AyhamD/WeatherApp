package com.example.weatherapp;

import android.util.Log;
import android.util.Pair;
import android.util.Xml;
import android.widget.Button;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    public WeatherForecast parse(InputStream in) throws XmlPullParserException, IOException {
        try{
            XmlPullParser parse = Xml.newPullParser();
            parse.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parse.setInput(in,null);
            parse.nextTag();
            return readFeed(parse);
        }finally{
            in.close();
        }
    }

    public WeatherForecast readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        WeatherForecast weatherForecast = null;
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG,null,"weatherdata");
        while(entries.size()<2){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                parser.next();
                continue;
            }
            if(parser.getName().equals("location")){
                entries.add(readWeatherForecast(parser));
            }
            parser.next();
        }
        weatherForecast = (WeatherForecast) entries.get(0);
        WeatherForecast precipitationForeCast = (WeatherForecast) entries.get(1);
        weatherForecast.precipition = precipitationForeCast.precipition;
        weatherForecast.symbol = precipitationForeCast.symbol;
        return weatherForecast;
    }

    private WeatherForecast readWeatherForecast(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,null,"location");
        String temperature = null;
        String windDirection = null;
        String windSpeed = null;
        String cloudiness = null;
        Pair<String,String> precipition = null;
        String symbol = null;

        while(parser.next()!= XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            Log.d(getClass().toString(),name);
            if(name.equals("temperature")){
                temperature = readTemperature(parser);
            }else if(name.equals("windDirection")){
                windDirection = readWindDirection(parser);
            }else if(name.equals("windSpeed")){
                windSpeed = readWindSpeed(parser);
            }else if(name.equals("precipitation")){
                precipition = readPrecipitation(parser);
            }else if (name.equals("symbol")) {
                symbol = readSymbol(parser);
            }else if(name.equals("cloudiness")){
                cloudiness = readCloudiness(parser);
            }
        }
        return new WeatherForecast(temperature,windSpeed,windDirection,cloudiness,precipition,symbol);
    }

    private String readTemperature(XmlPullParser parse) throws IOException, XmlPullParserException {
        parse.require(XmlPullParser.START_TAG,null,"temperature");
        String temperature = parse.getAttributeValue(null,"value");
        parse.next();
        parse.require(XmlPullParser.END_TAG,null,"temperature");
        return temperature;
    }
    private String readWindDirection(XmlPullParser parse) throws IOException, XmlPullParserException {
        parse.require(XmlPullParser.START_TAG,null,"windDirection");
        String windDirection = parse.getAttributeValue(null,"name");
        parse.next();
        parse.require(XmlPullParser.END_TAG,null,"windDirection");
        return windDirection;
    }
    private String readWindSpeed(XmlPullParser parse) throws IOException, XmlPullParserException {
        parse.require(XmlPullParser.START_TAG,null,"windSpeed");
        String WindSpeed = parse.getAttributeValue(null,"mps");
        parse.next();
        parse.require(XmlPullParser.END_TAG,null,"windSpeed");
        return WindSpeed;
    }

    private String readCloudiness(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "cloudiness");
        String cloudiness = parser.getAttributeValue(null, "percent");
        parser.next();
        parser.require(XmlPullParser.END_TAG, null, "cloudiness");
        return cloudiness;
    }
    private Pair<String,String> readPrecipitation(XmlPullParser parse) throws IOException, XmlPullParserException {
        parse.require(XmlPullParser.START_TAG,null,"precipitation");
        String minPrecipitation = parse.getAttributeValue(null,"minvalue");
        String maxPrecipitation = parse.getAttributeValue(null,"maxvalue");
        parse.next();
        parse.require(XmlPullParser.END_TAG,null,"precipitation");
        return Pair.create(minPrecipitation, maxPrecipitation);
    }
    private String readSymbol(XmlPullParser parse)throws IOException, XmlPullParserException{
        parse.require(XmlPullParser.START_TAG,null,"symbol");
        String symbol = parse.getAttributeValue(null,"code");
        parse.next();
        parse.require(XmlPullParser.END_TAG,null,"symbol");
        return symbol;
    }
}

