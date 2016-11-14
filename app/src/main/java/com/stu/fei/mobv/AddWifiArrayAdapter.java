package com.stu.fei.mobv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by miroslav.adamec on 13.11.2016.
 */
public class AddWifiArrayAdapter extends ArrayAdapter<String> {

    AddWifiArrayAdapter(Context context, List<String> wifi) {
        super(context, R.layout.list_item, wifi );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_item, parent, false);

        String wifiInfo = getItem(position);
        TextView wifiTextView = (TextView) customView.findViewById(R.id.summary);
        wifiTextView.setText(wifiInfo);
        return customView;
    }

}
