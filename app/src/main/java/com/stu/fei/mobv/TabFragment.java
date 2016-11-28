package com.stu.fei.mobv;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlado on 23.11.16.
 */

public class TabFragment extends Fragment{

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_fragment, container, false);


        // list_item //

        //String[] dataSource = {"eduroam", "C606"};
        //ArrayAdapter<String> wifiAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.ssid, dataSource);

        List<AccessPoint> accessPoints = new ArrayList<AccessPoint>();
        for (int i = 0; i < 3; ++i) {
            AccessPoint temp =  new AccessPoint();
            temp.ssid = "nejake SSID";
            temp.bssid = "nejake BSSID";
            accessPoints.add(temp);
        }

//        WifiManager mainWifi = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
//        mainWifi.startScan();
//        List<ScanResult> wifiList =mainWifi.getScanResults();
//
//        List<AccessPoint> accessPoints = new ArrayList<AccessPoint>();
//        for (int i = 0; i < wifiList.size(); ++i) {
//            accessPoints.add(AccessPoint.createNew(wifiList.get(i)));
//        }

        ListAdapter wifiAdapter = new AddWifiArrayAdapter(getActivity(), accessPoints);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(wifiAdapter);



        // list_item //


        Spinner dropdown1 = (Spinner)view.findViewById(R.id.spinner1);
        String[] items1 = new String[]{"-1", "0", "1", "2", "3", "4", "5", "6", "7"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items1);
        dropdown1.setAdapter(adapter1);

        Spinner dropdown2 = (Spinner)view.findViewById(R.id.spinner2);
        String[] items2 = new String[]{"A", "B", "C", "D", "E", "T"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown2.setAdapter(adapter2);


        return view;
    }

    public void registerAPs() {
        ListView listView = (ListView) view.findViewById(R.id.listView);
        List<AccessPoint> toRegisterAccessPoints = new ArrayList<AccessPoint>();
        for(int i = 0; i < listView.getCount(); i++) {
            CheckBox checkBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.checkBox);
            if(checkBox.isChecked()) {
                toRegisterAccessPoints.add((AccessPoint) listView.getAdapter().getItem(i));
            }
        }
        String poschodie = ((Spinner) view.findViewById(R.id.spinner1)).getSelectedItem().toString();
        String blok = ((Spinner) view.findViewById(R.id.spinner2)).getSelectedItem().toString();
        int locationId = 0;
        if(blok == "A") {
            switch (poschodie){
                case "-1" : locationId =  1;
                    break;
                case "0" : locationId =  2;
                    break;
                case "1" : locationId =  3;
                    break;
                case "2" : locationId =  4;
                    break;
                case "3" : locationId =  5;
                    break;
                case "4" : locationId =  6;
                    break;
                case "5" : locationId =  7;
                    break;
                case "6" : locationId =  8;
                    break;
                case "7" : locationId =  9;
                    break;
                case "8" : locationId =  10;
                    break;
            }
        }
        if(blok == "B") {
            switch (poschodie){
                case "-1" : locationId =  21;
                    break;
                case "0" : locationId =  22;
                    break;
                case "1" : locationId =  23;
                    break;
                case "2" : locationId =  24;
                    break;
                case "3" : locationId =  25;
                    break;
                case "4" : locationId =  26;
                    break;
                case "5" : locationId =  27;
                    break;
                case "6" : locationId =  28;
                    break;
                case "7" : locationId =  29;
                    break;
            }
        }
        if(blok == "C") {
            switch (poschodie){
                case "-1" : locationId =  11;
                    break;
                case "0" : locationId =  12;
                    break;
                case "1" : locationId =  13;
                    break;
                case "2" : locationId =  14;
                    break;
                case "3" : locationId =  15;
                    break;
                case "4" : locationId =  16;
                    break;
                case "5" : locationId =  17;
                    break;
                case "6" : locationId =  18;
                    break;
                case "7" : locationId =  19;
                    break;
                case "8" : locationId =  20;
                    break;
            }
        }
        if(blok == "D") {
            switch (poschodie){
                case "-1" : locationId =  30;
                    break;
                case "0" : locationId = 31;
                    break;
                case "1" : locationId =  32;
                    break;
                case "2" : locationId =  33;
                    break;
                case "3" : locationId =  34;
                    break;
                case "4" : locationId =  35;
                    break;
                case "5" : locationId =  36;
                    break;
                case "6" : locationId =  37;
                    break;
                case "7" : locationId =  38;
                    break;
            }
        }
        if(blok == "E") {
            switch (poschodie){
                case "-1" : locationId =  39;
                    break;
                case "0" : locationId =  40;
                    break;
                case "1" : locationId =  41;
                    break;
                case "2" : locationId =  42;
                    break;
                case "3" : locationId =  43;
                    break;
                case "4" : locationId =  44;
                    break;
                case "5" : locationId =  45;
                    break;
                case "6" : locationId =  46;
                    break;
                case "7" : locationId =  47;
                    break;
            }
        }
        if(blok == "T") {
            switch (poschodie){
                case "-1" : locationId =  48;
                    break;
                case "0" : locationId =  49;
                    break;
                case "1" : locationId = 50;
                    break;
            }
        }
        registerAPs registerAPsTask = new registerAPs(locationId, toRegisterAccessPoints, getActivity());
        registerAPsTask.execute();
    }
}
