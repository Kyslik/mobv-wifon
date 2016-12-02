package com.stu.fei.mobv;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vlado on 23.11.16.
 */

public class TabFragment2 extends Fragment {

    ListAdapter adapter;
    WebService ws = null;
    List<Location> listLocations = new LinkedList<>();
    View view;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.tab_fragment2, container, false);

        // list_item //
        ws = WebService.getInstance(getContext());
        ws.getLocations(new WebService.OnLocationsReceived() {
            @Override
            public void onSuccess(List<Location> list) {
                listLocations = list;

                adapter = new AddLocationArrayAdapter(getActivity(), listLocations);
                listView = (ListView) view.findViewById(R.id.listView2);
                listView.setAdapter(adapter);
            }
        });




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
}
