package com.stu.fei.mobv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class LocationDetailActivity extends AppCompatActivity {

    public static final String KEY_LOCATION_ID = "com.stu.fei.mobv.KEY_LOCATION_ID";
    private Integer locationId;

    private Location actualLocation = null;
    ListAdapter adapter;

    WebService ws = null;

    ListView listView = null;

    List<AccessPoint> accessPoints = new LinkedList<>();
    List<AccessPoint> accessPointsAround = new LinkedList<>();

    RepositoryAPs repositoryAPs = Repository.getInstance(RepositoryAPs.class);

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

        adapter = new LocationDetailArrayAdapter(getApplicationContext(), accessPoints, accessPointsAround);
        listView.setAdapter(adapter);

        ws = WebService.getInstance(getApplicationContext());
        ws.getLocation(locationId, new WebService.OnLocationReceived() {
            @Override
            public void onSuccess(Location location) {
                actualLocation = location;
                actualizeAccessPoints(location.getAccessPoints());
                if (actualLocation != null) {
                    getSupportActionBar().setTitle(actualLocation.getName());


                } else {
                    getSupportActionBar().setTitle("No location");
                }

            }
        });


        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    removeAcessPoint(position);


                                }


                            }
                        });
        listView.setOnTouchListener(touchListener);
    }

    private void removeAcessPoint(final int position) {
        final AccessPoint ap = accessPoints.get(position);
//        dissmisableAccessPoints.add(ap);

        accessPoints.remove(position);
        ((BaseAdapter) adapter).notifyDataSetChanged();

        ws.removeAccessPoint(locationId, ap.id, new WebService.OnAccessPointRemoved() {
            @Override
            public void onSuccess() {

//                dissmisableAccessPoints.remove(ap);
            }

            @Override
            public void onFailure() {

                accessPoints.add(ap);
                ((BaseAdapter) adapter).notifyDataSetChanged();

            }


        });

    }

    private void actualizeAccessPoints(List<AccessPoint> newAccessPoints) {

        accessPoints.clear();

        for (AccessPoint ap : newAccessPoints) {
            accessPoints.add(ap);
        }

        ((BaseAdapter) adapter).notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent defineFragmentIntent = new Intent(getApplication(), MainActivity.class);
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.compare:

                accessPointsAround.clear();
                accessPointsAround.addAll(repositoryAPs.getList());
                ((BaseAdapter) adapter).notifyDataSetChanged();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }
    }

//    public boolean onContextItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.remove)
//            Toast.makeText(getApplicationContext(), "Remove Clicked", Toast.LENGTH_LONG).show();
////        if(item.getTitle()=="Delete")Toast.makeText(getApplicationContext(), "Delete Clicked", Toast.LENGTH_LONG).show();
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location_detail_menu, menu);
        return true;
    }


}
