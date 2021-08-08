package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class ViewNote extends AppCompatActivity {

    TextView noteTitleBar, noteLabel, noteContent;
    Button editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        noteTitleBar = findViewById(R.id.noteTitleBar);
        noteLabel = findViewById(R.id.noteLabel);
        noteContent = findViewById(R.id.noteContent);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        Intent noteView = getIntent();

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
    }
}