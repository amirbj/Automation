package com.paya.authomation.fragments;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.paya.authomation.Cache.Detailletter;
import com.paya.authomation.Cache.ListString;
import com.paya.authomation.connections.DetailConnnection;
import com.paya.authomation.main.DetailLetters;
import com.paya.authomation.main.ErjaAdapter;
import com.paya.authomation.main.JsonContent;
import com.paya.authomation.main.R;
import com.paya.authomation.main.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 08/02/2016.
 */
public class ErjaFragment extends Fragment implements View.OnClickListener {
    // static LruCache<String, List<DetailLetters>> cache;
    static List<String> useridlist = new ArrayList<String>();
    static String matn;
    static String spinData;
    static String spinItem;
    ProgressDialog progress;
    GridView gridView;
    Spinner spin;
    Button button;
    EditText matnTxt;
    EditText searchtxt;
    ImageView imgcheck;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.erja, container, false);
        gridView = (GridView) view.findViewById(R.id.usergrid);
        matnTxt = (EditText) view.findViewById(R.id.matn);
        searchtxt = (EditText) view.findViewById(R.id.search_box);
        spin = (Spinner) view.findViewById(R.id.spinner);
        imgcheck = (ImageView) view.findViewById(R.id.checkimg);


        button = (Button) view.findViewById(R.id.send);
        button.setOnClickListener(this);


        SpinnerData data = new SpinnerData();
        data.execute();
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        //int maxmemory =  am.getMemoryClass()*1024*1024;
        //int memory = maxmemory;
        //cache = new LruCache<>(memory);
        if (Detailletter.get().get("classid") == null) {
            GetClassID getClassID = new GetClassID();
            getClassID.execute();
        }


        getUser usertask = new getUser();
        usertask.execute();

        return view;
    }

    @Override
    public void onClick(View view) {

        spinItem = spin.getSelectedItem().toString();
        Map<String, String> map = ListString.get().get("Spinmap");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (spinItem == entry.getValue()) {
                spinData = entry.getKey();
            }
        }
        matn = matnTxt.getText().toString();
        Doerja erja = new Doerja();
        erja.execute();

    }


    public class getUser extends AsyncTask<Void, String, ArrayList<User>> {

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "....", "لطفا منتظر بمانید", true);
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            ArrayList<User> userlist = new ArrayList<>();

            List<DetailLetters> list = Detailletter.get().get("classid");
            if (Detailletter.get().get("classid") != null) {
                Log.e("cache ", list.toString());
                DetailLetters leterobj = list.get(0);
                DetailConnnection conn = new DetailConnnection();
                String userResponse = conn.getUser(getActivity(), leterobj.getClassId(), leterobj.getAccessRight());

                userlist = JsonContent.getuser(userResponse);
                return userlist;
            } else {
                DetailConnnection conn = new DetailConnnection();
                String folderid = getArguments().getString("folderid");
                String messageid = getArguments().getString("msgid");
                String respon = conn.Detailcon(getActivity(), folderid, messageid);
                List<DetailLetters> list2 = new ArrayList<>();
                list2 = JsonContent.usercontent(respon);


                DetailLetters leterobj = list2.get(0);
                DetailConnnection connect = new DetailConnnection();
                String userResponse = connect.getUser(getActivity(), leterobj.getClassId(), leterobj.getAccessRight());
                userlist = JsonContent.getuser(userResponse);
                return userlist;

            }


        }

        @Override
        protected void onPostExecute(final ArrayList<User> users) {


            final ErjaAdapter adapter = new ErjaAdapter(getActivity(), R.layout.erja, users);
            gridView.setAdapter(adapter);
            searchtxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    adapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

           gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


               @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                ArrayList  selectedStrinings = new ArrayList();
                   selectedStrinings.add(gridView.getItemAtPosition(position));

                   users.get(position).setStat(1);
                    adapter.notifyDataSetChanged();

                    if(gridView.isItemChecked(position))
                   {
                        users.get(position).setStat(0);
                    }




                    for(int i=0 ; i<selectedStrinings.size();i++){
                        User user = users.get(position);
                        Log.e("usertitle ", user.getUserTitle()+""+user.getUserId());
                        if(user.getStat()==1) {
                            useridlist.add(user.getUserId());
                        }
                        if(user.getStat()==0){
                            useridlist.remove(user.getUserId());
                        }
                    }
                }
            });

            Log.e("items ", String.valueOf(gridView.getSelectedItemId()));











            progress.dismiss();
            useridlist.clear();
        }

    }

        public class SpinnerData extends AsyncTask<Void, String, Map<String, String>> {


            @Override
            protected Map<String, String> doInBackground(Void... voids) {
                DetailConnnection con = new DetailConnnection();
                String response = con.getSpinnerErja(getActivity());
                List<String> list = new ArrayList<>();
                Map<String, String> map;
                map = JsonContent.getSpinnerData(response);


                return map;
            }

            @Override
            protected void onPostExecute(Map<String, String> strings) {
                List<String> spinlist = new ArrayList<>();
                for (Map.Entry<String, String> entry : strings.entrySet()) {
                    spinlist.add(entry.getValue());
                }
                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, spinlist);
                spin.setAdapter(adapter);
                ListString.get().put("Spinmap", strings);


            }


        }

        public class GetClassID extends AsyncTask<Void, Void, List<DetailLetters>> {

            @Override
            protected List<DetailLetters> doInBackground(Void... voids) {
                DetailConnnection conn = new DetailConnnection();
                String folderid = getArguments().getString("folderid");
                String messageid = getArguments().getString("msgid");
                String respon = conn.Detailcon(getActivity(), folderid, messageid);
                List<DetailLetters> list = new ArrayList<>();
                list = JsonContent.usercontent(respon);

                return list;
            }

            @Override
            protected void onPostExecute(List<DetailLetters> detailLetterses) {

                // cache = new LruCache<>(memory);
                Detailletter.get().put("classid", detailLetterses);

            }
        }


        public class Doerja extends AsyncTask<Void, Void, Boolean> {

            @Override
            protected void onPreExecute() {
                progress = ProgressDialog.show(getActivity(), "....", "لطفا منتظر بمانید", true);
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                DetailConnnection con = new DetailConnnection();
                String folderid = getArguments().getString("folderid");
                String messageid = getArguments().getString("msgid");


                String response = con.doerja(getContext(), folderid, messageid, useridlist, spinData, matn);
                Boolean suceed = JsonContent.doErja(response);
return suceed;
        }

@Override
protected void onPostExecute(Boolean succeed) {
        progress.dismiss();
        if(succeed){
        Toast.makeText(getContext(),"ارجاع به کاربرها با موفقیت انجام گردید",Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }
        else{
        Toast.makeText(getContext(),"عملیات ارجاع ناموفق بود",Toast.LENGTH_SHORT).show();

        }
        }
        }
        }



