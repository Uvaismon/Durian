package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

public class ViewNote extends AppCompatActivity {

    TextView noteTitleBar, noteLabel, noteContent;
    Button editButton, deleteButton;
    DbHelper notesDbHelper;
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

        noteTitleBar.setText(noteView.getStringExtra(DbHelper.TITLE));
        noteLabel.setText(noteView.getStringExtra(DbHelper.LABEL_NAME));
        noteContent.setText(noteView.getStringExtra(DbHelper.CONTENTS));

        editButton.setOnClickListener(
                v -> {
                    Intent editIntent = new Intent(this, CreateNote.class);
                    editIntent.setData(Uri.parse(noteView.getStringExtra(DbHelper.TIMESTAMP)));
                    startActivity(editIntent);
                }
        );

        deleteButton.setOnClickListener(
                v -> {
                    deleteNote(noteView.getStringExtra(DbHelper.TIMESTAMP));
                }
        );

        noteContent.setMovementMethod(new ScrollingMovementMethod());
    }

    public void deleteNote(String timestamp) {
        notesDbHelper = new DbHelper(this);
        notesDb = notesDbHelper.getWritableDatabase();

        notesDb.delete(DbHelper.NOTES_TABLE_NAME,
                DbHelper.TIMESTAMP + "=?",
                new String[]{timestamp});
        startActivity(new Intent(this, MainActivity.class));
    }

}