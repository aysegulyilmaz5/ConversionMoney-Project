package com.aysegulyilmaz.homework3_5;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtText;
    Button btnConn;
    TextView txtView;


    Country countryI;
    String countryName;

    Country countryTo;
    String countryNameTo;



    private ArrayList<Country> countryArrayList;
    private CountryAdapter cAdapter;
    ArrayList<String> currency;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);

        WebView webView =  findViewById(R.id.myWebView);

        webView.setWebViewClient(new MyWebViewClient() );

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://www.oanda.com/currency-converter/live-exchange-rates/");



        currency = new ArrayList<>();
        currency.add("USD");
        currency.add("TRY");
        currency.add("EUR");
        currency.add("CHF");

        listCountry();
        String [] from =new String[1];
        String [] to = new String[1];
        btnConn = findViewById(R.id.buttoncon);
        edtText = findViewById(R.id.edtText);
        txtView = findViewById(R.id.txtView);

        Spinner spinner = findViewById(R.id.spinner);
        Spinner spinner_2 = findViewById(R.id.spinner_2);

        cAdapter = new CountryAdapter(this,countryArrayList);
        spinner.setAdapter(cAdapter);
        spinner_2.setAdapter(cAdapter);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currency.get(position);
                countryI = (Country) parent.getItemAtPosition(position);
                countryName = countryI.getCountryName();
                from[0]= countryName;
                //Toast.makeText(MainActivity.this,countryName + "Selected" ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currency.get(position);
                countryTo = (Country) parent.getItemAtPosition(position);
                countryNameTo = countryTo.getCountryName();
                to[0] = countryNameTo;
                //Toast.makeText(MainActivity.this,countryNameTo + "Selected" ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnConn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                String amount = edtText.getText().toString();
                String url_str = "https://api.exchangerate.host/convert?from=" + from[0] + "&to=" + to[0] + "&format=xml&amount=" + amount;
                System.out.println(url_str);
                new HTTPAsyncTask().execute(url_str);
            }

        });




    }

    private class HTTPAsyncTask extends AsyncTask<String, Void, String > {

        protected String doInBackground(String... urls) {
            try{
                return HttpGet(urls[0]);
            }catch (IOException e){
                return "Unable to retrieve web page. URL may be invalid";

            }
        }

        @Override
        protected void onPostExecute(String result){

            try{
                XMLParser(result);
            }catch (XmlPullParserException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }

        private void XMLParser(String result) throws XmlPullParserException, IOException{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput ( new StringReader( result ));
            int eventType = xpp.getEventType();
            String Tag = "";
            String NewResult ="";

            while(eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_DOCUMENT){

                }else if (eventType == XmlPullParser.END_DOCUMENT){

                }else if (eventType == XmlPullParser.START_TAG){
                    Tag= xpp.getName();
                }else if (eventType == XmlPullParser.END_TAG){

                }else if (eventType == XmlPullParser.TEXT){
                    if(Tag.equals("result")){
                        NewResult =  xpp.getText();
                        Tag = "";

                    }
                }
                eventType = xpp.next();
            }
            //newresult string to int
            // newresult_int = hjk
            // edit textin değeri to int
            // ikisini çarpıp stringe dönüştürüğ
            txtView.setText(NewResult);

        }

        private String HttpGet (String myUrl) throws IOException{
            InputStream inputStream = null;
            String result = "";
            URL url = new URL(myUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.connect();
            inputStream = conn.getInputStream();

            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work";
            return result;
        }
        private String convertInputStreamToString (InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line ="";
            String result = "";
            while ((line = bufferedReader.readLine()) != null){
                result += line;

            }
            inputStream.close();
            return result;
        }

    }



    private void listCountry() {

        countryArrayList = new ArrayList<>();
        countryArrayList.add(new Country("USD",R.drawable.abd));
        countryArrayList.add(new Country("TRY",R.drawable.turkey));
        countryArrayList.add(new Country("EUR",R.drawable.british));
        countryArrayList.add(new Country("CHF",R.drawable.switzerland));


    }

    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return false;
        }
    }
}