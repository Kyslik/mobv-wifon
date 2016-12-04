package com.stu.fei.mobv;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    public static final String KEY_FRAGMENT_TAG = "com.stu.fei.mobv.KEY_FRAGMENT_TAG";

//    private ListView roomListView;

    RepositoryAPs repositoryAPs = Repository.getInstance(RepositoryAPs.class);

//    String[] testArr = {"text1", "text2", "text3"};

    final private String TAB_FRAGMENT_TAG = "home";
    TabLayout tabLayout;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set tab_layout ****************** //

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(), this));

//        roomList = new ArrayList<>();
//        roomList.add(new RoomListAdapter.Room("AB300"));
//        roomList.add(new RoomListAdapter.Room("BC300"));
//        roomList.add(new RoomListAdapter.Room("CD300"));
//        roomList.add(new RoomListAdapter.Room("DE300"));




//        if (getIntent().hasExtra(KEY_FRAGMENT_TAG)) {
//            Bundle b = getIntent().getExtras();
//            String fragmentTag = b.getString(KEY_FRAGMENT_TAG);
//
//            FragmentManager fm = getFragmentManager();
//            fm.beginTransaction()
//                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
//                    .show(getFragmentManager().findFragmentByTag(fragmentTag))
//                    .commit();
//
//        }

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // set tab_layout ****************** //

        repositoryAPs.refresh(getApplicationContext());



    }


    public void exampleImgButton(View view) {
        Toast.makeText(this, "location", Toast.LENGTH_SHORT).show();
    }

    public void registerClick(View view) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        Fragment fragment = ((FragmentPageAdapter) viewPager.getAdapter()).fragment;
        if (fragment instanceof TabFragment) {
            ((TabFragment) fragment).registerAPs();

//            ((TabFragment) fragment).clear();
        }
    }

    SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.action_search).getActionView();

//        searchView.setSearchableInfo(searchManager.
//                getSearchableInfo(getComponentName()));

//        searchView.setSearchableInfo(searchManager.getSearchableInfo(
//                new ComponentName(getApplicationContext(), SearchResultsActivity.class)));
//
//        searchView.setSubmitButtonEnabled(true);



//        searchView.setOnQueryTextListener(this);

        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
//        Fragment fragment = ((FragmentPageAdapter) viewPager.getAdapter()).getActualFragment();
//
//        switch (id) {
//            case R.id.refresh:
//
//                if (fragment != null) {
//                    ((IRefreshFragment) fragment).refresh();
//                }
//                break;
//            case R.id.clear:
//
//                if (fragment != null && fragment instanceof TabFragment) {
//                    ((TabFragment) fragment).clear();
//                }
//
//                break;
//
//            case R.id.findMe:
//                if (fragment != null && fragment instanceof TabFragment) {
//                    ((TabFragment) fragment).findMe();
//                }
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


}
