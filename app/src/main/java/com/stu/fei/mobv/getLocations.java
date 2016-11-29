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
 * Created by roman on 11/28/16.
 */

public class getLocations extends AsyncTask<Void, Void, Void> {

    List<Location> locations;
    WeakReference<Activity> weakActivity;
    ProgressDialog pDialog;

    public getLocations(Activity activity) {
        weakActivity = new WeakReference<Activity>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Activity activity = weakActivity.get();
        if (activity != null) {
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Loading ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void ...parms) {
        HttpHandler sh = new HttpHandler();
        locations = new ArrayList<Location>();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall("http://mobv-server.visi.sk/api/v1/locations/");

        Log.e("MOBV", "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONArray locationsJson = new JSONArray(jsonStr);

                for (int i = 0; i < locationsJson.length(); i++) {
                    JSONObject c = locationsJson.getJSONObject(i);

                    Location location = new Location();
                    location.id = c.getInt("id");
                    location.block = c.getString("block");
                    location.level = c.getString("level");

                    locations.add(location);
                }
            } catch (final JSONException e) {
                Log.e("MOBV", "Json parsing error: " + e.getMessage());
                final Activity activity = weakActivity.get();
                if (activity == null)
                    return null;

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        } else {
            Log.e("MOBV", "Couldn't get json from server.");
            final Activity activity = weakActivity.get();
            if (activity == null)
                return null;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
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
            ListAdapter wifiAdapter = new AddLocationArrayAdapter(activity, locations);
            ListView listView = (ListView) activity.findViewById(R.id.listView2);
            listView.setAdapter(wifiAdapter);
        }
    }
}