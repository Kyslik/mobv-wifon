package com.stu.fei.mobv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by miroslav.adamec on 13.11.2016.
 */
public class NavigationFragmentAdapter extends ArrayAdapter<NavigationFragmentAdapter.Step> {

    public static class Step{
        public int stepNumber;
        public String locationText;
        public String locationSubText;

        public Step(int stepNumber, String locationText, String locationSubText) {
            this.stepNumber = stepNumber;
            this.locationText = locationText;
            this.locationSubText = locationSubText;
        }
    }

    RepositoryAPs repositoryAPs = Repository.getInstance(RepositoryAPs.class);
    Location actualLocation;

    NavigationFragmentAdapter(Context context, List<Step> steps) {

        super(context, R.layout.list_item_step, steps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_item_step, parent, false);

        final Step step = getItem(position);

        TextView locationShortView = (TextView) customView.findViewById(R.id.locationShort);
        locationShortView.setText(String.valueOf(step.stepNumber) + ".");


        TextView infoTextView = (TextView) customView.findViewById(R.id.location);
        infoTextView.setText(step.locationText);

        TextView infoUpdatedAtTextView = (TextView) customView.findViewById(R.id.infoUpdatedAt);
        infoUpdatedAtTextView.setText(step.locationSubText);

        return customView;
    }
}
