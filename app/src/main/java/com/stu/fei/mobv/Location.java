package com.stu.fei.mobv;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by roman on 11/28/16.
 */

public class Location {
    public int id;
    public String block;
    public String level;
    public String updated_at;

    private List<AccessPoint> accessPoints = new LinkedList<>();

    /// Pouziva sa pri navrhoch lokality
    public int matchCount;

    public String getFormattedUpdatedAt(){

        if(updated_at == null){
            return null;
        }
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date =  df.parse(updated_at);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 1);
            date = calendar.getTime();

            SimpleDateFormat dtfResult = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            return dtfResult.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getName() {
        String name = new String("Blok");
        name += " " + block;
        if (level == "0") {
            name += " - prízemie";
        }
        else {
            name += " - " + level + ". poschodie";
        }

        return name;
    }

    public boolean exist(AccessPoint accessPoint){
        for(AccessPoint ap: accessPoints){
            Log.v("Location", ap.toString());
            if(Objects.equals(ap.bssid, accessPoint.bssid)){
                return true;
            }
        }
        return false;
    }

    public boolean add(AccessPoint accessPoint){
        return accessPoints.add(accessPoint);
    }

    public void removeAll(){
        accessPoints = new LinkedList<>();
    }

    public boolean remove(AccessPoint accessPoint){

        return accessPoints.remove(accessPoint);
    }

    public AccessPoint remove(int index){
        return accessPoints.remove(index);
    }

    public List getAccessPoints(){
        return accessPoints;
    }
}
