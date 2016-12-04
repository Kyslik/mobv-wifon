package com.stu.fei.mobv;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    RoomListAdapter roomListAdapter;

    ArrayList<RoomListAdapter.Room> roomList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        roomList = new ArrayList<>();
        roomList.add(new RoomListAdapter.Room("AB300"));
        roomList.add(new RoomListAdapter.Room("BC300"));
        roomList.add(new RoomListAdapter.Room("CD300"));
        roomList.add(new RoomListAdapter.Room("DE300"));

        roomListAdapter = new RoomListAdapter(this, roomList);

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Toast.makeText(getApplicationContext(), "query: " + query, Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        roomListAdapter.getFilter().filter(newText);

        return false;
    }
}
