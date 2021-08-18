package com.example.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LabelListAdapter extends RecyclerView.Adapter<LabelListAdapter.ViewHolder> {

    private final Context context;
    private final String[] labelName;
    private final LabelClickListener labelClickListener;

    LabelListAdapter(Context context, String[] labelName, LabelClickListener labelClickListener) {
        this.context = context;
        this.labelName = labelName;
        this.labelClickListener = labelClickListener;
    }

    @NonNull
    @Override
    public LabelListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.label_card, parent, false);
        return new ViewHolder(view, labelClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelListAdapter.ViewHolder holder, int position) {
        holder.labelNameView.setText(labelName[position]);
    }

    @Override
    public int getItemCount() {
        return labelName.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView labelNameView;
        LabelClickListener labelClickListener;

        public ViewHolder(@NonNull View itemView, LabelClickListener labelClickListener) {
            super(itemView);
            labelNameView = itemView.findViewById(R.id.labelName);
            this.labelClickListener = labelClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            labelClickListener.onClick(labelNameView.getText().toString());
        }
    }

    public interface LabelClickListener {
        void onClick(String element);
    }
}
