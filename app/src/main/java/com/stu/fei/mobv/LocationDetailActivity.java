package com.stu.fei.mobv;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class LocationDetailActivity extends AppCompatActivity {

    public static final String KEY_LOCATION_ID = "com.stu.fei.mobv.KEY_LOCATION_ID";
    private Integer locationId;

    private Location actualLocation = null;
    ListAdapter adapter;

    WebService ws = null;

    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(KEY_LOCATION_ID)) {
            Bundle b = getIntent().getExtras();
            locationId = b.getInt(KEY_LOCATION_ID);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + KEY_LOCATION_ID);
        }

        listView = (ListView) findViewById(R.id.listView);

        ws = WebService.getInstance(getApplicationContext());
        ws.getLocation(locationId, new WebService.OnLocationReceived() {
            @Override
            public void onSuccess(Location location) {
                actualLocation = location;

                if(actualLocation != null){
                    getSupportActionBar().setTitle(actualLocation.getName());

                    adapter = new LocationDetailArrayAdapter(getApplicationContext(), actualLocation.getAccessPoints());

                    listView.setAdapter(adapter);

                } else {
                    getSupportActionBar().setTitle("No location");
                }

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
