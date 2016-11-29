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

    public boolean add(AccessPoint accessPoint){
        Log.v(TAG, "ADD | AP = " + accessPoint);
        return list.add(accessPoint);
    }

    public void removeAll(){
        list = new LinkedList<>();
    }

    public boolean remove(AccessPoint accessPoint){
        return list.remove(accessPoint);
    }

    public AccessPoint remove(int index){
        return list.remove(index);
    }

    public List getList(){
        return list;
    }
}
