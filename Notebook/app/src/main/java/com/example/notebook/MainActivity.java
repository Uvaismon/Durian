package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements NotesListAdapter.NoteClickListener {

    NotesDbHelper notesDbHelper;
    SQLiteDatabase notesDb;
    String[] title, contents, timestamp;
    NotesListAdapter notesListAdapter;
    RecyclerView mainRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRecyclerView = findViewById(R.id.mainRecyclerView);


        notesDbHelper = new NotesDbHelper(this);
        notesDb = notesDbHelper.getReadableDatabase();

        loadNotesList();

        notesListAdapter = new NotesListAdapter(MainActivity.this, title, contents
                , timestamp, this);
        mainRecyclerView.setAdapter(notesListAdapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    private void loadNotesList() {
        String[] projections = {NotesDbHelper.TITLE, NotesDbHelper.CONTENTS,
                NotesDbHelper.TIMESTAMP};

        Cursor c = notesDb.query(NotesDbHelper.TABLE_NAME, projections, null,
                null, null ,null, NotesDbHelper.TIMESTAMP);

        title = new String[c.getCount()];
        contents = new String[c.getCount()];
        timestamp = new String[c.getCount()];

        for (int i = 0; c.moveToNext(); i++){
            title[i] = c.getString(0);
            contents[i] = c.getString(1);
            timestamp[i] = c.getString(2);
        }
        c.close();
    }

    @Override
    public void onClick(String timestamp) {
        Intent noteView = new Intent(this, ViewNote.class);
        Bundle extras = new Bundle();
        String[] projections = {NotesDbHelper.TITLE, NotesDbHelper.CONTENTS,
                NotesDbHelper.TIMESTAMP, LabelDbHelper.LABEL_NAME};

        Cursor c = notesDb.query(NotesDbHelper.TABLE_NAME, projections,
                NotesDbHelper.TIMESTAMP + "=?", new String[]{timestamp},
                null, null, null);

        Log.d("Timestamp", timestamp);

        c.moveToFirst();

        extras.putString(NotesDbHelper.TITLE, c.getString(0));
        extras.putString(NotesDbHelper.CONTENTS, c.getString(1));
        extras.putString(NotesDbHelper.TIMESTAMP, c.getString(2));
        extras.putString(LabelDbHelper.LABEL_NAME, c.getString(3));
        noteView.putExtras(extras);
        c.close();
        startActivity(noteView);
    }
}