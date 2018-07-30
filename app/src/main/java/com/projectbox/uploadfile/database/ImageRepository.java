package com.projectbox.uploadfile.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ImageRepository {

   private ImageDao mWordDao;
   private LiveData<List<Image>> mAllImages;

   ImageRepository(Application application) {
       ImageDb db = ImageDb.getDatabase(application);
       mWordDao = db.imageDao();
       mAllImages = mWordDao.getAllImages();
   }

   LiveData<List<Image>> getAllImages() {
       return mAllImages;
   }


    public void insert(Image word) {
        new insertAsyncTask(mWordDao).execute(word);
    }

    public void update(Image word) {
        new updateTask(mWordDao).execute(word);
    }

   private static class insertAsyncTask extends AsyncTask<Image, Void, Void> {

       private ImageDao mAsyncTaskDao;

       insertAsyncTask(ImageDao dao) {
           mAsyncTaskDao = dao;
       }

       @Override
       protected Void doInBackground(final Image... params) {
           mAsyncTaskDao.insert(params[0]);
           return null;
       }
   }

    private static class updateTask extends AsyncTask<Image, Void, Void> {

        private ImageDao mAsyncTaskDao;

        updateTask(ImageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Image... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}