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
 * Created by miroslav.adamec on 13.11.2016.
 */
public class AddWifiArrayAdapter extends ArrayAdapter<AccessPoint> {

    RepositoryCheckedAPs repositoryCheckedAPs = Repository.getInstance(RepositoryCheckedAPs.class);

    AddWifiArrayAdapter(Context context, List<AccessPoint> wifi) {
        super(context, R.layout.list_item, wifi );
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
        if(checkBox != null){
            if(repositoryCheckedAPs.exist(wifiInfo)){
                checkBox.setChecked(true);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        repositoryCheckedAPs.add(wifiInfo);
                    } else {
                        repositoryCheckedAPs.remove(wifiInfo);
                    }
                }
            });
        }

        return customView;
    }
}
