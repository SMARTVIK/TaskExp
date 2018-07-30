package com.projectbox.uploadfile.activity;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.projectbox.uploadfile.Helper;
import com.projectbox.uploadfile.adapter.ImagesAdapter;
import com.projectbox.uploadfile.database.Image;
import com.projectbox.uploadfile.database.ImageViewModel;
import com.projectbox.uploadfile.listener.PostImageService;
import com.projectbox.uploadfile.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageUploadActivity extends AppCompatActivity {

    private static final int CAPTURE_FROM_CAMERA = 101;
    private static final int SELECTE_GALLERY_PERMISSION = 200;
    private static final int CAPTURE_FROM_CAMERA_PERMISSION = 201;
    private ImageViewModel imageViewModel;
    public static final int PICK_IMAGE = 100;
    private static final String TAG = ImageUploadActivity.class.getSimpleName();
    private static final String BASE_URL = "http://192.168.1.37:3000"; //my local server
    PostImageService service;
    private ImagesAdapter imagesAdapter;
    private File cameraImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        setUpImagesList();
        imageViewModel.getAllImages().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable List<Image> images) {
                imagesAdapter.setData(images);
                tryToUpload();
            }
        });

        OkHttpClient client = new OkHttpClient.Builder()/*.addInterceptor(interceptor)*/.build();
        // Change base URL to your upload server URL.
        service = new Retrofit.Builder().baseUrl(BASE_URL).client(client).build().create(PostImageService.class);
        findViewById(R.id.select_from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ImageUploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ImageUploadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SELECTE_GALLERY_PERMISSION);
                    return;
                }
                selectMultipleFromGallery();
            }
        });

        findViewById(R.id.capture_from_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ImageUploadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(ImageUploadActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ImageUploadActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CAPTURE_FROM_CAMERA_PERMISSION);
                    return;
                }
                captureMultiplePhotos();
            }
        });

        findViewById(R.id.go_to_json_parser_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageUploadActivity.this, JsonParserActivity.class));
            }
        });
    }

    private void setUpImagesList() {
        RecyclerView questionsList = findViewById(R.id.list_of_images);
        questionsList.addItemDecoration(new SpacesItemDecoration(8));
        questionsList.setNestedScrollingEnabled(false);
        StaggeredGridLayoutManager linearLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        questionsList.setLayoutManager(linearLayoutManager);
        imagesAdapter = new ImagesAdapter(this);
        questionsList.setAdapter(imagesAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SELECTE_GALLERY_PERMISSION && grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            selectMultipleFromGallery();
        }else if(requestCode == SELECTE_GALLERY_PERMISSION && grantResults.length > 1){
            captureMultiplePhotos();
        }
    }

    private void selectMultipleFromGallery() {
        Intent intent = null;
        intent = new Intent();
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "select_image"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult called");
        if (requestCode == PICK_IMAGE) {
            onSelectionFromGallery(requestCode, resultCode, data);
        }else {
            onCameraPhotoTaken(requestCode, resultCode, data);
        }
    }

    private void onCameraPhotoTaken(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            Image image = new Image(cameraImageFile.getPath(),cameraImageFile.getPath(),false);
            imageViewModel.insert(image);
        }
    }

    private void onSelectionFromGallery(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.getClipData() != null) { //hey buddy , you selected multiple images , now handle this
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    Image image = new Image(imageUri.getPath(), imageUri.getPath(), false);
                    imageViewModel.insert(image);
                }
            } else if (data.getData() != null) { //hey buddy , you selected single image , now handle this
                Uri selectedImage = data.getData();
                Image image = new Image(selectedImage.getPath(), selectedImage.getPath(), false);
                imageViewModel.insert(image);

            }
        }
    }

    private void captureMultiplePhotos() {
        //currently capturing single photo at a time
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraImageFile = new File(getExternalCacheDir(), "camera_temp" + System.currentTimeMillis() + Helper.getInstance().getLocation() != null ? Helper.getInstance().getLocation().getLatitude() + " " + Helper.getInstance().getLocation().getLongitude() : "");
        if (cameraImageFile.exists())
            cameraImageFile.delete();
        try {
            cameraImageFile.createNewFile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, getApplicationContext().getPackageName()
                    + ".provider", cameraImageFile));
            startActivityForResult(intent, CAPTURE_FROM_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage(final Image selectedImage) {
        File file = getFile(selectedImage);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        try {
            Call<ResponseBody> req = service.postImage(body, name);

            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("result ", "Successfully uploaded");
                    selectedImage.setUploaded(true);
                    imageViewModel.update(selectedImage);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            Log.e("fat gaya ", "exception");
        }
    }

    private File getFile(Image selectedImage) {
        return new File(selectedImage.getImagePath().contains("/raw")?selectedImage.getImagePath().replace("/raw",""):selectedImage.getImagePath());
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkChange, intentFilter1); //registering receiver to upload images when we get internet
    }

    private BroadcastReceiver networkChange = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isInternetConnected(context) && imageViewModel.getAllImages().getValue() != null && !imageViewModel.getAllImages().getValue().isEmpty()) {
                Log.d("Internet", "hey I am connected");
                tryToUpload();
            }
        }
    };

    private void tryToUpload() {
        LiveData<List<Image>> imageLiveData = imageViewModel.getAllImages();
        if (imageLiveData.getValue() != null && !imageLiveData.getValue().isEmpty()) {
            List<Image> images = imageLiveData.getValue();
            for (Image image : images) {
                if (!image.isUploaded()) {
                    uploadImage(image);
                }
            }
        }
    }

    private boolean isInternetConnected(Context mContext) {
        if (mContext == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
