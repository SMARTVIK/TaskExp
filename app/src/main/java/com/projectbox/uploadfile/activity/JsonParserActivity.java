package com.projectbox.uploadfile.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.projectbox.uploadfile.model.DetailsModel;
import com.projectbox.uploadfile.adapter.QuestionsAdapter;
import com.projectbox.uploadfile.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonParserActivity extends AppCompatActivity {

    private QuestionsAdapter questionsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setUpQuestionsList();
        findViewById(R.id.parse_json).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = loadJSONFromAsset(JsonParserActivity.this);
                List<DetailsModel> detailsModels = DetailsModel.arrayDetailsModelFromData(json);
                if (detailsModels != null && !detailsModels.isEmpty()) {
                    questionsAdapter.setData(detailsModels);
                }
            }
        });
    }


    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("temp.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void setUpQuestionsList() {
        RecyclerView questionsList = findViewById(R.id.questions_container);
        questionsList.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        questionsList.setLayoutManager(linearLayoutManager);
        questionsAdapter = new QuestionsAdapter();
        questionsList.setAdapter(questionsAdapter);
    }
}
