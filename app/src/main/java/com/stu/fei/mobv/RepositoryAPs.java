package com.stu.fei.mobv;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Michal on 29.11.2016.
 */

public class RepositoryAPs extends Repository {

    public void refresh(Context context){

        WifiManager mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mainWifi.startScan();
        List<ScanResult> wifiList = mainWifi.getScanResults();

        Toast.makeText(context, "Searching for new APs around ..", Toast.LENGTH_LONG).show();

        for (int i = 0; i < wifiList.size(); ++i) {
            AccessPoint ap = AccessPoint.createNew(wifiList.get(i));
            if(!this.exist(ap)){
                this.add(ap);
            }
        }

        triggerOnChange(this.getList());

    }
}
