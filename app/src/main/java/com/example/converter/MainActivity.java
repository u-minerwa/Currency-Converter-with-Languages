package com.example.converter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.io.*;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView convertFromDropDownTextView, convertToDropDownTextView, conversionRateText;
    EditText amountToConvert;
    ArrayList<String> arrayList;
    Dialog fromDialog;
    Dialog toDialog;
    Button convButton;
    String convertFromValue, convertToValue, conversionValue;
    String[] country = {"EUR", "USD", "RUB", "BSD", "AUD", "AZN", "ALL", "DZD", "XCD", "AOA", "ARS", "AMD", "AWG", "AFN", "BDT", "BBD", "BHD", "BZD", "BYN", "EUR", "XOF", "BMD", "BGN", "BOB", "USD", "RUB", "BAM", "BWP", "BRL", "BND", "BIF", "BTN", "VUV", "GBP", "HUF", "VES", "VND", "XAF", "HTG", "GYD", "GMD", "GHS", "GTQ", "GNF", "GIP", "HNL", "HKD", "XCD", "DKK", "GEL", "DKK", "DJF", "DOP", "EGP", "ZMW", "MAD", "ZWL", "ILS", "INR", "IDR", "JOD", "IQD", "IRR", "ISK", "YER", "CVE", "KZT", "KYD", "KHR", "XAF", "CAD", "QAR", "KES", "KGS", "AUD", "CNY", "COP", "KMF", "CDF", "KPW", "KRW", "CRC", "CUP", "KWD", "ANG", "LAK", "LSL", "LRD", "LBP", "LYD", "CHF", "MUR", "MRU", "MGA", "MOP", "MKD", "MWK", "MYR", "MVR", "MAD", "MXN", "MZN", "MDL", "MNT", "XCD", "MMK", "NAD", "AUD", "NPR", "NGN", "NIO", "NZD", "XPF", "NOK", "AED", "OMR", "PKR", "PAB", "PGK", "PYG", "PEN", "PLN", "RWF", "RON", "SVC", "BTC", "WST", "STN", "SAR", "SHP", "SCR", "RSD", "SGD", "ANG", "SYP", "SBD", "SOS", "SDG", "SRD", "SLL", "TJS", "THB", "TWD", "TZS", "TOP", "TTD", "TND", "TMT", "TRY", "UGX", "UZS", "UAH", "XPF", "UYU", "DKK", "FJD", "PHP", "FKP", "CZK", "CLP", "SEK", "LKR", "NOK", "ERN", "SZL", "ETB", "ZAR", "SSP", "JMD", "JPY", "EUR", "RUB", "USD"}; //TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convertFromDropDownTextView = findViewById(R.id.convert_from_dropdown_menu);
        convertToDropDownTextView = findViewById(R.id.convert_to_dropdown_menu);
        convButton = findViewById(R.id.conversionButton);
        conversionRateText = findViewById(R.id.convertionRateText);
        amountToConvert = findViewById(R.id.amountToConvertValueEditText);

        //androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1);
        //setSupportActionBar(toolbar);

        arrayList = new ArrayList<>();
        for (String i: country){
            arrayList.add(i);
        }
        convertFromDropDownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDialog = new Dialog(MainActivity.this);
                fromDialog.setContentView(R.layout.from_spinner);
                fromDialog.getWindow().setLayout(650, 800);
                fromDialog.show();

                EditText editText = fromDialog.findViewById(R.id.edit_text);
                ListView listView = fromDialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                        convertFromDropDownTextView.setText(adapter.getItem(i));
                        fromDialog.dismiss();
                        convertFromValue = adapter.getItem(i);
                    }
                });
            }
        });

        convertToDropDownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDialog = new Dialog(MainActivity.this);
                toDialog.setContentView(R.layout.to_spinner);
                toDialog.getWindow().setLayout(650, 800);
                toDialog.show();

                EditText editText = toDialog.findViewById(R.id.edit_text);
                ListView listView = toDialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        convertToDropDownTextView.setText(adapter.getItem(i));
                        toDialog.dismiss();
                        convertToValue = adapter.getItem(i);
                    }
                });
            }
        });

        convButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double amountToConvert = Double.valueOf(MainActivity.this.amountToConvert.getText().toString());
                    getConversionRate(convertFromValue, convertToValue, amountToConvert);
                } catch (Exception e){

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, dm);

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.rus1:
                //setLocale(MainActivity.this, "ru");
                setAppLocale("ru");
                return true;
            case R.id.eng1:
                //setLocale(MainActivity.this, "en");
                setAppLocale("en");
                return true;
            case R.id.germ1:
                //setLocale(MainActivity.this, "de");
                setAppLocale("de");
                return true;
            case R.id.ukr1:
                //setLocale(MainActivity.this, "uk");
                setAppLocale("uk");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    public static void apii(){
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api.apilayer.com/currency_data/convert?to=to&from=from&amount=amount")
                .addHeader("apikey", "k1AauVSVEWphsf2FfJHUJwOdkiywdIYj")
                .method("GET", })
            .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
     */

    public String getConversionRate(String convFrom, String convTo, Double amountToConv){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://free.currconv.com/api/v7/convert?q="+convFrom+"_"+convTo+"&compact=ultra&apiKey=4b1610bebbbf82beb440";
        //String url = "https://api.apilayer.com/currency_data/convert?to="+convTo+"&from="+convFrom+"&amount="+amountToConv;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Double convRateValue = round(((Double) jsonObject.get(convFrom+"_"+convTo)), 2);
                    conversionValue = "" + round((convRateValue * amountToConv), 2);
                    conversionRateText.setText(conversionValue);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
        return null;
    }

    public static double round(double value, int places){
        if(places<0) throw new IllegalArgumentException();
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}

