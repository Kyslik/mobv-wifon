package com.stu.fei.mobv;


import android.content.Context;
import android.graphics.Typeface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlado on 23.11.16.
 */

public class TabFragment extends Fragment implements Repository.OnChangeListener {

    View view;
    RepositoryCheckedAPs repositoryCheckedAPs = Repository.getInstance(RepositoryCheckedAPs.class);
    RepositoryAPs repositoryAPs = Repository.getInstance(RepositoryAPs.class);

    TextView locationText;

    ListAdapter wifiAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public void onRefresh() {
        if(swipeRefreshLayout != null){

            Toast t = Toast.makeText(getActivity(), "Searching ..", Toast.LENGTH_SHORT);
            t.show();

            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setProgressViewOffset(false, 0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

            swipeRefreshLayout.setRefreshing(true);

            repositoryAPs.refresh(getContext());

            ((BaseAdapter)wifiAdapter).notifyDataSetChanged();

            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_fragment, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(false);

        // list_item //

        //String[] dataSource = {"eduroam", "C606"};
        //ArrayAdapter<String> wifiAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.ssid, dataSource);

//        List<AccessPoint> accessPoints = new ArrayList<AccessPoint>();
//        for (int i = 0; i < 3; ++i) {
//            AccessPoint temp =  new AccessPoint();
//            temp.ssid = "nejake SSID";
//            temp.bssid = "nejake BSSID";
//            accessPoints.add(temp);
//        }

//        WifiManager mainWifi = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
//        mainWifi.startScan();
//        List<ScanResult> wifiList =mainWifi.getScanResults();
//
//        List<AccessPoint> accessPoints = new ArrayList<AccessPoint>();
//        for (int i = 0; i < wifiList.size(); ++i) {
//            accessPoints.add(AccessPoint.createNew(wifiList.get(i)));
//        }

        repositoryAPs.refresh(getContext());

        wifiAdapter = new AddWifiArrayAdapter(getActivity(), repositoryAPs.getList());
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(wifiAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AccessPoint ap = (AccessPoint) adapterView.getItemAtPosition(i);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                if(repositoryCheckedAPs.exist(ap)){
                    checkBox.setChecked(true);
                    repositoryCheckedAPs.add(ap);
                } else {
                    checkBox.setChecked(false);
                    repositoryCheckedAPs.remove(ap);
                }
            }
        });

        repositoryAPs.registerOnChangeListener(this);

        // list_item //



        //String[] items1 = new String[]{"-1", "0", "1", "2", "3", "4", "5", "6", "7"};
        //ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items1);
        //dropdown1.setAdapter(adapter1);

        final Spinner dropdown2 = (Spinner)view.findViewById(R.id.spinner2);
        String[] items2 = new String[]{"A", "B", "C", "D", "E", "T"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items2);
        adapter2.setDropDownViewResource(R.layout.dropdown);
        dropdown2.setAdapter(adapter2);

        dropdown2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final String newValue = (String) dropdown2.getItemAtPosition(position);
                String[] floorItems;
                switch (newValue) {
                    case "A" : floorItems = new String[]{"-1", "0", "1", "2", "3", "4", "5", "6", "7", "8"};
                        break;
                    case "B" : floorItems = new String[]{"-1", "0", "1", "2", "3", "4", "5", "6", "7"};
                        break;
                    case "C" : floorItems = new String[]{"-1", "0", "1", "2", "3", "4", "5", "6", "7", "8"};
                        break;
                    case "D" : floorItems = new String[]{"-1", "0", "1", "2", "3", "4", "5", "6", "7"};
                        break;
                    case "E" : floorItems = new String[]{"-1", "0", "1", "2", "3", "4", "5", "6", "7"};
                        break;
                    case "T" : floorItems = new String[]{"-1", "0", "1"};
                        break;
                    default: floorItems = new String[0];
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, floorItems);
                adapter1.setDropDownViewResource(R.layout.dropdown);
                Spinner dropdown1 = (Spinner)view.findViewById(R.id.spinner1);
                dropdown1.setAdapter(adapter1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        Typeface font = Typeface.createFromAsset( getActivity().getAssets(), "fontawesome-webfont.ttf" );
        Button button = (Button)view.findViewById( R.id.button );
        button.setTypeface(font);

        locationText = (TextView) view.findViewById(R.id.locationText);

        return view;
    }

    public void refresh(){
        ((BaseAdapter)wifiAdapter).notifyDataSetChanged();
    }

    public void registerAPs() {
        ListView listView = (ListView) view.findViewById(R.id.listView);
        List<AccessPoint> toRegisterAccessPoints = repositoryCheckedAPs.getList();
//        for(int i = 0; i < listView.getCount(); i++) {
//            CheckBox checkBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.checkBox);
//            if(checkBox.isChecked()) {
//                toRegisterAccessPoints.add((AccessPoint) listView.getAdapter().getItem(i));
//            }
//        }
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

        Toast t = Toast.makeText(getActivity(), "APs saved to server", Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    public void onChange(List<AccessPoint> list) {

        Log.v("WS", "trigger on Change");
        WebService ws = WebService.getInstance(getContext());
        if(list != null){
            ws.getSuggestion(list, new WebService.OnSuggestionsReceived() {
                @Override
                public void onSuccess(List<Location> list) {
                    if(list != null && list.size() > 0){
                        Location location = list.get(0);
                        locationText.setText("Nachádzate sa na: " + location.getName());
                    }
                    else {
                        locationText.setText("Vaša poloha nebola zistená");
                    }
                }

                @Override
                public void onFailure(String responseString) {
                    Toast t  = Toast.makeText(getActivity(), "Nieco je zle :(", Toast.LENGTH_LONG);
                    t.show();
                }
            });
        }

    }
}
