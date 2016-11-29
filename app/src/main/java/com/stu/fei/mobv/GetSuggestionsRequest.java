package com.stu.fei.mobv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by roman on 11/29/16.
 */

public class GetSuggestionsRequest extends AsyncTask<Void, Void, Void> {
    List<AccessPoint> accessPoints;

    List<Location>  suggestedLcations;
    Map<Integer, List<AccessPoint>> locationsAPs;

    ProgressDialog pDialog;
    WeakReference<Activity> weakActivity;

    public GetSuggestionsRequest(List<AccessPoint> accessPointsSrc, Activity activity) {
        accessPoints = accessPointsSrc;
        weakActivity = new WeakReference<Activity>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Activity activity = weakActivity.get();
        if (activity != null) {
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Sending ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void ...params) {
        try {
            String link = new String("http://mobv-server.visi.sk/api/v1/locations/find");

            URL url = new URL(link); //Enter URL here
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(createJson(accessPoints));
            wr.flush();
            wr.close();

            Log.i("NET", httpURLConnection.getRequestMethod());
            Log.i("NET", httpURLConnection.getURL().getPath());
            Log.i("NET", Integer.toString(httpURLConnection.getResponseCode()));

            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            String jsonStr = HttpHandler.convertStreamToString(in);
            if (jsonStr != null) {
                try {
                    suggestedLcations = new ArrayList<Location>();
                    locationsAPs = new HashMap<Integer, List<AccessPoint>>();

                    JSONObject root = new JSONObject(jsonStr);
                    JSONArray suggestions = root.getJSONArray("suggestions");
                    for (int i = 0; i < suggestions.length(); ++i) {
                        JSONObject suggestionRoot = suggestions.getJSONObject(i);
                        JSONObject suggestion = suggestionRoot.getJSONObject("location");

                        Location location = new Location();
                        location.id = suggestion.getInt("id");
                        location.block = suggestion.getString("block");
                        location.level = suggestion.getString("level");
                        location.matchCount = suggestionRoot.getInt("match_count");
                        suggestedLcations.add(location);

                        JSONArray locationAps = suggestion.getJSONArray("access_points");
                        List<AccessPoint> aps = new ArrayList<AccessPoint>();
                        for (int j = 0; j < locationAps.length(); ++j) {
                            JSONObject apObj = locationAps.getJSONObject(j);

                            AccessPoint ap = new AccessPoint();
                            ap.bssid = apObj.getString("bssid");
                            ap.ssid = apObj.getString("ssid");

                            aps.add(ap);
                        }
                        locationsAPs.put(location.id, aps);
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Void result) {
        // Dismiss the progress dialog
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();

        Toast.makeText(weakActivity.get(), "Data received", Toast.LENGTH_LONG).show();
    }

    static private String createJson(List<AccessPoint> aps)
    {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < aps.size() ; ++i) {
            AccessPoint ap = aps.get(i);
            jsonArray.put(ap.bssid);
        }

        String json = jsonArray.toString();
        Log.d("JSON", json);
        return json;
    }
}
