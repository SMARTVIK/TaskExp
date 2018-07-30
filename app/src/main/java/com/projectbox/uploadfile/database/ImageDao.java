package com.projectbox.uploadfile.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ImageDao {

   @Insert
   void insert(Image word);

   @Query("DELETE FROM image_table")
   void deleteAll();

   @Query("SELECT * from image_table")
   LiveData<List<Image>> getAllImages();

   @Delete
   void deleteImage(Image image);

   @Update
   void update(Image word);
}