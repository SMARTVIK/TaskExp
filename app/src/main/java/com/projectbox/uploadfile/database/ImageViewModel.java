package com.projectbox.uploadfile.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ImageViewModel extends AndroidViewModel {

    private ImageRepository mRepository;

    private LiveData<List<Image>> mAllImages;

    public ImageViewModel(Application application) {
        super(application);
        mRepository = new ImageRepository(application);
        mAllImages = mRepository.getAllImages();
    }

    public LiveData<List<Image>> getAllImages() {
        return mAllImages;
    }

    public void insert(Image image) {
        mRepository.insert(image);
    }

    public void update(Image selectedImage) {
        mRepository.update(selectedImage);
    }
}
