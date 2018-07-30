package com.projectbox.uploadfile.locationmodule;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocationStringModel {


    /**
     * location-update-result : One location reported: Jul 28, 2018 2:16:19 PM
     (28.5056473, 77.1789946)

     */

    @com.google.gson.annotations.SerializedName("location-update-result")
    private String locationupdateresult;

    public static List<LocationStringModel> arrayLocationStringModelFromData(String str) {

        Type listType = new com.google.gson.reflect.TypeToken<ArrayList<LocationStringModel>>() {
        }.getType();

        return new com.google.gson.Gson().fromJson(str, listType);
    }

    public String getLocationupdateresult() {
        return locationupdateresult;
    }

    public void setLocationupdateresult(String locationupdateresult) {
        this.locationupdateresult = locationupdateresult;
    }
}
