/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.projectbox.uploadfile.locationmodule;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;

import com.projectbox.uploadfile.R;
import com.projectbox.uploadfile.activity.LocationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Class to process location results.
 */
public class ResultsHelper {

    public final static String PREFERENCE_KEY = "location-update-result";

    final private static String PRIMARY_CHANNEL = "default";
    private static final String LAST_LOCATION = "last_known_location";


    private Context mContext;
    private List<Location> mLocations;
    private NotificationManager mNotificationManager;

    ResultsHelper(Context context, List<Location> locations) {
        mContext = context;
        mLocations = locations;

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(PRIMARY_CHANNEL,
                    context.getString(R.string.default_channel), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getNotificationManager().createNotificationChannel(channel);
        }
    }

    /**
     * Returns the title for reporting about a list of {@link Location} objects.
     */
    private String getLocationResultTitle() {
        return  mLocations.size() + ": " + DateFormat.getDateTimeInstance().format(new Date());
    }

    private String getLocationResultText() {
        if (mLocations.isEmpty()) {
            return mContext.getString(R.string.unknown_location);
        }
        StringBuilder sb = new StringBuilder();
        for (Location location : mLocations) {
            sb.append("(");
            sb.append(location.getLatitude());
            sb.append(", ");
            sb.append(location.getLongitude());
            sb.append(")");
            sb.append("\n");
        }
        return sb.toString();
    }

    void saveResults() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
            try {
                if(!getSavedLocationResult(mContext).equals("")){
                    jsonArray = new JSONArray(getSavedLocationResult(mContext));
                }
                jsonObject.put(PREFERENCE_KEY,getLocationResultTitle() + "\n" +
                        getLocationResultText());
                jsonArray.put(jsonObject);

                PreferenceManager.getDefaultSharedPreferences(mContext)
                        .edit()
                        .putString(PREFERENCE_KEY, jsonArray.toString())
                        .apply();
                saveLastLocation()
                        .apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    private SharedPreferences.Editor saveLastLocation() {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(LAST_LOCATION, getLocationResultTitle() + "\n" +
                        getLocationResultText());
    }

    public static String getLastLocation(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LAST_LOCATION,"");
    }

    public static String getSavedLocationResult(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREFERENCE_KEY, "");
    }

    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mContext.getSystemService(
                    Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    void showNotification() {
        Intent notificationIntent = new Intent(mContext, LocationActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(LocationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(mContext, PRIMARY_CHANNEL);
        } else {
            notificationBuilder = new Notification.Builder(mContext)
                    .setContentTitle(getLocationResultTitle())
                    .setContentText(getLocationResultText())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(notificationPendingIntent);
        }
        getNotificationManager().notify(0, notificationBuilder.build());
    }
}
