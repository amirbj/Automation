package com.paya.authomation.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.paya.authomation.connections.DetailConnnection;
import com.paya.authomation.connections.DownloadConnection;
import com.paya.authomation.connections.ImageDownloadConnection;
import com.paya.authomation.main.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 07/22/2016.
 */
public class ImageFragment extends Fragment {

    ProgressDialog dialog;
    private View view;

    ImageView img;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.imagedetail, container, false);
        img = (ImageView) view.findViewById(R.id.image);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        String filename = getArguments().getString("filename");
        String f = Environment.getExternalStorageDirectory().getPath().toString();
        File file = new File(f + "/paya/Image", filename);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "image/jpeg");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                getActivity().startActivity(intent);
            } catch (ActivityNotFoundException e1) {
                e1.printStackTrace();
                getActivity().finish();
            }
        } else {
            DownloadImage task = new DownloadImage(getActivity());
            task.execute();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
    }

    public class DownloadImage extends AsyncTask<Void, Long, Void> {

        private Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("لطفا منتظر بمانید...");
            dialog.setTitle("دانلود فایل");
            dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            dialog.setMax(100);
            dialog.show();
        }

        public DownloadImage(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String folderid = getArguments().getString("folderid");
            String messageid = getArguments().getString("messageid");
            String attachmentid = getArguments().getString("attachmentid");
            String filename = getArguments().getString("filename");
            long downloaded = 0;
            ImageDownloadConnection con = new ImageDownloadConnection();
            Map<InputStream, File> map = new HashMap<>();
            map = con.download(getActivity(), folderid, messageid, attachmentid, filename, "Image", "image");
            for (Map.Entry<InputStream, File> entry : map.entrySet()) {
                byte[] buff = new byte[1024 * 4];


                int count = 0;
                OutputStream output = null;
                try {
                    output = new FileOutputStream(entry.getValue());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    while ((count = entry.getKey().read(buff)) != -1) {


                    //write buff
                    downloaded += count;
                    output.write(buff, 0, count);
                    publishProgress(downloaded);

                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("download length ", String.valueOf(downloaded));

                try {
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (entry.getKey() != null)
                        try {
                            entry.getKey().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                }
            }


            return null;


        }

        @Override
        protected void onProgressUpdate(Long... values) {
            dialog.setProgress(values[0].intValue());



            }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            Toast.makeText(getActivity(), "دانلود انجام شد", Toast.LENGTH_SHORT).show();
            String f = Environment.getExternalStorageDirectory().toString();
            String filename = getArguments().getString("filename");
            String path = f + "/paya/Image/";
            File file = new File(path, filename);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "image/jpeg");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                getActivity().startActivity(intent);
            } catch (ActivityNotFoundException e1) {
                Toast.makeText(getActivity(), "هیچ اپی برای باز کردن فایل یافت نشد",Toast.LENGTH_SHORT).show();
                e1.printStackTrace();
                FragmentManager manager = getFragmentManager();
                manager.popBackStack();
            }
            FragmentManager manager = getFragmentManager();
            manager.popBackStack();
        }
    }


        private void setPic(String imagePath, ImageView destination) {
            int targetW = destination.getWidth();
            int targetH = destination.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
            destination.setImageBitmap(bitmap);
        }

    }

