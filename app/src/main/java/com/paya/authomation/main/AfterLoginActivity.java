package com.paya.authomation.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.paya.authomation.connections.SecondConnection;

import java.util.*;

/**
 * Created by Administrator on 07/08/2016.
 */
public class AfterLoginActivity extends AppCompatActivity {
    private static Context context;
    List<Letters> items = new ArrayList<>();
   ArrayList<String> arrtitle = new ArrayList<>();
    ArrayList<String> arrid = new ArrayList<>();
    Map<String, String> map = new HashMap<String, String>();
    private SharedPreferences listPreferences;
    private SharedPreferences.Editor listPrefsEditor;
private  ProgressDialog pro;
    String username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        username =i.getStringExtra("username");
        task mytask = new task();
          mytask.execute();
    }

   public void passdata(){

       Intent in = new Intent(this, MainActivity.class);
       in.putStringArrayListExtra("itemtitle", arrtitle);
       listPreferences = getSharedPreferences("listPrefs", MODE_PRIVATE);
       listPrefsEditor = listPreferences.edit();
       Gson gson= new Gson();

       String value = gson.toJson(arrtitle);
       listPrefsEditor.putString("item",value);


       listPrefsEditor.commit();
       in.putStringArrayListExtra("itemId", arrid);
       Gson gs = new Gson();
       String idvalue=  gs.toJson(arrid);
       listPrefsEditor = listPreferences.edit();
       listPrefsEditor.putString("id",idvalue);

       listPrefsEditor.commit();
       in.putExtra("username", username);
       listPrefsEditor = listPreferences.edit();
       listPrefsEditor.putString("username", username);
       listPrefsEditor.commit();

       startActivity(in);
  }

    private class task extends AsyncTask<Void, String, String> {




        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(Void... voids) {
            SecondConnection con = new SecondConnection();
            String res = con.method(getApplicationContext());


            return res;
        }

        @Override
        protected void onPostExecute(String s) {


            items = JsonContent.getData(s);


                for (Letters g : items) {
                    Log.e("in AfterLoginActivity", g.getTitle());
                    arrtitle.add(g.getTitle());
                    arrid.add(g.getID());


                }
                passdata();

                // pro.dismiss();
            }
        }


}

