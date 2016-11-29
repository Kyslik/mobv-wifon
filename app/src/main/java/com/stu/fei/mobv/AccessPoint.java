package com.stu.fei.mobv;

import android.net.wifi.ScanResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by miroslav.adamec on 14.11.2016.
 */
public class AccessPoint {
    public String ssid;
    public String bssid;
    public String capabilities;
    public String level;
    public String frequency;
    public String timestamp;
    public String distance;
    public String distanceSd;

    public static AccessPoint createNew(ScanResult scannedAP) {
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.ssid = scannedAP.SSID;
        accessPoint.bssid = scannedAP.BSSID;
        accessPoint.capabilities = scannedAP.capabilities;
        accessPoint.level = Integer.toString(scannedAP.level);
        accessPoint.frequency = Integer.toString(scannedAP.frequency);
        accessPoint.timestamp = Long.toString(scannedAP.timestamp);

        return accessPoint;
    }

    public String toString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("ssid", ssid);
            jsonObject.accumulate("bssid", bssid);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
}
