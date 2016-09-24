package com.paya.authomation.connections;

/**
 * Created by Administrator on 07/22/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Dimension;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 06/27/2016.
 */
public class SecondConnection {


    static final String COOKIES_HEADER = "Cookie";
    String uri = "http://pendar.demo.payasaas.ir/folder/index";
    Context context;
    private SharedPreferences cookiePreferences;
    private SharedPreferences.Editor cookiePrefsEditor;
    private List<String> list;

    public String method(Context contex) {


        Response response = null;
        String version = "2.5.2.13";
        URI uri = URI.create("http://pendar.demo.payasaas.ir/Account/SignIn");
        this.context = contex;
        cookiePreferences = context.getSharedPreferences("cookiePrefs", Context.MODE_PRIVATE);
        cookiePreferences.edit();


        Set<String> setCookies = cookiePreferences.getStringSet("cookies", new HashSet<String>());
        list = new ArrayList<String>(setCookies);
        List<HttpCookie> lstCookies = new ArrayList<HttpCookie>();

        for (String st : list) {


            lstCookies.addAll(HttpCookie.parse(st));
            Log.e("list string ", st.toString());
        }


        FormBody.Builder formbuilder = new FormBody.Builder();

        RequestBody formbody = formbuilder.build();


        String PSCM = lstCookies.get(0).toString().substring(6);

        String lo = TextUtils.join(" ; ", lstCookies);


        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("pendar.demo.payasaas.ir")
                .addPathSegment("folder/index")
                .addQueryParameter("sessionid", PSCM)

                .addQueryParameter("storeIndex", "0")
                .addQueryParameter("parentId", "R")
                .build();


        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();



        Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).post(formbody).build();

        Log.e("request", "this " + request.toString());

        try {
            response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                return response.body().string();
            }
            else{
                Toast.makeText(context.getApplicationContext(), "از سرور جوابی دریافت نشد",Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }




    }
    public String Heartbit(Context context){
        Response response = null;
        String version = "2.5.2.13";
        URI uri = URI.create("http://pendar.demo.payasaas.ir/Account/SignIn");
        this.context = context;
        cookiePreferences = context.getSharedPreferences("cookiePrefs", Context.MODE_PRIVATE);
        cookiePreferences.edit();


        Set<String> setCookies = cookiePreferences.getStringSet("cookies", new HashSet<String>());
        list = new ArrayList<String>(setCookies);
        List<HttpCookie> lstCookies = new ArrayList<HttpCookie>();

        for (String st : list) {


            lstCookies.addAll(HttpCookie.parse(st));
            Log.e("list string ", st.toString());
        }


        FormBody.Builder formbuilder = new FormBody.Builder();

        RequestBody formbody = formbuilder.build();


        String PSCM = lstCookies.get(0).toString().substring(6);

        String lo = TextUtils.join(" ; ", lstCookies);


        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("pendar.demo.payasaas.ir")
                .addPathSegment("home/heartbeat")
                .build();


        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();


        Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).post(formbody).build();

        Log.e("request", "this " + request.toString());

        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

}





