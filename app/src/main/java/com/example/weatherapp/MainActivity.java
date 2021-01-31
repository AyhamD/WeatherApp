package com.example.weatherapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity {

    private String ver;
    private Button button;
    private TextView temperature;
    private ImageView img;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperature = (TextView) findViewById(R.id.temperature);
        /*windSpeed = (TextView) findViewById(R.id.windSpeed);
        cloudiness = (TextView) findViewById(R.id.cloudiness);
        precipitation = (TextView) findViewById(R.id.precipitation);
*/
        img = (ImageView) findViewById(R.id.imageID);
        button= (Button) findViewById(R.id.refbutton);
        button.setOnClickListener(new buttonclick());
        v  = findViewById(R.id.view);
        v.setBackgroundColor(Color.BLACK);

    }

    private class buttonclick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            try {
                ver = "https://api.met.no/weatherapi/locationforecast/1.9/?lat=60.10;lon=9.58";
                new DownloadFilesTask(ver).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadFilesTask extends AsyncTask<String, Void, String>{
        
        private final String minURL;
        public DownloadFilesTask(final String param){
            minURL = param;
        }

        @Override
        protected String doInBackground(String... strings) {
            URL inputStream;
            HttpURLConnection connection;
            HttpURLConnection.setFollowRedirects(true);


            try {

                inputStream = new URL(minURL);
                connection = (HttpURLConnection) inputStream.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("user-Agent","Mozilla/5.0");

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                StringBuilder sb = new StringBuilder();

                while((line = br.readLine()) != null){
                    sb.append(line);
                    //Log.d(getClass().toString(),line);
                }
                br.close();
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }

        protected void onPostExecute(String result) {
            XmlParser parser = new XmlParser();
            try{

                WeatherForecast WF = parser.parse(new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8)));
                Context content = img.getContext();
                int id = content.getResources().getIdentifier(WF.symbol ,"drawable" ,content.getPackageName());
                img.setImageResource(id);

                temperature.setText(WF.toString());

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

}