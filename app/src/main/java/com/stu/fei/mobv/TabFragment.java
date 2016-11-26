package com.stu.fei.mobv;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by vlado on 23.11.16.
 */

public class TabFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment, container, false);


        // list_item //

        String[] dataSource = {"eduroam", "C606"};
        ArrayAdapter<String> wifiAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.ssid, dataSource);

        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(wifiAdapter);



        // list_item //


        return view;
    }



}
