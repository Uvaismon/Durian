package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

public class ViewNote extends AppCompatActivity {

    TextView noteTitleBar, noteLabel, noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        noteTitleBar = findViewById(R.id.noteTitleBar);
        noteLabel = findViewById(R.id.noteLabel);
        noteContent = findViewById(R.id.noteContent);

        Intent noteView = getIntent();

        noteTitleBar.setText(noteView.getStringExtra(NotesDbHelper.TITLE));
        noteLabel.setText(noteView.getStringExtra(LabelDbHelper.LABEL_NAME));
        noteContent.setText(noteView.getStringExtra(NotesDbHelper.CONTENTS));
    }
}