package com.pewcode.beam;

import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import static com.pewcode.beam.Globals.ADMIN_NUMBERS;
import static com.pewcode.beam.Globals.VALID_NUMBERS;

public class SplashScreen extends Activity {
  String contacts, keywords;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    new PrefetchData().execute();

  }

  public boolean isOnline() {
    ConnectivityManager connMgr = (ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    return (networkInfo != null && networkInfo.isConnected());
  }

  private class PrefetchData extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      if(!isOnline()) {
        finish();
      }
    }

    @Override
    protected Void doInBackground(Void... arg0) {
      HttpHandler thttp = new HttpHandler();
      String teachers_json = thttp.makeServiceCall(Globals.HOST);

      HttpHandler ahttp = new HttpHandler();
      String admins_json = ahttp.makeServiceCall(Globals.HOST + "/admins.php");

      if(!teachers_json.isEmpty() && !admins_json.isEmpty()) {
        VALID_NUMBERS = new String[teachers_json.length()];
        ADMIN_NUMBERS = new String[admins_json.length()];

        try {
          JSONArray ajson_array = new JSONArray(admins_json);
          for(int index = 0; index < admins_json.length(); index++) {
            ADMIN_NUMBERS[index] = ajson_array.get(index).toString();
          }
        }catch(JSONException e) {
          e.printStackTrace();
        }

        try {
          JSONArray tjson_array = new JSONArray(teachers_json);
          for(int index = 0; index < teachers_json.length(); index++) {
            VALID_NUMBERS[index] = tjson_array.get(index).toString();
          }
        }catch(JSONException e) {
          e.printStackTrace();
        }

        try {
          JSONArray tjson_array = new JSONArray(teachers_json);
          for(int index = 0; index < teachers_json.length(); index++) {
            VALID_NUMBERS[index] = tjson_array.get(index).toString();
          }
        }catch(JSONException e) {
          e.printStackTrace();
        }
        contacts = "Contacts Updated";
        keywords = "Keywords Synced";
      }else{
        finish();
      }

      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      super.onPostExecute(result);
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          Intent i = new Intent(SplashScreen.this, MainActivity.class);
          i.putExtra("contacts", contacts);
          i.putExtra("keywords", keywords);
          startActivity(i);
          finish();
        }
      }, 3000);
    }

  }

}