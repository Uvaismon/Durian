package com.example.notebook;

import android.content.Context;
import android.provider.ContactsContract;
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
    private final String[] label;
    private final NoteClickListener notesClickListener;

    NotesListAdapter(Context context, String[] title, String[] contents, String[] timestamp,
                     String[] label, NoteClickListener notesClickListener) {
        this.context = context;
        this.title = title;
        this.content = contents;
        this.timestamp = timestamp;
        this.label = label;
        this.notesClickListener = notesClickListener;
    }

    @NonNull
    @Override
    public NotesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notes_card, parent, false);
        return new MyViewHolder(view, notesClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesListAdapter.MyViewHolder holder, int position) {
        holder.titleView.setText(title[position]);
        holder.contentView.setText(content[position]);
        holder.timestampView.setText(timestamp[position]);
        holder.labelView.setText(label[position]);
    }

    @Override
    public int getItemCount() {
        return title.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleView, contentView, timestampView, labelView;
        NoteClickListener noteClickListener;

        public MyViewHolder(@NonNull View itemView, NoteClickListener noteClickListener) {
            super(itemView);
            titleView = itemView.findViewById(R.id.cardTitleBar);
            contentView = itemView.findViewById(R.id.cardContent);
            timestampView = itemView.findViewById(R.id.cardTimestamp);
            labelView = itemView.findViewById(R.id.cardLabel);
            this.noteClickListener = noteClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            noteClickListener.onClick(timestampView.getText().toString());
        }
    }

    public interface NoteClickListener {
        void onClick(String position);
    }
}
