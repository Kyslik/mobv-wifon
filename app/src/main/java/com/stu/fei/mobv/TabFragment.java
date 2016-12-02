package com.stu.fei.mobv;


import android.content.Intent;
import android.graphics.Typeface;
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

import java.util.LinkedList;
import java.util.List;

/**
 * Created by vlado on 23.11.16.
 */

public class TabFragment extends Fragment implements Repository.OnChangeListener, IRefreshFragment, WebService.OnAccessPointsRegistered {

    View view;
    RepositoryCheckedAPs repositoryCheckedAPs = Repository.getInstance(RepositoryCheckedAPs.class);
    RepositoryAPs repositoryAPs = Repository.getInstance(RepositoryAPs.class);
    WebService ws = null;

    TextView locationText;
    Location actualLocation = null;
    List<Location> locationList = null;

    List<AccessPoint> accessPointsAround = new LinkedList<>();


    ListAdapter wifiAdapter;
    ListView listView;
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

        wifiAdapter = new AddWifiArrayAdapter(getActivity(), accessPointsAround);
        listView = (ListView) view.findViewById(R.id.listView);
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


        ws = WebService.getInstance(getContext());
        ws.getLocations(new WebService.OnLocationsReceived() {
            @Override
            public void onSuccess(List<Location> list) {
                locationList = list;


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


            }
        });


        Typeface font = Typeface.createFromAsset( getActivity().getAssets(), "fontawesome-webfont.ttf" );
        Button button = (Button)view.findViewById( R.id.button );
        button.setTypeface(font);

        locationText = (TextView) view.findViewById(R.id.locationText);

        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repositoryAPs.setClickedLocation(actualLocation);
                Intent myIntent = new Intent(getActivity(), ActualLocationDetailActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        repositoryAPs.registerOnChangeListener(this);

        return view;
    }

    public void refresh(){
        repositoryAPs.refresh(getContext());
    }

    public void clear(){
        repositoryCheckedAPs.removeAll();
        repositoryAPs.removeAll();
    }


    public void registerAPs() {

        final List<AccessPoint> toRegisterAccessPoints = repositoryCheckedAPs.getList();

        final String level = ((Spinner) view.findViewById(R.id.spinner1)).getSelectedItem().toString();
        final String blok = ((Spinner) view.findViewById(R.id.spinner2)).getSelectedItem().toString();

        repositoryAPs.getLocationList();

        if(locationList != null){
            for(Location location: locationList){
                if(location.block.equals(blok) && location.level.equals(level)){

                    Log.v("WS", location.toString());

//                            registerAPs registerAPsTask = new registerAPs(location.id, toRegisterAccessPoints, getActivity());
//                            registerAPsTask.execute();

                    ws.registerAccessPointsForLocation(toRegisterAccessPoints, location.id, this);

                    return;
                }
            }
        } else {
            Toast t = Toast.makeText(getActivity(), "Location list is null :(", Toast.LENGTH_LONG);
            t.show();
        }


    }

    @Override
    public void onChange(List<AccessPoint> list) {

        Log.v("WS", "trigger on Change");
        WebService ws = WebService.getInstance(getContext());
        if(list != null){
            ws.getSuggestion(list, new WebService.OnSuggestionsReceived() {
                @Override
                public void onSuccess(List<Location> list) {
                    setupActualLocation(list);
                }

                @Override
                public void onFailure(String responseString) {
                    Toast t  = Toast.makeText(getActivity(), "Nieco je zle :(", Toast.LENGTH_LONG);
                    t.show();
                }
            });
        }

        accessPointsAround.clear();

        for(AccessPoint ap: list){
            accessPointsAround.add(ap);
        }

        ((BaseAdapter)wifiAdapter).notifyDataSetChanged();
//        listView.invalidateViews();

    }

    private void setupActualLocation(List<Location> list){
        if(list != null && list.size() > 0){
            actualLocation = list.get(0);
            locationText.setText("Nachádzate sa na: " + actualLocation.getName());
        }
        else {
            locationText.setText("Vaša poloha nebola zistená");
        }
    }

    @Override
    public void onSuccess() {

        Toast t = Toast.makeText(getActivity(), "Access Points was successfully registered", Toast.LENGTH_LONG);
        t.show();

        clear();
    }
}
