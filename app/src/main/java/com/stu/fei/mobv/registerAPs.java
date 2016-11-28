package com.stu.fei.mobv;

/**
 * Created by roman on 11/27/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class registerAPs extends AsyncTask<Void, Void, String> {
    int locationId;
    List<AccessPoint> accessPoints;

    ProgressDialog pDialog;
    WeakReference<Activity> weakActivity;

    public registerAPs(int locationIdSrc, List<AccessPoint> accessPointsSrc, Activity activity) {
        locationId = locationIdSrc;
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
    protected String doInBackground(Void ...params) {
        try {
            String link = new String("http://mobv-server.visi.sk/api/v1/location/"
                    + Integer.toString(locationId)
                    + "/access-points");

            URL url = new URL(link); //Enter URL here
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(createJson(666, accessPoints));
            wr.flush();
            wr.close();

            Log.i("NET", httpURLConnection.getRequestMethod());
            Log.i("NET", httpURLConnection.getURL().getPath());
            return httpURLConnection.getResponseMessage();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        // Dismiss the progress dialog
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();

        Log.d("NET", result);

        Toast.makeText(weakActivity.get(), result, Toast.LENGTH_LONG).show();
    }

    static private String createJson(int deviceId, List<AccessPoint> aps)
    {
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < aps.size() ; ++i) {
                AccessPoint ap = aps.get(i);

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("ssid", ap.ssid);
                jsonObject.accumulate("device_id", deviceId);
                jsonObject.accumulate("bssid", ap.bssid);
                jsonObject.accumulate("level", ap.level);
                jsonObject.accumulate("capabilities", ap.capabilities);
                jsonObject.accumulate("timestamp", ap.timestamp);

                jsonArray.put(jsonObject);
            }

            String json = jsonArray.toString();
            Log.d("JSON", json);
            return json;
        }
        catch (JSONException e){
            return "";
        }
    }
}