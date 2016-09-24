package com.paya.authomation.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import com.paya.authomation.connections.DownloadConnection;
import com.paya.authomation.connections.ImageDownloadConnection;
import com.paya.authomation.main.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 07/26/2016.
 */
public class FileFragment extends Fragment  {
    ImageView imgview, imgStat;
    ProgressDialog progressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.imagedetail, container, false);

        imgview = (ImageView) view.findViewById(R.id.image);
        imgStat = (ImageView) view.findViewById(R.id.stat);

        imgStat.setImageResource(R.drawable.stat_sys_download_anim0);
        String filename = getArguments().getString("filename");
        String mime = getArguments().getString("mimetype");
        String f = Environment.getExternalStorageDirectory().getPath().toString();
        File file = new File(f + "/paya/File", filename);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), mime);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                getActivity().startActivity(intent);
            } catch (ActivityNotFoundException e1) {
                Toast.makeText(getActivity(), "هیچ اپی برای باز کردن فایل یافت نشد",Toast.LENGTH_SHORT).show();
                FragmentManager manager = getFragmentManager();
                manager.popBackStack();
                e1.printStackTrace();
                getActivity().finish();
            }
        }
            else{
                DownloadTask task = new DownloadTask(getActivity());
                task.execute();

            }

        return view;
    }




    public class  DownloadTask extends AsyncTask<Void,Long, Map<InputStream, File>> {

        private Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar= new ProgressDialog(getActivity());
            progressBar.setMessage("لطفا منتظر بمانید...");
            progressBar.setTitle("دانلود فایل");
            progressBar.setIndeterminate(false);
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            progressBar.setMax(100);
           progressBar.show();
        }

        public DownloadTask(Context context) {
            this.context = context;
        }
        @Override
        protected Map<InputStream, File> doInBackground(Void... voids) {
            String folderid = getArguments().getString("folderid");
            String messageid = getArguments().getString("messageid");
            String attachmentid = getArguments().getString("attachmentid");
            String filename = getArguments().getString("filename");
            long downloaded = 0;
            ImageDownloadConnection con = new ImageDownloadConnection();
            Map<InputStream, File> map = new HashMap<>();
            map = con.download(getActivity(), folderid, messageid, attachmentid, filename, "File","get");
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


            return map;
        }
        @Override
        protected void onProgressUpdate(Long... values) {
           progressBar.setProgress(values[0].intValue());



        }

            @Override
        protected void onPostExecute(Map<InputStream, File> s) {
                super.onPostExecute(s);

                for (final Map.Entry<InputStream, File> entry : s.entrySet()) {


                    progressBar.dismiss();
                    Handler handlerTimer = new Handler();
                    handlerTimer.postDelayed(new Runnable(){
                        public void run() {
                            if (entry.getValue().exists()) {

                                Toast.makeText(getActivity(), "دانلود انجام شد", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                String mime = getArguments().getString("mimetype");
                                intent.setDataAndType(Uri.fromFile(entry.getValue()), mime);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                try {
                                    getActivity().startActivity(intent);
                                } catch (ActivityNotFoundException e1) {
                                    Toast.makeText(getActivity(), "هیچ اپی برای باز کردن فایل یافت نشد", Toast.LENGTH_SHORT).show();
                                    FragmentManager manager = getFragmentManager();
                                    manager.popBackStack();
                                    e1.printStackTrace();
                                }
                            }
                            FragmentManager manager = getFragmentManager();
                            manager.popBackStack();
                        }}, 2000);

                }
            }

}


        }

