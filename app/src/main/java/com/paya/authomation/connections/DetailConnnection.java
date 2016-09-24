package com.paya.authomation.connections;

/**
 * Created by Administrator on 07/22/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import java.util.concurrent.TimeUnit;

import static okhttp3.HttpUrl.*;


/**
 * Created by Administrator on 07/13/2016.
 */
public class DetailConnnection {
    private Context context;
    private String folderId, messageId;
    private SharedPreferences cookiePreferences;
    private List<String> list;

    public String Detailcon(Context context, String folderId, String messageId) {

        Response response = null;
        this.context = context;
        this.folderId = folderId;
        this.messageId = messageId;

        cookiePreferences = context.getSharedPreferences("cookiePrefs", Context.MODE_PRIVATE);
        cookiePreferences.edit();


        Set<String> setCookies = cookiePreferences.getStringSet("cookies", new HashSet<String>());
        list = new ArrayList<String>(setCookies);
        List<HttpCookie> lstCookies = new ArrayList<HttpCookie>();

        for (String st : list) {


            lstCookies.addAll(HttpCookie.parse(st));

        }
        FormBody.Builder formbuilder = new FormBody.Builder();

        RequestBody formbody = formbuilder.build();


        String PSCM = lstCookies.get(0).toString().substring(6);

        String lo = TextUtils.join(" ; ", lstCookies);


        HttpUrl url = new Builder()
                .scheme("http")
                .host("pendar.demo.payasaas.ir")
                .addPathSegment("message/details")

                .addQueryParameter("storeIndex", "0")
                .addQueryParameter("folderId", folderId)
                .addQueryParameter("messageid", messageId)
                .build();


        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();


        Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).post(formbody).build();

        Log.e("request", "this " + request.toString());

        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resp = response.body().string();

                response.body().close();
                return resp;
            } else {
                Toast.makeText(context.getApplicationContext(), "از سرور جوابی دریافت نشد", Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (ConnectException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public OkHttpClient getFile(Context context) {
        cookiePreferences = context.getSharedPreferences("cookiePrefs", Context.MODE_PRIVATE);
        cookiePreferences.edit();


        Set<String> setCookies = cookiePreferences.getStringSet("cookies", new HashSet<String>());
        list = new ArrayList<String>(setCookies);
        List<HttpCookie> lstCookies = new ArrayList<HttpCookie>();

        for (String st : list) {


            lstCookies.addAll(HttpCookie.parse(st));

        }
        FormBody.Builder formbuilder = new FormBody.Builder();

        RequestBody formbody = formbuilder.build();


        String PSCM = lstCookies.get(0).toString().substring(6);

        final String lo = TextUtils.join(" ; ", lstCookies);
        // cookiePreferences = context.getSharedPreferences("listPrefs", Context.MODE_PRIVATE);
        // cookiePreferences.edit();


        // Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).build();
        OkHttpClient client = new OkHttpClient.Builder().cache(new Cache(context.getCacheDir(), Integer.MAX_VALUE))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Cookie", lo.substring(1))
                                .build();
                        Response res = chain.proceed(newRequest);
                        return res.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
                    }
                })
                .build();


        return client;
    }

    public HttpUrl getUrl(String MesgId, String foldId, String attachmentid) {

        final HttpUrl url = new Builder()
                .scheme("http")
                .host("pendar.demo.payasaas.ir")
                .addPathSegment("Attachment/image")

                .addQueryParameter("storeIndex", "0")
                .addQueryParameter("folderId", foldId)
                .addQueryParameter("messageId", MesgId)
                .addQueryParameter("attachmentId", attachmentid)
                .addQueryParameter("maxwidth","200")
                .build();
        Log.e("getfile query string ", foldId + " " + MesgId + " " + attachmentid);

        return url;

    }

    public String archive(Context con, String foldId, String msgId) {

        cookiePreferences = con.getSharedPreferences("cookiePrefs", Context.MODE_PRIVATE);
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


        HttpUrl url = new Builder()
                .scheme("http")
                .host("pendar.demo.payasaas.ir")
                .addPathSegment("Message/Continue")

                .addQueryParameter("storeIndex", "0")
                .addQueryParameter("folderId", foldId)
                .addQueryParameter("messageids[]", msgId)
                .addQueryParameter("inDetails", "false")
                .addQueryParameter("remark", "")
                .addQueryParameter("unique", "true")
                .build();


        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();


        Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).post(formbody).build();

        Log.e("request", "this " + request.toString());

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resp = response.body().string();
                Log.e("response in Archive", resp);
                response.body().close();
                return resp;
            } else {
                Toast.makeText(context.getApplicationContext(), "از سرور جوابی دریافت نشد", Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (ConnectException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUser(Context con, String classId, String accessRight) {
        cookiePreferences = con.getSharedPreferences("cookiePrefs", Context.MODE_PRIVATE);
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


        HttpUrl url = new Builder()
                .scheme("http")
                .host("pendar.demo.payasaas.ir")
                .addPathSegment("User/IndexByWorkGroup")

                .addQueryParameter("storeIndex", "0")
                .addQueryParameter("workGroupId", "null")
                .addQueryParameter("classid", classId)
                .addQueryParameter("accessright", accessRight)
                .build();


        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();


        Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).post(formbody).build();

        Log.e("request", "this " + request.toString());

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resp = response.body().string();
                Log.e("response in Archive", resp);
                response.body().close();
                return resp;
            } else {
                Toast.makeText(context.getApplicationContext(), "از سرور جوابی دریافت نشد", Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (ConnectException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public String getSpinnerErja(Context context) {
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


        HttpUrl url = new Builder()
                .scheme("http")
                .host("pendar.demo.payasaas.ir")
                .addPathSegment("WorkFlowNode")
                .addQueryParameter("storeIndex", "0")
                .build();


        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();


        Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).post(formbody).build();

        Log.e("request", "this " + request.toString());

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resp = response.body().string();
                response.body().close();
                return resp;
            } else {
                Toast.makeText(context.getApplicationContext(), "از سرور جوابی دریافت نشد", Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (ConnectException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String doerja(Context context, String foldid, String mesgid, List<String> usersid, String workflownodetype, String text) {

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
        for (int i = 0; i < usersid.size(); i++) {
            formbuilder.add("recievers["+i+"][IsCopy]", "false")
                    .add("recievers["+i+"][Remark]", text)
                    .add("recievers["+i+"][UserID]", usersid.get(i))
                    .add("recievers["+i+"][WorkFlowNodeTypeID]", workflownodetype)
                    .add("recievers["+i+"][IsPrivate]", "false");

        }
        Log.e("formbuilder ",formbuilder.toString());
        RequestBody formbody = formbuilder.build();


        String PSCM = lstCookies.get(0).toString().substring(6);

        String lo = TextUtils.join(" ; ", lstCookies);



        HttpUrl url = new Builder()
                .scheme("http")
                .host("pendar.demo.payasaas.ir")
                .addPathSegment("message/send")
                .addQueryParameter("storeIndex", "0")
                .addQueryParameter("folderid", foldid)
                .addQueryParameter("messageIds[]", mesgid)
                .build();


        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();


        Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).post(formbody).build();



        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resp = response.body().string();
                Log.e("response", "this " + resp);
                response.body().close();
                return resp;
            } else {
                Toast.makeText(context.getApplicationContext(), "از سرور جوابی دریافت نشد", Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (ConnectException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String confirm(Context context,String folder, String msgid){
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


        HttpUrl url = new Builder()
                .scheme("http")
                .host("pendar.demo.payasaas.ir")
                .addPathSegment("message/confirm")
                .addQueryParameter("storeIndex", "0")
                .addQueryParameter("folderId", folder)
                .addQueryParameter("messageid", msgid)
                .addQueryParameter("letterNumberingTypeId","null")
                .addQueryParameter("resend", "false")
                .addQueryParameter("workGroupId", "null")
                .addQueryParameter("formData", "null")
                .build();


        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();


        Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).post(formbody).build();

        Log.e("request", "this " + request.toString());

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resp = response.body().string();
                response.body().close();
                return resp;
            } else {
                Toast.makeText(context.getApplicationContext(), "از سرور جوابی دریافت نشد", Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (ConnectException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}


