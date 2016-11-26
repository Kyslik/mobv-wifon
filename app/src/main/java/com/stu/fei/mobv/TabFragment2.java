package com.stu.fei.mobv;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by vlado on 23.11.16.
 */

public class TabFragment2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment2, container, false);

        // list_item //

        String[] dataSource = {"Blok C - prízemie","Blok A - prízemie"};
        ArrayAdapter<String> wifiAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_info, R.id.location, dataSource);

        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(wifiAdapter);

        // list_item //

        return view;
    }
}
