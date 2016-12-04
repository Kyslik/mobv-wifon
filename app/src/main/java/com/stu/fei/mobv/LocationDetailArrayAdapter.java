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
public class LocationDetailArrayAdapter extends ArrayAdapter<AccessPoint> {

    List<AccessPoint> accessPointsAround = null;
    LocationDetailArrayAdapter(Context context, List<AccessPoint> wifi, List<AccessPoint> accessPointsAround) {
        super(context, R.layout.list_item, wifi );

        this.accessPointsAround = accessPointsAround;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_item, parent, false);

        final AccessPoint wifiInfo = getItem(position);
        TextView ssidTextView = (TextView) customView.findViewById(R.id.ssid);
        ssidTextView.setText(wifiInfo.ssid);
        TextView bssidTextView = (TextView) customView.findViewById(R.id.bssid);
        bssidTextView.setText(wifiInfo.bssid);

        final CheckBox checkBox = (CheckBox) customView.findViewById(R.id.checkBox);

        if(accessPointsAround.size() == 0){
            checkBox.setEnabled(false);
        } else {
            checkBox.setEnabled(true);
            checkBox.setChecked(false);
            for(AccessPoint ap: accessPointsAround){
                if(ap.bssid.equals(wifiInfo.bssid)){
                    checkBox.setChecked(true);
                    break;
                }
            }

            checkBox.setClickable(false);
        }



        return customView;
    }
}
