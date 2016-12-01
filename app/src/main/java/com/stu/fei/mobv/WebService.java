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

    interface OnSuggestionsReceived{
        public void onSuccess(List<Location> list);
        public void onFailure(String responseString);
    }

    interface OnAccessPointsReceived{
        public void onSuccess(List<AccessPoint> list);
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
        if(context != null){

            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        else {
            Log.v("WS", "offline");
            return false;
        }
    }

    public void getSuggestion(List<AccessPoint> accessPoints, final OnSuggestionsReceived handler){
        if (!isOnline())
        {
            Toast t = Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT);
            t.show();
        }

        JSONArray arr = new JSONArray();
        for(AccessPoint ap: accessPoints){
            arr.put(ap.bssid);
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(arr.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(context, BASE_PATH + "locations/find", entity, "application/json" ,new JsonHttpResponseHandler() {
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
                                listSuggestions.add(location);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
}
