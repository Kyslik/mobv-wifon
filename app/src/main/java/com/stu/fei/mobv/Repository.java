package com.stu.fei.mobv;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Michal on 29.11.2016.
 */

public class Repository {

    private static final String TAG = "REPOSITORY";

    private static Map<Class<? extends Repository>,Repository> INSTANCES_MAP = new java.util.HashMap<Class<? extends Repository>, Repository>();

    private List<AccessPoint> list = null;
    private Location clickedLocation = null;
    private List<Location> suggestions = null;

    private List<Location> locationlist = null;

    interface OnChangeListener{
        public void onChange(List<AccessPoint> list);
    }

    List<OnChangeListener> handlers = new LinkedList<>();

    public void registerOnChangeListener(OnChangeListener listener){
        handlers.add(listener);
    }
    public void triggerOnChange(List list){
        for(OnChangeListener handler: handlers){

            handler.onChange(list);
        }
    }

    protected Repository(){
        list = new LinkedList<>();
    }
    public static <E extends Repository> E getInstance(Class<E> instanceClass)  {
        if(INSTANCES_MAP.containsKey(instanceClass)) {
            return (E) INSTANCES_MAP.get(instanceClass);
        } else {
            E instance = null;
            try {
                instance = instanceClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            INSTANCES_MAP.put(instanceClass, instance);
            return instance;
        }
    }

    public boolean exist(AccessPoint accessPoint){
        Log.v(TAG, "EXIST | AP = " + accessPoint);
        for(AccessPoint ap: list){
            if(Objects.equals(ap.bssid, accessPoint.bssid)){
                return true;
            }
        }
        return false;
//        return list.contains(accessPoint);
    }

    public void setLocationList(List locationlist){
        this.locationlist = this.locationlist;
    }

    public List getLocationList(){
        return locationlist;
    }

    public void setClickedLocation(Location location){
        clickedLocation = location;
    }

    public Location getClickedLocation(){
        return clickedLocation;
    }

    public void setSuggestions(List suggestions){
        this.suggestions = suggestions;
    }

    public List getSuggestions(){
        return suggestions;
    }

    public boolean add(AccessPoint accessPoint){
        Log.v(TAG, "ADD | AP = " + accessPoint);
        boolean result = list.add(accessPoint);

        triggerOnChange(list);
        return result;
    }

    public void removeAll(){
        list = new LinkedList<>();

        triggerOnChange(list);
    }

    public boolean remove(AccessPoint accessPoint){

        boolean result = list.remove(accessPoint);
        triggerOnChange(list);
        return result;
    }

    public AccessPoint remove(int index){
        AccessPoint result = list.remove(index);
        triggerOnChange(list);
        return result;
    }

    public List getList(){
        return list;
    }
}
