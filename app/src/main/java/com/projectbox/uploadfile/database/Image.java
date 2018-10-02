package com.projectbox.uploadfile.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "image_table")
public class Image {

    public Image() {

    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")

    private String id;

    @ColumnInfo(name = "imagePath")
    private String imagePath;

    @ColumnInfo(name = "isUploaded")
    private boolean isUploaded;

    public Image(String id,String word,boolean isUploaded) {
        this.id = id;
        this.imagePath = word;
        this.isUploaded = isUploaded;
    }

}