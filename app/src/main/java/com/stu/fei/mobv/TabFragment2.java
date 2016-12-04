package com.stu.fei.mobv;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vlado on 23.11.16.
 */

public class TabFragment2 extends Fragment implements AdapterView.OnItemClickListener, IRefreshFragment {

    ListAdapter adapter;
    RepositoryAPs repositoryAPs = Repository.getInstance(RepositoryAPs.class);
    WebService ws = null;

    List<Location> listLocations = repositoryAPs.getLocationList();
    View view;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_fragment2, container, false);

        ws = WebService.getInstance(getContext());

        listView = (ListView) view.findViewById(R.id.listView2);
        listView.setOnItemClickListener(this);

        if(listLocations == null){
            listLocations = new LinkedList<>();
        }

        adapter = new AddLocationArrayAdapter(getActivity(), listLocations);

        listView.setAdapter(adapter);

//        new getLocations(getActivity()).execute();

//        List<Location> locations = new ArrayList<Location>();
//        AddLocationArrayAdapter locationAdapter = new AddLocationArrayAdapter(getActivity(), locations);
//        for (int i = 0; i < 4; ++i) {
//            Location location = new Location();
//            location.id = i;
//            location.block = Integer.toString(i);
//            location.level = Integer.toString(i);
//            locationAdapter.add(location);
//        }

//        ListView listView = (ListView) view.findViewById(R.id.listView);
//        listView.setAdapter(locationAdapter);

        // list_item //

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        final Location clickedLocation = (Location) adapter.getItem(position);

        Intent intent=new Intent(getActivity(), LocationDetailActivity.class);
        intent.putExtra(LocationDetailActivity.KEY_LOCATION_ID, clickedLocation.id);
        startActivity(intent);

        Toast t = Toast.makeText(getContext(), "Opening location: " + clickedLocation.getName(), Toast.LENGTH_SHORT);
        t.show();

    }

    @Override
    public void refresh() {

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

            ws = WebService.getInstance(getContext());

            if(repositoryAPs.getLocationList() == null)
            {
                ws.getLocations(new WebService.OnLocationsReceived() {
                    @Override
                    public void onSuccess(List<Location> list) {
                        repositoryAPs.setLocationList(list);

                        listLocations.clear();
                        listLocations.addAll(list);
                        ((BaseAdapter)adapter).notifyDataSetChanged();

                    }
                });
            }

        }
    }

}
