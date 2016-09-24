package com.paya.authomation.fragments;

/**
 * Created by Administrator on 07/22/2016.
 */

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paya.authomation.connections.SecondConnection;
import com.paya.authomation.main.CustomDrawerAdapter;
import com.paya.authomation.main.DividerItemDecoration;
import com.paya.authomation.main.JsonContent;
import com.paya.authomation.main.Letters;
import com.paya.authomation.main.MainActivity;
import com.paya.authomation.main.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;





/**
 * Created by Administrator on 06/24/2016.
 */
public class NavigationDrawerFragment extends Fragment {

    final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    public ListView mDrawerListView;
    boolean mSavedInstanceState;
    boolean userOpenDrawer;
    List<String> dataList;
    RecyclerView recycle;
    TypedArray navIcons;
    ArrayList<String> arrayList;
    ArrayList<String> ar = new ArrayList<>();
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout = null;
    // String navTitles[];
    private static ActionBarDrawerToggle mDrawerToggle;
    private NavigationDrawerCallbacks mCallbacks;
    private int mCurrentSelectedPosition = 0;
    private ProgressDialog progress;
    List<Letters> lettersList = new ArrayList<Letters>();
    private SharedPreferences listPreferences;
    private SharedPreferences.Editor listPrefsEditor;
    Letters lett;
    private View.OnClickListener mOriginalListener;
    static ArrayList<String> set;
    private View rootview;
    private List<WeakReference<Fragment>> mFragments = new ArrayList<WeakReference<Fragment>>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mSavedInstanceState = true;
        }
        if (userOpenDrawer) {
            userOpenDrawer = false;
        }
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_navigation_drawer, null);

        recycle = (RecyclerView) rootview.findViewById(R.id.list);
        navIcons = getResources().obtainTypedArray(R.array.navIcon);
        arrayList = new ArrayList<String>();




        listPreferences = getActivity().getSharedPreferences("listPrefs", getActivity().MODE_PRIVATE);
        listPreferences.edit();


        final String setlist = listPreferences.getString("item","");
        String username = listPreferences.getString("username", "");
        String setid = listPreferences.getString("id","");


        Gson json = new Gson();

        java.lang.reflect.Type listOfTestObject =  new TypeToken<List<String>>(){}.getType();
       set = json.fromJson(setlist, (java.lang.reflect.Type) listOfTestObject);

        final ArrayList<String> idlist = json.fromJson(setid, listOfTestObject);
        CustomDrawerAdapter adapter = new CustomDrawerAdapter(set, navIcons,username, getActivity());
        recycle.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recycle.setLayoutManager(llm);
        DividerItemDecoration decor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST,4);

        recycle.addItemDecoration(decor);

        adapter.setOnItemClickListener(new CustomDrawerAdapter.ClickListener() {
            @Override
            public void onItemClick( View view) {
                int position = recycle.getChildAdapterPosition(view);

                selectItem(position, idlist, set.get(position-1));



            }
        });

        return rootview;
    }

    public void toggleDrawerUse(boolean useDrawer) {
        // Enable/Disable the icon being used by the drawer
        mDrawerToggle.setDrawerIndicatorEnabled(useDrawer);

        if(useDrawer)
            mDrawerToggle.setToolbarNavigationClickListener(mOriginalListener);
        else
            mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Custom listener", Toast.LENGTH_SHORT).show();
                }
            });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("here", "Drawer responding to menu click...");
        if(item.getItemId() == android.R.id.home) Log.e("here", "Drawer got it....");
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUp(int fragmentid, DrawerLayout drawerlayout, final Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentid);
        mDrawerLayout = drawerlayout;

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);

      //actionBar.setIcon(R.drawable.ic_menu_back);



        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        //toolbar.setNavigationIcon(R.drawable.ic_menu_back);



        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close) {


            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if(item!= null && item.getItemId()== android.R.id.home)

                {
                    if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);

                    }
                    else{
                        mDrawerLayout.openDrawer(Gravity.RIGHT);

                    }

                }
                return false;
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!userOpenDrawer) {
                    userOpenDrawer = true;

                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if (!userOpenDrawer && !mSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);

        }
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        final View.OnClickListener originalToolbarListener = mDrawerToggle.getToolbarNavigationClickListener();

        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    mDrawerToggle.setDrawerIndicatorEnabled(false);
                    toolbar.setNavigationIcon(R.drawable.ic_menu_forward1);
                    mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getActivity().getSupportFragmentManager().popBackStack();

                        }
                    });
                } else {
                    mDrawerToggle.setDrawerIndicatorEnabled(true);


                        toolbar.setTitle(set.get(1));

                    mDrawerToggle.setToolbarNavigationClickListener(originalToolbarListener);
                }


            }
        });

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }



    public void selectItem(int position, ArrayList<String> id, String title) {
        Fragment fragment = null;



        Bundle lst = new Bundle();
        lst.putString("id", id.get(position - 1));

        fragment = new HomeFragment();

        fragment.setArguments(lst);




        if (fragment != null) {
            recycleFragments();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment,"home").addToBackStack("home").commit();
            fragmentManager.beginTransaction().commitAllowingStateLoss();


                getActionBar().setTitle(title);

            mDrawerLayout.closeDrawer(recycle);
        }

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub

        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
        mCallbacks = null;
        rootview = null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rootview = null;
        recycle=null;
    }
    private void recycleFragments() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        for (WeakReference<Fragment> ref : mFragments) {
            Fragment fragment = ref.get();
            if (fragment != null) {
                ft.remove(fragment);
            }
        }
    }

    public interface NavigationDrawerCallbacks {
        void selectItem(int position, ArrayList<String> idlist, String title);

    }

    private class task extends AsyncTask<Void, String, List<Letters>> {

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(),"....","لطفا منتظر بمانید" ,true);

        }

        @Override
        protected List<Letters> doInBackground(Void... voids) {
            SecondConnection con = new SecondConnection();
            String res = con.method(getActivity());
            lettersList= JsonContent.getData(res);
            Log.e("do background",res);
            return lettersList;
        }

        @Override
        protected void onPostExecute(List<Letters> s) {



            for (Letters g:s){
                Log.e("in post", g.getTitle());
                arrayList.add(g.getID());
            }

            progress.dismiss();
        }
    }

}
