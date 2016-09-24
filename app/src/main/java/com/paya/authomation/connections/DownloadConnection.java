package com.paya.authomation.connections;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 07/26/2016.
 */
public class DownloadConnection {
    private SharedPreferences cookiePreferences;
    private List<String> list;

    public Map<Long, File> download(Context context, String folderId, String messageId, String attachmentID, String filename, String filepath){

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
                .addPathSegment("Attachment/get")

                .addQueryParameter("storeIndex", "0")
                .addQueryParameter("folderId", folderId)
                .addQueryParameter("messageid", messageId)
                .addQueryParameter("attachmentId", attachmentID)
                .build();


        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();


        Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).build();

        try {
            Response res = client.newCall(request).execute();
            InputStream is = res.body().byteStream();
            long length =res.body().contentLength();
            BufferedInputStream bf = new BufferedInputStream(is);
            String f = Environment.getExternalStorageDirectory().getPath().toString();


           // intent.setType("video/, images/");


            File fileroot = new File(f + "/paya/"+filepath);
            if(!fileroot.exists()) {
                fileroot.mkdir();
            }

           //File file = new File(fileroot, "/"+filename);
            File file = new File(fileroot,filename);

           try {
               OutputStream output = new FileOutputStream(file);

               byte[] data = new byte[1024];

               long total = 0;
               int count;

               while ((count = bf.read(data)) != -1) {
                   total += count;
                   output.write(data, 0, count);
               }
               output.flush();
               output.close();
               is.close();

               Map<Long, File> map = new HashMap<>();
               map.put(length, fileroot);
               return map;
           }
           catch(FileNotFoundException e1){
               e1.printStackTrace();
               return null;
           }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

}
