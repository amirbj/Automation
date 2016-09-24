package com.paya.authomation.connections;

/**
 * Created by Administrator on 07/22/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 06/19/2016.
 */
public class FirstConnection {

    static final String COOKIES_HEADER = "Set-Cookie";
    private static final String STATUS = "Status";
    private static final String DATA = "Data";
    String uri = "http://pendar.demo.payasaas.ir/Account/SignIn";


    int rescode;
    CookieManager cookimng = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    String version = "2.5.2.13";
    HttpURLConnection con = null;
    Context context;
    List<Cookie> cookieHeader;
    private SharedPreferences cookiePreferences;
    private SharedPreferences.Editor cookiePrefsEditor;
    private Response response;

    public String getContent(String userName, String password, Context context) throws IOException {

        this.context = context;
        //  Map<String, String> params = new LinkedHashMap<>();

        // params.put("userName", userName);
        //  params.put("password", password);
        //  params.put("version", version);
        //  int dataLength = params.size();
        String res;


        try {

            CookieManager cookieManager = new CookieManager();
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")

                    .host("pendar.demo.payasaas.ir")
                    .addPathSegment("Account/SignIn")
                    .addQueryParameter("userName", userName)

                    .addQueryParameter("password", password)
                            .addQueryParameter("version", version)
                    // .addQueryParameter("version", version)
                    .build();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();


            FormBody.Builder formbuilder = new FormBody.Builder();
            RequestBody formbody = formbuilder.build();
            Request request = new Request.Builder().url(url).post(formbody).build();

            response = client.newCall(request).execute();
            Log.e("response", response.toString());
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            if(response.isSuccessful()) {
                res = response.body().string();
                storeCookie();
                Log.e("response in body ", res);
                return res;
            }
            else{

                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void storeCookie() {
        URI url = URI.create("http://pendar.demo.payasaas.ir/Account/SignIn");
        HttpUrl u = HttpUrl.get(url);
        Headers heade = response.headers();

        cookieHeader = Cookie.parseAll(u, heade);
        HashSet<String> setCookies = new HashSet<String>();
        setCookies.add(cookieHeader.toString());

        cookiePreferences = context.getSharedPreferences("cookiePrefs", Context.MODE_PRIVATE);
        cookiePrefsEditor = cookiePreferences.edit();
        cookiePrefsEditor.putStringSet("cookies", setCookies);

        cookiePrefsEditor.commit();


    }



    public Boolean Authenticate(String res) {


        JSONObject status, data;
        try {
            JSONObject Jobg = new JSONObject(res);
            status = Jobg.getJSONObject(STATUS);
            data = Jobg.getJSONObject(DATA);
            Boolean succeed = status.optBoolean("Succeed");
            Boolean islogged = status.optBoolean("IsLoggedIn");
            boolean succeed2 = data.optBoolean("Succeed");

            return succeed && succeed2;

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


    }


}


