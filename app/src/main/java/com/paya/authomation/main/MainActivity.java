package com.paya.authomation.main;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;


import com.google.gson.Gson;
import com.paya.authomation.connections.SecondConnection;
import com.paya.authomation.fragments.DetailFragment;
import com.paya.authomation.fragments.HomeFragment;
import com.paya.authomation.fragments.NavigationDrawerFragment;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 06/24/2016.
 */
public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    RecyclerView recycle;
    String navTitles[];
    TypedArray navIcons;
    private static Toolbar toolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private SharedPreferences listPreferences;
    private SharedPreferences.Editor listPrefsEditor;
    String Username;
    static ArrayList<String> array = new ArrayList<>();
    static  ArrayList<String> arrayid = new ArrayList<>();
    private List<WeakReference<Fragment>> mFragments = new ArrayList<WeakReference<Fragment>>();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        //toolbar.setTitle(R.string.toobar_title);
        Intent i = getIntent();
        array = i.getStringArrayListExtra("itemtitle");
        arrayid = i.getStringArrayListExtra("itemId");
        Username =i.getStringExtra("username");
        setSupportActionBar(toolbar);
        //toolbar.setTitle(array.get(1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout),
                toolbar);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Fragment fragment= null;
        if(savedInstanceState==null)
        {

            Bundle lst = new Bundle();
            lst.putString("id", arrayid.get(1));

            fragment = new HomeFragment();
            fragment.setArguments(lst);
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.container, fragment, "firsthome").addToBackStack("firsthome");
            tx.commit();




        }






    }


    public void HeartBeat(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SecondConnection con = new SecondConnection();
                String res = con.Heartbit(getApplicationContext());
                Log.e("Timer", res);

            }
        },0, 240000);

    }

    @Override
    protected void onResume() {
        HeartBeat();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
    }

    public static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            if(ex.getClass().equals(OutOfMemoryError.class)) {

                try {
                    android.os.Debug.dumpHprofData("/sdcard/dump.hprof");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }



    @Override
    public void selectItem(int position, ArrayList<String> idlist, String title) {

    }


}
