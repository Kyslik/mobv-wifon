package com.stu.fei.mobv;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class TabFragment3 extends Fragment implements IRefreshFragment, Repository.OnChangeListener {

    View view;
    TextView locationText;
    ListView listView;

    RepositoryAPs repositoryAPs = Repository.getInstance(RepositoryAPs.class);
    List<AccessPoint> accessPointsAround = repositoryAPs.getList();

    WebService ws;

    Location actualLocation = null;

    ListAdapter adapter;

    List<NavigationFragmentAdapter.Step> navigationSteps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_fragment3, container, false);

        listView = (ListView) view.findViewById(R.id.listView3);

        ws = WebService.getInstance(getContext());

//        if(accessPointsAround == null){
//            accessPointsAround = new LinkedList<>();
//        }

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

        if(navigationSteps == null){
            navigationSteps = new LinkedList<>();
            navigationSteps.add(new NavigationFragmentAdapter.Step(1, "Blok A - prízemie", "Pouzite vztah na prizemie"));
            navigationSteps.add(new NavigationFragmentAdapter.Step(2, "Blok B - prizemie", "Pokracujte po prizemi rovno"));
            navigationSteps.add(new NavigationFragmentAdapter.Step(3, "Blok C - prizemie", "Pouzite vytah na 6. poschodie"));
            navigationSteps.add(new NavigationFragmentAdapter.Step(4, "Blok C - 6. poschodie", "Ste na 6. poschodi bloku C"));
        }

        adapter = new NavigationFragmentAdapter(getActivity(), navigationSteps);

        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void refresh() {

    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

            ws = WebService.getInstance(getContext());

            getSuggestions();

        }
    }

    private void getSuggestions(){
        if(repositoryAPs.getSuggestions() == null)
        {
            ws.getSuggestion(accessPointsAround, new WebService.OnSuggestionsReceived() {
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
    public void onChange(List<AccessPoint> list) {
        Log.v("WS", "trigger on Change");

        accessPointsAround.addAll(list);

        ((BaseAdapter)adapter).notifyDataSetChanged();
    }
}
