package com.paya.authomation.fragments;

/**
 * Created by Administrator on 07/22/2016.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paya.authomation.connections.FoldersConnection;
import com.paya.authomation.main.CustomAdapter;
import com.paya.authomation.main.DividerItemDecoration;
import com.paya.authomation.main.JsonContent;
import com.paya.authomation.main.Letters;
import com.paya.authomation.main.R;

import java.util.List;

import okhttp3.HttpUrl;

/**
 * Created by Administrator on 06/25/2016.
 */
public class HomeFragment extends Fragment {


    String uri = "http://pendar.demo.payasaas.ir/folder/index";
    HttpUrl ur = new HttpUrl.Builder()
            .scheme("http")
            .host("pendar.demo.payasaas.ir")
            .addPathSegment("folder/index").build();
    private List<Letters> lettersList;
    private ProgressDialog progress;
    private RecyclerView recycler;
    private   View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.recycler, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.recycle);
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(llm);
        task mtask = new task();
        mtask.execute();

        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
                        getActivity().finish();
                        return false;
                    } else {

                        removeCurrentFragment();
                        getActivity().getSupportFragmentManager().popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);


                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void removeCurrentFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        Fragment detailfrag = getActivity().getSupportFragmentManager().findFragmentByTag("fragName");


        String fragName = "NONE";

        if (detailfrag != null)
            fragName = detailfrag.getClass().getSimpleName();


        if (detailfrag != null)
            transaction.remove(detailfrag);

        transaction.commit();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        view=null;
        recycler = null;
    }

    private class task extends AsyncTask<Void, String, List<Letters>> {

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "....", "لطفا منتظر بمانید", true);

        }

        @Override
        protected List<Letters> doInBackground(Void... voids) {

            String id = getArguments().getString("id");
            Log.e("in home fragment", id);
            FoldersConnection conn = new FoldersConnection();
            String respon = conn.connect(getActivity(), id);

            if (respon != null) {
                lettersList = JsonContent.kartabl(respon);
                return lettersList;
            } else {
                progress.dismiss();

                return null;
            }

        }

        @Override
        protected void onPostExecute(final List<Letters> s) {

            final String id = getArguments().getString("id");
            CustomAdapter adapter = new CustomAdapter(s, getActivity(), id);
            recycler.setAdapter(adapter);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            recycler.setLayoutManager(llm);

            adapter.setOnItemClickListener(new CustomAdapter.ClickListener() {
                @Override
                public void onItemClick(View view) {
                    Bundle data = new Bundle();
                    int position = recycler.getChildAdapterPosition(view);
                    Letters item = s.get(position);

                    Log.e("item.getMessage() ", item.getMessageId());
                    data.putString("folderid", id);
                    data.putString("messageid", item.getMessageId());

                    Fragment fragment = new DetailFragment();
                    fragment.setArguments(data);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment, "detail").addToBackStack("detail").commit();


                }
            });

            progress.dismiss();
        }
    }
}

