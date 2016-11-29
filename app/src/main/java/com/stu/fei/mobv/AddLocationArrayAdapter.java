package com.stu.fei.mobv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by roman on 11/28/16.
 */

public class AddLocationArrayAdapter extends ArrayAdapter<Location> {

    AddLocationArrayAdapter(Context context, List<Location> locations) {
        super(context, R.layout.list_item, locations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_item_info, parent, false);

        Location location = getItem(position);
        TextView infoTextView = (TextView) customView.findViewById(R.id.location);
        infoTextView.setText(location.getName());

        return customView;
    }
}