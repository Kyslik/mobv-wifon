package com.stu.fei.mobv;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] testArr = {"text1", "text2", "text3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> testArr = Arrays.asList("sup1", "sup2", "sup3");
        //List<String> testArr = new ArrayList<String>();

        WifiManager mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mainWifi.startScan();
        List<ScanResult> wifiList =mainWifi.getScanResults();
        for(int i = 0; i < wifiList.size(); i++){
            testArr.add((wifiList.get(i)).toString());
        }


        ListAdapter wifiAdapter = new AddWifiArrayAdapter(this, testArr);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(wifiAdapter);
    }
}
