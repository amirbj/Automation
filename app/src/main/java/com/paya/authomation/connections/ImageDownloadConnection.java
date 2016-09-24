package com.paya.authomation.connections;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
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
 * Created by Administrator on 07/30/2016.
 */
public class ImageDownloadConnection {
    private SharedPreferences cookiePreferences;
    private List<String> list;

    public Map<InputStream, File> download(Context context, String folderId, String messageId, String attachmentID, String filename, String filepath, String path) {

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
                .addPathSegment("Attachment/"+path)

                .addQueryParameter("storeIndex", "0")
                .addQueryParameter("folderId", folderId)
                .addQueryParameter("messageid", messageId)
                .addQueryParameter("attachmentId", attachmentID)
                .build();


        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();

        Request request = new Request.Builder().addHeader("Cookie", lo.substring(1)).url(url).build();
        Log.e("request in image ", request.toString());

        Response res = null;
        try {

            String f = Environment.getExternalStorageDirectory().toString();


            File fileroot = new File(f + "/paya/" + filepath + "/");

            fileroot.mkdirs();


            File file = new File(fileroot, filename);



            res = client.newCall(request).execute();
            InputStream is = res.body().byteStream();


          //  byte[] buff = new byte[1024 * 4];
          //  long downloaded = 0;
           // long target = res.body().contentLength();
           // int count=0;
           // while ((count =is.read(buff))!=-1) {


                //write buff
             //   downloaded += count;
            //    output.write(buff,0,count);

         //   }
              //  Log.e("download length ", String.valueOf(downloaded));
            //  BufferedInputStream bf = new BufferedInputStream(is);
             //   BitmapFactory.Options o = new BitmapFactory.Options();
             //  o.inJustDecodeBounds = true;

             //  Bitmap bit = BitmapFactory.decodeStream(iss);

            //   bit.compress(Bitmap.CompressFormat.JPEG, 90, output);
               // output.flush();
             //   output.close();
             //   is.close();

               Map<InputStream, File> map = new HashMap<>();
            map.put(is, file);
              return map;


            } catch (FileNotFoundException e) {
            e.printStackTrace();
            return  null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }




    public static Bitmap decodeSampledBitmapFromResource(InputStream in,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(in, null, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    private Bitmap decodeFile(InputStream in) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(in, null, o2);
        } catch (Exception e) {}
        return null;
    }
}
