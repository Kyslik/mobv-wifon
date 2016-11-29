package com.stu.fei.mobv;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    RepositoryAPs repositoryAPs = Repository.getInstance(RepositoryAPs.class);

    String[] testArr = {"text1", "text2", "text3"};

    final private String TAB_FRAGMENT_TAG = "home";
    TabLayout tabLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // set tab_layout ****************** //

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(), this));

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // set tab_layout ****************** //


    }


    public void exampleImgButton(View view) {
        Toast.makeText(this,"location", Toast.LENGTH_SHORT).show();
    }

    public void registerClick(View view)
    {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        Fragment fragment = ((FragmentPageAdapter)viewPager.getAdapter()).fragment;
        if(fragment instanceof TabFragment) {
            ((TabFragment) fragment).registerAPs();
        }
    }

}
