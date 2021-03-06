package com.stu.fei.mobv;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Michal on 01.12.2016.
 */

public class WebService {

    RepositoryAPs repositoryAPs = Repository.getInstance(RepositoryAPs.class);

    interface OnSuggestionsReceived {
        public void onSuccess(List<Location> list);

        public void onFailure(String responseString);
    }

    interface OnAccessPointsReceived {
        public void onSuccess(List<AccessPoint> list);
        public void onFailure();
    }

    interface OnLocationsReceived {
        public void onSuccess(List<Location> list);
        public void onFailure();
    }

    interface OnLocationReceived {
        public void onSuccess(Location location);
        public void onFailure();
    }

    interface OnAccessPointsRegistered {
        public void onSuccess();
        public void onFailure();
    }

    interface OnAccessPointRemoved {
        public void onSuccess();

        public void onFailure();
    }


    private final String BASE_PATH = "http://mobv-server.visi.sk/api/v1/";

    private static WebService ws = null;
    private Context context;

    AsyncHttpClient client = new AsyncHttpClient();

    private WebService(Context context) {
        this.context = context;
    }

    public static WebService getInstance(Context context) {

        if (ws == null) {
            Log.v("WS", "WebService new instance");
            ws = new WebService(context);
        }

        return ws;
    }

    public boolean isOnline() {
        if (context != null) {

            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            return netInfo != null && netInfo.isConnectedOrConnecting();
        } else {
            Log.v("WS", "offline");
            return false;
        }
    }

    public void getSuggestion(List<AccessPoint> accessPoints, final OnSuggestionsReceived handler) {
        if (!isOnline()) {
            Toast t = Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT);
            t.show();
        }

        JSONArray arr = new JSONArray();
        for (AccessPoint ap : accessPoints) {
            arr.put(ap.bssid);
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(arr.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(context, BASE_PATH + "locations/find", entity, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        List<Location> listSuggestions = new LinkedList<Location>();
                        try {
                            JSONArray suggestions = response.getJSONArray("suggestions");
                            for (int i = 0; i < suggestions.length(); ++i) {
                                JSONObject suggestionRoot = suggestions.getJSONObject(i);

                                JSONObject suggestion = suggestionRoot.getJSONObject("location");

                                Location location = new Location();
                                location.id = suggestion.getInt("id");
                                location.block = suggestion.getString("block");
                                location.level = suggestion.getString("level");
                                location.matchCount = suggestionRoot.getInt("match_count");

                                JSONArray accessPointsArr = suggestion.getJSONArray("access_points");
                                for (int j = 0; j < accessPointsArr.length(); ++j) {
                                    JSONObject apObj = accessPointsArr.getJSONObject(j);
                                    AccessPoint ap = AccessPoint.createNew(apObj);
                                    if (ap != null) {
                                        location.add(ap);
                                    }
                                }

                                listSuggestions.add(location);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        repositoryAPs.setSuggestions(listSuggestions);
                        handler.onSuccess(listSuggestions);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.v("WS", "onFailure");
                        Log.v("WS", "statusCode " + statusCode);
                        Log.v("WS", "res " + responseString);
                        handler.onFailure(responseString);
                    }
                }
        );
    }

    public void getLocations(final OnLocationsReceived handler) {
        if (!isOnline()) {
            Toast t = Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT);
            t.show();
        }

        client.get(context, BASE_PATH + "locations", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                List<Location> listLocations = new LinkedList<Location>();
                for (int i = 0; i < response.length(); ++i) {

                    try {
                        JSONObject locationObj = response.getJSONObject(i);
                        Location location = new Location();
                        location.id = locationObj.getInt("id");
                        location.block = locationObj.getString("block");
                        location.level = locationObj.getString("level");
                        location.updated_at = locationObj.getString("updated_at");

                        listLocations.add(location);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                handler.onSuccess(listLocations);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.v("WS", "onFailure");
                Log.v("WS", "statusCode " + statusCode);
                Log.v("WS", "res " + responseString);
                handler.onFailure();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject){
                Log.v("WS", "onFailure");
                Log.v("WS", "statusCode " + statusCode);
                Log.v("WS", "res " + jsonObject);
                handler.onFailure();
            }
        });

    }

    public void getLocation(Integer locationId, final OnLocationReceived handler) {

        if (!isOnline()) {
            Toast t = Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT);
            t.show();
        }

        client.get(context, BASE_PATH + "locations/" + locationId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Location location = new Location();
                    location.id = response.getInt("id");
                    location.block = response.getString("block");
                    location.level = response.getString("level");
                    location.updated_at = response.getString("updated_at");

                    JSONArray accessPointsArr = response.getJSONArray("access_points");
                    for (int j = 0; j < accessPointsArr.length(); ++j) {
                        JSONObject apObj = accessPointsArr.getJSONObject(j);
                        AccessPoint ap = AccessPoint.createNew(apObj);
                        if (ap != null) {
                            location.add(ap);
                        }
                    }

                    handler.onSuccess(location);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.v("WS", "onFailure");
                Log.v("WS", "statusCode " + statusCode);
                Log.v("WS", "res " + responseString);
                handler.onFailure();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject){
                Log.v("WS", "onFailure");
                Log.v("WS", "statusCode " + statusCode);
                Log.v("WS", "res " + jsonObject);
                handler.onFailure();
            }
        });
    }

    public void removeAccessPoint(Integer locationId, Integer accessPointId, final OnAccessPointRemoved handler) {

        if (!isOnline()) {
            Toast t = Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT);
            t.show();
            handler.onFailure();
        }

        client.delete(context, BASE_PATH + "locations/" + locationId + "/access-points/" + accessPointId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        handler.onSuccess();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.v("WS", "onFailure");
                        Log.v("WS", "statusCode " + statusCode);
                        Log.v("WS", "res " + responseString);
                        handler.onFailure();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject){
                        Log.v("WS", "onFailure");
                        Log.v("WS", "statusCode " + statusCode);
                        Log.v("WS", "res " + jsonObject);
                        handler.onFailure();
                    }
                }
        );
    }

    public void registerAccessPointsForLocation(List<AccessPoint> accessPoints, Integer locationId, final OnAccessPointsRegistered handler) {
        if (!isOnline()) {
            Toast t = Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT);
            t.show();
        }

        String devideId = "";

        JSONArray jsonArray = new JSONArray();
        for (AccessPoint ap : accessPoints) {
            JSONObject jsonObject = ap.toJSONObject(devideId);
            if (jsonObject != null) {
                jsonArray.put(jsonObject);
            }
        }
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonArray.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(context, BASE_PATH + "locations/" + locationId + "/access-points", entity, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        handler.onSuccess();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                        Log.v("WS", "onFailure");
                        Log.v("WS", "statusCode " + statusCode);
                        Log.v("WS", "res " + jsonObject.toString());
                        handler.onFailure();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.v("WS", "onFailure");
                        Log.v("WS", "statusCode " + statusCode);
                        Log.v("WS", "res " + responseString);
                        handler.onFailure();
                    }

                }
        );

    }
}
