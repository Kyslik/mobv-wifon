package com.stu.fei.mobv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 11/27/16.
 */
public class getAPs extends AsyncTask<Void, Void, Void> {

    int locationId;
    List<AccessPoint> accessPoints;
    WeakReference<Activity> weakActivity;
    ProgressDialog pDialog;

    public getAPs(int locationIdSrc, Activity activity) {
        locationId = locationIdSrc;
        weakActivity = new WeakReference<Activity>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Activity activity = weakActivity.get();
        if (activity != null) {
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void ...parms) {
        HttpHandler sh = new HttpHandler();
        accessPoints = new ArrayList<AccessPoint>();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall("http://mobv-server.visi.sk/api/v1/locations/"+ Integer.toString(locationId) +"/access-points");

        Log.e("MOBV", "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONArray locations = new JSONArray(jsonStr);

                for (int i = 0; i < locations.length(); i++) {
                    JSONObject c = locations.getJSONObject(i);

                    AccessPoint ap = new AccessPoint();
                    ap.ssid = c.getString("ssid");
                    ap.bssid = c.getString("bssid");
                    ap.timestamp = c.getString("timestamp");
                    ap.frequency = c.getString("frequency");
                    ap.level = c.getString("level");
                    ap.capabilities = c.getString("capabilities");
                    accessPoints.add(ap);
                }
            } catch (final JSONException e) {
//                Log.e("MOVC", "Json parsing error: " + e.getMessage());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Json parsing error: " + e.getMessage(),
//                                Toast.LENGTH_LONG)
//                                .show();
//                    }
//                });
            }
        } else {
//            Log.e("MOBV", "Couldn't get json from server.");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),
//                            "Couldn't get json from server. Check LogCat for possible errors!",
//                            Toast.LENGTH_LONG)
//                            .show();
//                }
//            });
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();

        Activity activity = weakActivity.get();
        if (activity != null) {
            ListAdapter wifiAdapter = new AddWifiArrayAdapter(activity, accessPoints);
            ListView listView = (ListView) activity.findViewById(R.id.listView);
            listView.setAdapter(wifiAdapter);
        }
    }
}
