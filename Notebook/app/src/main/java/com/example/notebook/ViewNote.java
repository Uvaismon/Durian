package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ViewNote extends AppCompatActivity {

    TextView noteTitleBar, noteLabel, noteContent;
    Button editButton, deleteButton;
    NotesDbHelper notesDbHelper;
    SQLiteDatabase notesDb;
    Intent noteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        noteTitleBar = findViewById(R.id.noteTitleBar);
        noteLabel = findViewById(R.id.noteLabel);
        noteContent = findViewById(R.id.noteContent);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        noteView = getIntent();

        noteTitleBar.setText(noteView.getStringExtra(NotesDbHelper.TITLE));
        noteLabel.setText(noteView.getStringExtra(LabelDbHelper.LABEL_NAME));
        noteContent.setText(noteView.getStringExtra(NotesDbHelper.CONTENTS));

        editButton.setOnClickListener(
                v -> {
                    Intent editIntent = new Intent(this, CreateNote.class);
                    editIntent.setData(Uri.parse(noteView.getStringExtra(NotesDbHelper.TIMESTAMP)));
                    startActivity(editIntent);
                }
        );

        deleteButton.setOnClickListener(
                v -> {
                    deleteNote(noteView.getStringExtra(NotesDbHelper.TIMESTAMP));
                }
        );
    }

    public void deleteNote(String timestamp) {
        notesDbHelper = new NotesDbHelper(this);
        notesDb = notesDbHelper.getWritableDatabase();

        notesDb.delete(NotesDbHelper.TABLE_NAME,
                NotesDbHelper.TIMESTAMP + "=?",
                new String[]{timestamp});
        startActivity(new Intent(this, MainActivity.class));
    }

}