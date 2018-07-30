package com.projectbox.uploadfile.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.projectbox.uploadfile.model.DetailsModel;
import com.projectbox.uploadfile.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionItemAdapter extends RecyclerView.Adapter<QuestionItemAdapter.ViewHolder> {
    private List<DetailsModel.QuestionsBean> questions = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DetailsModel.QuestionsBean bean = questions.get(position);
        holder.editText.setHint(bean.getHint());

        switch (bean.getType()) {
            case "text":
                holder.editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case "textNumeric":
                holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }

        if (bean.getValidation() != null) {
            holder.editText.setFilters(new InputFilter[]{new InputFilter() {

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    Log.d("ChatSequence", (dest.toString() + source.toString()));
                    int lenght = bean.getValidation().getSize();
                    if ((source.toString() + dest.toString()).length() != lenght) {
                        holder.editText.requestFocus();
                        holder.editText.setError("wrong input");
                    }
                    return null;
                }
            }, new InputFilter.LengthFilter(bean.getValidation().getSize())});
        }

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void setData(List<DetailsModel.QuestionsBean> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private EditText editText;

        public ViewHolder(View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.item);
        }
    }
}
