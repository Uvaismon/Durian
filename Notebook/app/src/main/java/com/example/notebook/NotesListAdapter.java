package com.example.notebook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.MyViewHolder> {

    private final Context context;
    private final String[] title;
    private final String[] content;
    private final String[] timestamp;

    NotesListAdapter(Context context, String[] title, String[] contents, String[] timestamp) {
        this.context = context;
        this.title = title;
        this.content = contents;
        this.timestamp = timestamp;
    }

    View.OnClickListener test = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Card click event", "onClick: Card clicked");
        }
    };

    @NonNull
    @Override
    public NotesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notes_card, parent, false);
        view.setOnClickListener(test);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesListAdapter.MyViewHolder holder, int position) {
        holder.titleView.setText(title[position].toString());
        holder.contentView.setText(content[position].toString());
        holder.timestampView.setText(timestamp[position].toString());
    }

    @Override
    public int getItemCount() {
        return title.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleView, contentView, timestampView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.cardTitleBar);
            contentView = itemView.findViewById(R.id.cardContent);
            timestampView = itemView.findViewById(R.id.cardTimestamp);
        }
    }
}
