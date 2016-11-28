package com.stu.fei.mobv;

/**
 * Created by roman on 11/27/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

public class registerAPs extends AsyncTask<Void, Void, String> {
    public static List<AccessPoint> apsToRegister;

    int locationId;
    List<AccessPoint> accessPoints;
    WeakReference<Activity> weakActivity;

    public registerAPs(int locationIdSrc, List<AccessPoint> accessPointsSrc, Activity activity) {
        locationId = locationIdSrc;
        accessPoints = accessPointsSrc;
        weakActivity = new WeakReference<Activity>(activity);
    }

    @Override
    protected String doInBackground(Void... params) {
        return POST("http://mobv-server.visi.sk/api/v1/location/"
                        + Integer.toString(locationId)
                        + "/access-points"
                , apsToRegister);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
//        Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private static String POST(String url, List<AccessPoint> aps) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            String json;

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < aps.size(); ++i) {
                AccessPoint ap = aps.get(i);

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("ssid", ap.ssid);
                jsonObject.accumulate("bssid", ap.bssid);
                jsonObject.accumulate("level", ap.level);
                jsonObject.accumulate("capabilities", ap.capabilities);
                jsonObject.accumulate("timestamp", ap.timestamp);

                jsonArray.put(jsonObject);
            }

            // 4. convert JSONObject to JSON to String
            json = jsonArray.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }
}
