package com.example.mohitagarwal.weatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView resultTextview;

    public void findingtheweather(View view){

        Log.i("City Name",cityName.getText().toString());

        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        try {

            String encodedCItyName= URLEncoder.encode(cityName.getText().toString(),"UTF-8");
            DownloadTask task=new DownloadTask();
            task.execute(String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=3ae37994928c7e0f3dcc3f88cb4e9477", encodedCItyName));
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Could not find weather ",Toast.LENGTH_LONG);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName=(EditText)findViewById(R.id.cityName);
        resultTextview=(TextView)findViewById(R.id.textView2);

    }

    public class DownloadTask extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... strings) {

            String result="";
            URL url= null;
            HttpURLConnection urlConnection=null;
            try {

                url = new URL(strings[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream);
                int data=reader.read();
                while (data!=-1){

                    char current=(char) data;
                    result +=current;
                    data = reader.read();
                }
                return  result;

            } catch (Exception e) {

                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Could not find weather ",Toast.LENGTH_LONG);

            }

            return null;
        }


        protected void OnPostExecute(String result){

            super.onPostExecute(result);

            try {

                String message="";
                JSONObject jsonObject=new JSONObject(result);
                String WeatherInfo = jsonObject.getString("weather");
                Log.i("Website Content",WeatherInfo);
                JSONArray array=new JSONArray(WeatherInfo);
                for (int i=0;i<array.length();i++){

                    JSONObject jsonpart=array.getJSONObject(i);
                    String main="";
                    String description="";
                    Log.i("main", jsonpart.getString("main"));

                    Log.i("description", jsonpart.optString("description"));
                    main=jsonpart.getString("main");
                    description=jsonpart.optString("description");
                    if(main != "" && description != ""){

                        message += main +":"+description+"\r\n";

                    }
                }
                if (message !=""){

                    resultTextview.setText(message);
                }else {

                  // Toast.makeText(getApplicationContext(),"Could not find weather ",Toast.LENGTH_LONG);
                }

            } catch (JSONException e) {

                e.printStackTrace();

            }


        }
    }
}
