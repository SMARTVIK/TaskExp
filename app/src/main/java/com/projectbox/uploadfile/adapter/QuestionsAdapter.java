package com.projectbox.uploadfile.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projectbox.uploadfile.model.DetailsModel;
import com.projectbox.uploadfile.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<DetailsModel> results = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetailsModel detailsModel = results.get(position);
        holder.title.setText(detailsModel.getHeader());
        holder.questionsAdapter.setData(detailsModel.getQuestions());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setData(List<DetailsModel> detailsModels) {
        this.results = detailsModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private RecyclerView questionsList;
        private QuestionItemAdapter questionsAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            questionsList = itemView.findViewById(R.id.items_list);
            questionsList.setNestedScrollingEnabled(false);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            questionsList.setLayoutManager(linearLayoutManager);
            questionsAdapter = new QuestionItemAdapter();
            questionsList.setAdapter(questionsAdapter);
        }
    }
}
