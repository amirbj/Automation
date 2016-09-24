package com.paya.authomation.connections;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.HttpCookie;
import java.sql.Time;
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

/**
 * Created by Administrator on 07/22/2016.
 */


    import android.content.Context;
    import android.content.SharedPreferences;
    import android.text.TextUtils;
    import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
    import java.net.HttpCookie;
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
     * Created by Administrator on 07/08/2016.
     */
    public class FoldersConnection  {

        static final String COOKIES_HEADER = "Cookie";
        Context context;
        private SharedPreferences cookiePreferences;
        private SharedPreferences.Editor cookiePrefsEditor;
        private List<String> list;
        private String id;

        public String connect(Context contex, String id) {


            Response response = null;
            this.context = contex;
            this.id= id;

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
            //  cookiePreferences = context.getSharedPreferences("listPrefs", Context.MODE_PRIVATE);
            //  cookiePreferences.edit();




            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host("pendar.demo.payasaas.ir")
                    .addPathSegment("message/index")

                    .addQueryParameter("storeIndex", "0")
                    .addQueryParameter("folderId", id)
                    .addQueryParameter("unique", "true")
                    .addQueryParameter("pageSize","1000000")
                    .build();


            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();


            Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).post(formbody).build();

            Log.e("request", "this " + request.toString());

            try {
                response = client.newCall(request).execute();
                if(response.isSuccessful()) {
                    String resp = response.body().string();

                    Log.e("response in kartabl", resp);
                    return resp;
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

    }


