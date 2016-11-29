package com.stu.fei.mobv;

/**
 * Created by roman on 11/28/16.
 */

public class Location {
    public int id;
    public String block;
    public String level;

    /// Pouziva sa pri navrhoch lokality
    public int matchCount;

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
}
