package com.projectbox.uploadfile;

import android.location.Location;

public class Helper {

    private Helper(){

    }

    private static Helper helper;

    public static Helper getInstance() {
        if (helper == null) {
            helper = new Helper();
        }
        return helper;
    }

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
