package com.projectbox.uploadfile.vidstatus;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.projectbox.uploadfile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoStatusActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ValueAnimator widthAnimator;
    private AnimatorSet buttonAnimator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_status);
        getLatestVideoStatus();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        findViewById(R.id.button).post(new Runnable() {
            @Override
            public void run() {
                int width = findViewById(R.id.button).getWidth();
                widthAnimator = ValueAnimator.ofInt(width, 2 * width);
                widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int animatedValue = (int) animation.getAnimatedValue();
                        Button button = findViewById(R.id.button);
                        button.getLayoutParams().width = animatedValue;
                        button.requestLayout();
                    }
                });
                widthAnimator.setDuration(500);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
//        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
                  widthAnimator.start();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getLatestVideoStatus() {
       /* findViewById(R.id.check_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api api = new StatusApi().getStatusApi();
                Call<Status> req = api.getLatestVideoStatus(1);
                req.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        Log.d("onResponse", response.isSuccessful() + "");
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        Log.d("onFailure", t.getMessage() + "");
                    }
                });
            }
        });*/
    }
}
