package com.stu.fei.mobv;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michal on 29.11.2016.
 */

public class RepositoryCheckedAP {

    private static RepositoryCheckedAP instance = null;

    private List<AccessPoint> listCheckedAP = null;

    protected RepositoryCheckedAP(){
        listCheckedAP = new LinkedList<>();
    }
    public static RepositoryCheckedAP getInstance(){
        if(instance == null) {
            instance = new RepositoryCheckedAP();
        }
        return instance;
    }

    public boolean isAPChecked(AccessPoint accessPoint){
        return listCheckedAP.contains(accessPoint);
    }

    public boolean addCheckedAP(AccessPoint accessPoint){
        return listCheckedAP.add(accessPoint);
    }

    public void removeAllCheckedAP(){
        listCheckedAP = new LinkedList<>();
    }

    public boolean removeCheckedAP(AccessPoint accessPoint){
        return listCheckedAP.remove(accessPoint);
    }

    public AccessPoint removeCheckedAP(int index){
        return listCheckedAP.remove(index);
    }

    public List getListCheckedAPs(){
        return listCheckedAP;
    }
}
