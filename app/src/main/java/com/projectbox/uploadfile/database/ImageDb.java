package com.projectbox.uploadfile.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Image.class}, version = 1,exportSchema = false)
public abstract class ImageDb extends RoomDatabase {
    public abstract ImageDao imageDao();

    private static ImageDb INSTANCE;

    public static ImageDb getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (ImageDb.class) {

                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ImageDb.class, "image_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ImageDao mDao;

        PopulateDbAsync(ImageDb db) {
            mDao = db.imageDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
//            mDao.deleteAll();
            return null;
        }
    }

}