package com.stu.fei.mobv;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by vlado on 23.11.16.
 */

public class TabFragment3 extends Fragment implements Repository.OnChangeListener {

    private static List<String> LOCATIONS = new LinkedList<>();

    View view;
    TextView locationText;
    ListView listView;
    TextView searchingIndicator;

    RepositoryAPs repositoryAPs = Repository.getInstance(RepositoryAPs.class);
    List<AccessPoint> accessPointsAround = repositoryAPs.getList();

    List<Location> locationsList = repositoryAPs.getLocationList();

    WebService ws;

    Location actualLocation = null;

    ListAdapter adapter;

    List<NavigationFragmentAdapter.Step> navigationSteps = new LinkedList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_fragment3, container, false);
        searchingIndicator = (TextView) view.findViewById(R.id.searchingIndicator);

        listView = (ListView) view.findViewById(R.id.listView3);

        ws = WebService.getInstance(getContext());

//        if(accessPointsAround == null){
//            accessPointsAround = new LinkedList<>();
//        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, LOCATIONS);
        AutoCompleteTextView searchForRoomTextView = (AutoCompleteTextView) view
                .findViewById(R.id.search_for_room);
        searchForRoomTextView.setAdapter(arrayAdapter);


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


        adapter = new NavigationFragmentAdapter(getActivity(), navigationSteps);

        listView.setAdapter(adapter);


        searchForRoomTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                navigateForNewRoom(String.valueOf(charSequence));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void navigateForNewRoom(String roomText){

        if(actualLocation != null){

            if(!roomText.equals("")) {
                navigationSteps.clear();
                List<NavigationFragmentAdapter.Step> newSteps = Navigation.navigate(actualLocation, roomText);
                navigationSteps.addAll(newSteps);

                ((BaseAdapter)adapter).notifyDataSetChanged();
            } else {
                navigationSteps.clear();
                ((BaseAdapter)adapter).notifyDataSetChanged();
            }

        } else {
            Toast.makeText(getContext(), "Please scan your position first.", Toast.LENGTH_LONG).show();
        }

        // vyrataj a vrat navigation items
//        navigationSteps.add(new NavigationFragmentAdapter.Step(1, "Blok A - prízemie", "Pouzite vztah na prizemie"));
//        navigationSteps.add(new NavigationFragmentAdapter.Step(2, "Blok B - prizemie", "Pokracujte po prizemi rovno"));
//        navigationSteps.add(new NavigationFragmentAdapter.Step(3, "Blok C - prizemie", "Pouzite vytah na 6. poschodie"));
//        navigationSteps.add(new NavigationFragmentAdapter.Step(4, "Blok C - 6. poschodie", "Ste na 6. poschodi bloku C"));


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (menu != null) {
            menu.findItem(R.id.refresh).setVisible(false);
            menu.findItem(R.id.clear).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.findMe:
                this.getSuggestions();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

            ws = WebService.getInstance(getContext());

            if(LOCATIONS.size() == 0){
                if(locationsList != null){
                    for(Location location: locationsList){
                        LOCATIONS.add(location.block + location.level + "XX");
                    }
                }
                LOCATIONS.add("AB300");
                LOCATIONS.add("BC300");
                LOCATIONS.add("CD300");
                LOCATIONS.add("DE300");
                LOCATIONS.add("AB150");
                LOCATIONS.add("BC150");
                LOCATIONS.add("CD150");
                LOCATIONS.add("DE150");
            }

            getSuggestions();

        }
    }

    private void getSuggestions(){
        if(searchingIndicator != null){
            searchingIndicator.setVisibility(View.VISIBLE);
        }

        if(repositoryAPs.getSuggestions() == null)
        {
            ws.getSuggestion(repositoryAPs.getList(), new WebService.OnSuggestionsReceived() {
                @Override
                public void onSuccess(List<Location> list) {
                    setupActualLocation(list);
                }

                @Override
                public void onFailure(String responseString) {
                    if(searchingIndicator != null) {
                        searchingIndicator.setVisibility(View.INVISIBLE);
                    }
                    Toast t  = Toast.makeText(getActivity(), "Nieco je zle :(", Toast.LENGTH_LONG);
                    t.show();
                }
            });
        }
    }

    private void setupActualLocation(List<Location> list){
        if(searchingIndicator != null) {
            searchingIndicator.setVisibility(View.INVISIBLE);
        }
        if(list != null && list.size() > 0){
            actualLocation = list.get(0);
            locationText.setText("Nachádzate sa na: " + actualLocation.getName());
        }
        else {
            locationText.setText("Vaša poloha nebola zistená");
        }
    }

    @Override
    public void onChange(List<AccessPoint> list) {
        Log.v("WS", "trigger on Change");

        accessPointsAround.addAll(list);

        ((BaseAdapter)adapter).notifyDataSetChanged();
    }
}
