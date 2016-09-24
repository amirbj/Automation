package com.paya.authomation.fragments;

/**
 * Created by Administrator on 07/22/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.paya.authomation.connections.DetailConnnection;
import com.paya.authomation.main.Attachment;
import com.paya.authomation.main.DetailLetters;
import com.paya.authomation.main.JsonContent;
import com.paya.authomation.main.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 07/13/2016.
 */
public class DetailFragment extends Fragment {

    private ProgressDialog progress;
    private Map<DetailLetters, ArrayList<Attachment>> map = new HashMap<>();

    private TextView createdby, subject, sender, body;
    private ImageView img;
    private LinearLayout layout;
    private WebView web;
    private  Fragment frag;
    private Toolbar toolbar;
    private String html;
    private  View view;
     static boolean canConfirm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.detail, container, false);
        setHasOptionsMenu(true);

        createdby = (TextView) view.findViewById(R.id.createdby);
        subject = (TextView) view.findViewById(R.id.subject);
        sender = (TextView) view.findViewById(R.id.sender);

        layout = (LinearLayout) view.findViewById(R.id.linearImg);
        img = (ImageView) view.findViewById(R.id.imdetail);
        web = (WebView) view.findViewById(R.id.webview);



        task mytask = new task();
        mytask.execute();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.sign);
        if(!canConfirm) {
            item.setVisible(false);
            getActivity().invalidateOptionsMenu();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();

        if(id==R.id.icon){
            Archive archive = new Archive();
            archive.execute();
        }


        if(id==R.id.share){
           ErjaFragment fragment = new ErjaFragment();
            FragmentManager manager = getFragmentManager();
          //Fragment fragment =  getFragmentManager().findFragmentById(R.id.erjafrag);
            final String folderid = getArguments().getString("folderid");
            final String messageid = getArguments().getString("messageid");
            Bundle bundle = new Bundle();
            bundle.putString( "folderid", folderid);
            bundle.putString("msgid", messageid);
            fragment.setArguments(bundle);
            manager.beginTransaction().setCustomAnimations(R.anim.anim_up,0).add(R.id.container,fragment, "anim").addToBackStack("anim").commit();


        }
        if(id==R.id.sign){
            ConfirmTask task = new ConfirmTask();
            task.execute();
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view =null;
    }

    public class ConfirmTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "....", "لطفا منتظر بمانید", true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            DetailConnnection con = new DetailConnnection();
            final String folderid = getArguments().getString("folderid");
            final String messageid = getArguments().getString("messageid");
            String response = con.confirm(getContext(),folderid, messageid);
            boolean succeed = JsonContent.confirm(response);

            return succeed;
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            progress.dismiss();
            if(succeed){

                Toast.makeText(getActivity(),"نامه با موفقیت تایید گردید",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(), "عملیات تایید ناموفق بود",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Archive extends AsyncTask<Void, Void, String>{
    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(getActivity(), "....", "لطفا منتظر بمانید", true);
    }

    @Override
    protected String doInBackground(Void... voids) {
       final String folderid = getArguments().getString("folderid");
       final String messageid = getArguments().getString("messageid");
        DetailConnnection con = new DetailConnnection();
        String response = con.archive(getActivity(),folderid, messageid);

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
       boolean success= JsonContent.archive(s);
        if(success){
            Toast.makeText(getActivity(), "نامه با موفقیت به لیست آرشیو اضافه گردید",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "عملیات نا موفق",Toast.LENGTH_SHORT).show();
        }
        progress.dismiss();


    }
}

    private class task extends AsyncTask<Void, String, Map<DetailLetters, ArrayList<Attachment>>> {
        public Target target;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "....", "لطفا منتظر بمانید", true);

        }

        @Override
        protected Map<DetailLetters, ArrayList<Attachment>> doInBackground(Void... voids) {

            String folderid = getArguments().getString("folderid");
            String messageid = getArguments().getString("messageid");

            Log.e(" homefragment folderid ", folderid);
            Log.e(" homefragment msgid ", messageid);
            DetailConnnection conn = new DetailConnnection();

            String respon = conn.Detailcon(getActivity(), folderid, messageid);


            map = JsonContent.detail(respon);

            return map;
        }

        @Override
        protected void onPostExecute(Map<DetailLetters, ArrayList<Attachment>> s) {
            final String folderid = getArguments().getString("folderid");
            final String messageid = getArguments().getString("messageid");
            for (final Map.Entry<DetailLetters, ArrayList<Attachment>> entry : s.entrySet()) {


                Log.e("file mimetype ", entry.getKey().getCreatedBy() + entry.getKey().getSubject() + entry.getKey().getSender());
                createdby.setText(entry.getKey().getCreatedBy());
                subject.setText(entry.getKey().getSubject());
                sender.setText(entry.getKey().getSender());
                canConfirm = entry.getKey().isCanconfirm();

                try {
                    if(entry.getKey().getBody().startsWith("%")) {
                      html = URLDecoder.decode(entry.getKey().getBody(), "UTF-8");
                    }

                    else{
                        html = entry.getKey().getBody();
                    }
                    web.loadData(html, "text/html; charset=utf-8", "UTF-8");
                    web.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          Bundle data = new Bundle();
                            data.putString(html, "html");
                            WebviewFragment fragment = new WebviewFragment();
                            fragment.setArguments(data);
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction().replace(R.id.container, fragment, "web").addToBackStack("web").commit();
                        }
                    });

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }



                if (entry.getKey().isHasAttachment()) {


                    for (final Attachment attach : entry.getValue())
                        if (attach.getMimeType().startsWith("image")) {
                            Log.e("mimetype ", attach.getMimeType());
                            final DetailConnnection conn = new DetailConnnection();
                            OkHttpClient client = conn.getFile(getActivity());

                            final Picasso.Builder picasso = new Picasso.Builder(getActivity());
                            picasso.downloader(new OkHttp3Downloader(client));
                            Picasso built = picasso.build();
                            built.setIndicatorsEnabled(true);


                            final ImageView imgview = new ImageView(getActivity());
                            imgview.setAdjustViewBounds(true);
                            imgview.setPadding(5, 5, 5, 5);
                            imgview.setBackgroundColor(Color.BLACK);


                            imgview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String folderid = getArguments().getString("folderid");
                                    final String messageid = getArguments().getString("messageid");

                                    Bundle data = new Bundle();
                                    data.putString("folderid", folderid);
                                    data.putString("messageid", messageid);
                                    data.putString("attachmentid", attach.getAttachmentId());
                                    data.putString("filename", attach.getFileName());
                                    ImageFragment fragment = new ImageFragment();
                                    fragment.setArguments(data);
                                    FragmentManager manager = getFragmentManager();
                                    manager.beginTransaction().replace(R.id.container, fragment, "image").addToBackStack("image").commit();

                                }
                            });
                            layout.addView(imgview);
                            target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    //  BlurTransform blur = new BlurTransform(getActivity());
                                    // Bitmap bit =blur.transform(bitmap);
                                    imgview.setImageBitmap(bitmap);
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    imgview.setImageDrawable(placeHolderDrawable);

                                }
                            };

                            DetailConnnection connection = new DetailConnnection();

                            built.load(String.valueOf(connection.getUrl(messageid, folderid, attach.getAttachmentId()))).placeholder(R.drawable.placeholder)
                                    .into(target);

                        }

                    else{

                            final TextView textView = new TextView(getActivity());
                            textView.setText(attach.getFileName());
                            textView.setTextColor(Color.BLUE);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String folderid = getArguments().getString("folderid");
                                    final String messageid = getArguments().getString("messageid");

                                    Bundle data = new Bundle();
                                    data.putString("folderid", folderid);
                                    data.putString("messageid", messageid);
                                    data.putString("attachmentid", attach.getAttachmentId());
                                    data.putString("filename", attach.getFileName());
                                    data.putString("mimetype",attach.getMimeType());
                                    FileFragment fragment = new FileFragment();
                                    fragment.setArguments(data);
                                    FragmentManager manager = getFragmentManager();
                                    manager.beginTransaction().replace(R.id.container, fragment, "file").addToBackStack("file").commit();



                                }
                            });
                            layout.addView(textView);
                        }
                }


                progress.dismiss();

            }


        }


        public class BlurTransform implements Transformation {

            RenderScript rs;

            public BlurTransform(Context context) {
                super();
                rs = RenderScript.create(context);
            }

            @Override
            public Bitmap transform(Bitmap bitmap) {
                //  Bitmap blurredBitmap = Bitmap.createBitmap(bitmap);

                // Allocate memory for Renderscript to work with
                Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
                Allocation output = Allocation.createTyped(rs, input.getType());

                // Load up an instance of the specific script that we want to use.
                ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                script.setInput(input);

                // Set the blur radius
                script.setRadius(10);

                // Start the ScriptIntrinisicBlur
                script.forEach(output);

                // Copy the output to the blurred bitmap
                output.copyTo(bitmap);

                return bitmap;
            }

            @Override
            public String key() {
                return "blur";
            }
        }



    }
}




